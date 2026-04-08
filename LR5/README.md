# LR5 - Кешування та контейнеризація Docker

## Що реалізовано

- `content-service`
  - підключено `Spring Cache`
  - підключено Redis як зовнішній кеш
  - кешується `GET /api/v1/contents/{id}`
  - реалізовано політику очищення кешу:
    - TTL у Redis (`APP_CACHE_TTL_MINUTES`, за замовчуванням `10`)
    - інвалідація кешу при `update`, `delete`, `publish`, `edit`, зміні `status` та `averageRating`
- `interaction-service`, `moderation-service`
  - підготовлені до запуску в контейнерах
  - базова адреса `content-service` тепер задається через змінні середовища
- Docker
  - створено `Dockerfile` для кожного мікросервісу
  - створено `docker-compose.yml` для запуску всієї системи
  - додано `Redis`
  - додано `PostgreSQL` з трьома базами: `content_db`, `interaction_db`, `moderation_db`

## Структура

- `content-service` - сервіс контенту з кешуванням у Redis
- `interaction-service` - сервіс коментарів і рейтингів
- `moderation-service` - сервіс модерації
- `docker-compose.yml` - оркестрація контейнерів
- `docker/postgres/init/01-create-databases.sql` - створення баз даних
- `demo-requests.http` - сценарій демонстрації

## Як працює кеш

Кешування увімкнено для методу отримання контенту за id:

- перший виклик `GET /api/v1/contents/{id}` іде в БД та записує результат у Redis
- повторний виклик з тим самим `id` повертається з Redis
- при зміні контенту відповідний запис кешу очищається
- додатково використовується TTL, щоб кеш не зберігався безмежно

Для наочності в `content-service` додано керовану затримку при cache miss:

- `APP_CACHE_DEMO_DELAY_MS=350`

Ця затримка спрацьовує тільки тоді, коли метод реально виконується. Якщо відповідь повертається з кешу, затримки немає, тому різницю часу легко показати на захисті.

## Локальний запуск без Docker

### 1. Запустити Redis у Docker

```powershell
docker run --name lr5-redis -p 6379:6379 -d redis:7-alpine
```

### 2. Запустити `content-service`

```powershell
cd C:\POLITECH\intellij\POIRS\LR5\content-service
$env:SPRING_DATA_REDIS_HOST="localhost"
$env:SPRING_DATA_REDIS_PORT="6379"
.\mvnw.cmd spring-boot:run
```

### 3. Запустити `interaction-service`

```powershell
cd C:\POLITECH\intellij\POIRS\LR5\interaction-service
$env:CONTENT_SERVICE_BASE_URL="http://localhost:8081"
.\mvnw.cmd spring-boot:run
```

### 4. Запустити `moderation-service`

```powershell
cd C:\POLITECH\intellij\POIRS\LR5\moderation-service
$env:CONTENT_SERVICE_BASE_URL="http://localhost:8081"
.\mvnw.cmd spring-boot:run
```

## Запуск усієї системи через Docker Compose

У корені `LR5`:

```powershell
docker compose up --build
```

У результаті піднімуться:

- `postgres`
- `redis`
- `content-service`
- `interaction-service`
- `moderation-service`

Порти:

- `content-service` -> `8081`
- `interaction-service` -> `8082`
- `moderation-service` -> `8083`
- `postgres` -> `5432`
- `redis` -> `6379`

Зупинка:

```powershell
docker compose down
```

Зупинка з видаленням volume:

```powershell
docker compose down -v
```

## Перевірка кешування

### Підготовка даних

1. Створити автора
2. Створити категорію
3. Створити контент

Готові запити є в `demo-requests.http`.

### Варіант перевірки через PowerShell

Перший виклик:

```powershell
Measure-Command {
  Invoke-RestMethod -Method GET -Uri "http://localhost:8081/api/v1/contents/1" | Out-Null
}
```

Повторний виклик:

```powershell
Measure-Command {
  Invoke-RestMethod -Method GET -Uri "http://localhost:8081/api/v1/contents/1" | Out-Null
}
```

Очікування:

- перший виклик довший
- повторний виклик швидший, бо дані беруться з Redis

### Перевірка очищення кешу

1. Викликати `GET /api/v1/contents/1`
2. Виконати `PUT /api/v1/contents/1` або модерацію/оновлення рейтингу
3. Повторити `GET /api/v1/contents/1`

Після зміни контенту кеш для цього `id` буде скинутий, тому наступний запит знову піде в БД.

## Збірка образів окремо

```powershell
docker build -t lr5-content-service ./content-service
docker build -t lr5-interaction-service ./interaction-service
docker build -t lr5-moderation-service ./moderation-service
```

## Що перевірити в контейнерах

- усі контейнери стартували без помилок
- `interaction-service` і `moderation-service` успішно звертаються до `content-service`
- `GET /api/v1/contents/{id}` працює через Redis
- після зміни даних кеш очищається
- дані сервісів зберігаються в PostgreSQL

## Короткий аналіз

### Переваги контейнеризації

- однакове середовище запуску на різних машинах
- простіший старт усієї системи однією командою
- зручне керування залежностями: Redis, БД, сервіси
- краща ізоляція компонентів
- легше масштабувати і переносити систему

### Вплив кешування на продуктивність

- повторні GET-запити виконуються швидше
- менше навантаження на БД
- знижується час відповіді для часто запитуваних даних
- з'являється потреба контролювати актуальність кешу, тому потрібні TTL та інвалідація

## Важливо

У цьому середовищі не вдалося виконати повну автоматичну перевірку, тому що:

- відсутній локальний `docker`
- відсутній локальний `mvn`
- Maven Wrapper не може завантажити залежності через обмеження мережі

Тобто код і конфігурація підготовлені, але фактичний запуск `docker compose up --build` треба виконати на твоїй машині, де є Docker Desktop і доступ до інтернету для першого завантаження залежностей.
