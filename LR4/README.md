# LR4 - Мікросервіси. Декомпозиція моноліту

## Що вимагається в цій лабораторній

У цій роботі потрібно взяти моноліт із LR3 і розділити його на незалежні сервіси з окремими зонами відповідальності, окремими портами та окремими базами даних. Після цього треба показати, що сервіси взаємодіють через REST, а також продемонструвати, що буде при недоступності одного з них.

## Що зроблено в цій папці

- `media-service-monolith` - копія моноліту з LR3 для аналізу
- `content-service` - сервіс контенту, авторів, категорій та історії редагувань
- `interaction-service` - сервіс коментарів і рейтингів
- `moderation-service` - сервіс модерації

## Межі відповідальності

- `content-service`
  - CRUD для контенту
  - публікація контенту
  - історія редагувань
  - зберігання середнього рейтингу контенту
  - зберігання статусу контенту
- `interaction-service`
  - створення коментарів
  - створення рейтингів
  - обчислення середнього рейтингу
  - REST-виклик у `content-service` для оновлення `averageRating`
- `moderation-service`
  - зберігання рішень модерації
  - REST-виклик у `content-service` для оновлення `status`

## Порти та бази даних

- `content-service`
  - порт: `8081`
  - БД: `jdbc:h2:mem:content_db`
- `interaction-service`
  - порт: `8082`
  - БД: `jdbc:h2:mem:interaction_db`
- `moderation-service`
  - порт: `8083`
  - БД: `jdbc:h2:mem:moderation_db`

## Міжсервісна взаємодія

- `interaction-service -> content-service`
  - перевірка існування контенту
  - оновлення середнього рейтингу контенту
- `moderation-service -> content-service`
  - перевірка існування контенту
  - оновлення статусу контенту

## Як запускати локально

В окремих терміналах:

```powershell
cd C:\Users\iciyhniw\Politech_3\POIRC_1\LR4\content-service
.\mvnw.cmd spring-boot:run
```

```powershell
cd C:\Users\iciyhniw\Politech_3\POIRC_1\LR4\interaction-service
.\mvnw.cmd spring-boot:run
```

```powershell
cd C:\Users\iciyhniw\Politech_3\POIRC_1\LR4\moderation-service
.\mvnw.cmd spring-boot:run
```

## Сценарій демонстрації

1. Запустити всі три сервіси.
2. Створити автора, категорію і контент у `content-service`.
3. Додати рейтинг через `interaction-service`.
4. Перевірити, що в `content-service` оновився `averageRating`.
5. Виконати модерацію через `moderation-service`.
6. Перевірити, що в `content-service` оновився `status`.
7. Зупинити `content-service`.
8. Спробувати додати коментар або модерацію ще раз.
9. Показати відповідь `503 Service Unavailable`.

Детальні запити є у файлі `demo-requests.http`.
