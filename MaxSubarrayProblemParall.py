import time
import random
import multiprocessing
import os

# --- Блок 1: Допоміжні структури ---

class ChunkResult:
    """Зберігає проміжні результати для частини масиву"""
    def __init__(self, max_sub, max_pref, max_suff, total):
        self.max_sub = max_sub   # Максимальний підмасив всередині
        self.max_pref = max_pref # Максимальний префікс
        self.max_suff = max_suff # Максимальний суфікс
        self.total = total       # Повна сума

# --- Блок 2: Логіка обчислень ---

def solve_kadane_sequential(arr):
    """Класичний алгоритм Кадейна (O(n))"""
    if not arr:
        return 0
    max_so_far = arr[0]
    current_max = arr[0]
    for x in arr[1:]:
        current_max = max(x, current_max + x)
        max_so_far = max(max_so_far, current_max)
    return max_so_far

def process_chunk(chunk):
    """
    Функція-воркер, яка обробляє шматок масиву для паралельного алгоритму.
    Повертає об'єкт ChunkResult.
    """
    if not chunk:
        return ChunkResult(-float('inf'), -float('inf'), -float('inf'), 0)
    
    total = sum(chunk)
    
    # Знаходимо max_pref (префікс)
    curr = 0
    max_pref = -float('inf')
    for x in chunk:
        curr += x
        if curr > max_pref:
            max_pref = curr
            
    # Знаходимо max_suff (суфікс) - йдемо з кінця
    curr = 0
    max_suff = -float('inf')
    for x in reversed(chunk):
        curr += x
        if curr > max_suff:
            max_suff = curr
            
    # Знаходимо max_sub (всередині шматка) - звичайний Кадейн
    max_sub = solve_kadane_sequential(chunk)
    
    return ChunkResult(max_sub, max_pref, max_suff, total)

def merge_results(left, right):
    """Об'єднує результати двох сусідніх блоків"""
    # Новий повний підмасив може бути:
    # 1. Повністю в лівій частині
    # 2. Повністю в правій частині
    # 3. Перетинати межу (суфікс лівої + префікс правої)
    new_max_sub = max(left.max_sub, right.max_sub, left.max_suff + right.max_pref)
    
    # Новий префікс: або префікс лівого, або весь лівий + префікс правого
    new_max_pref = max(left.max_pref, left.total + right.max_pref)
    
    # Новий суфікс: або суфікс правого, або весь правий + суфікс лівого
    new_max_suff = max(right.max_suff, right.total + left.max_suff)
    
    new_total = left.total + right.total
    
    return ChunkResult(new_max_sub, new_max_pref, new_max_suff, new_total)

def solve_parallel(arr, num_processes=None):
    """Керуюча функція паралельного обчислення"""
    if num_processes is None:
        num_processes = os.cpu_count() or 4
        
    # Якщо масив малий, паралелізація тільки зашкодить (overhead)
    if len(arr) < 100000:
        return solve_kadane_sequential(arr)

    # Розбиття масиву на шматки
    chunk_size = len(arr) // num_processes
    chunks = []
    for i in range(num_processes):
        start = i * chunk_size
        # Останній шматок забирає все до кінця
        end = None if i == num_processes - 1 else (i + 1) * chunk_size
        chunks.append(arr[start:end])

    # Створення пулу процесів
    with multiprocessing.Pool(processes=num_processes) as pool:
        # Паралельне виконання process_chunk для кожного шматка
        results = pool.map(process_chunk, chunks)
    
    # Послідовне об'єднання результатів
    if not results:
        return 0
        
    final_result = results[0]
    for i in range(1, len(results)):
        final_result = merge_results(final_result, results[i])
        
    return final_result.max_sub

# --- Блок 3: Генерація даних та Меню ---

def generate_input_file(filename, size):
    print(f"Генерація {size} чисел у файл '{filename}'...")
    with open(filename, 'w') as f:
        # Генеруємо зразу рядок, щоб швидше писати
        data = (str(random.randint(-100, 100)) for _ in range(size))
        f.write(" ".join(data))
    print("Готово.")

def load_data(filename):
    print(f"Зчитування файлу '{filename}'...")
    with open(filename, 'r') as f:
        data = list(map(int, f.read().split()))
    print(f"Зчитано {len(data)} елементів.")
    return data

def main():
    filename = "large_input.txt"
    # Рекомендований розмір для помітної різниці: 10-20 млн елементів
    # Увага: 20 млн int займуть близько 150-200 МБ RAM + копії в процесах
    default_size = 10_000_000 
    
    while True:
        print("\n=== ЛАБОРАТОРНА РОБОТА: ПОСЛІДОВНІ vs ПАРАЛЕЛЬНІ ОБЧИСЛЕННЯ ===")
        print("1. Згенерувати дані (10 млн елементів - для навантаження)")
        print("2. Запустити тест порівняння")
        print("3. Вихід")
        
        choice = input("Оберіть дію: ")
        
        if choice == '1':
            try:
                sz = int(input(f"Введіть к-сть елементів (Enter для {default_size}): ") or default_size)
                start_gen = time.time()
                generate_input_file(filename, sz)
                print(f"Час генерації: {time.time() - start_gen:.2f} сек")
            except ValueError:
                print("Помилка введення числа.")

        elif choice == '2':
            if not os.path.exists(filename):
                print("Спочатку згенеруйте файл (пункт 1)!")
                continue
                
            data = load_data(filename)
            
            print(f"\n--- Починаємо тестування ({len(data)} елементів) ---")
            
            # --- ТЕСТ 1: ПОСЛІДОВНИЙ ---
            print("Запуск послідовного алгоритму (Kadane)...")
            start_seq = time.perf_counter()
            res_seq = solve_kadane_sequential(data)
            end_seq = time.perf_counter()
            time_seq = end_seq - start_seq
            print(f"Результат: {res_seq}")
            print(f"Час: {time_seq:.6f} сек")
            
            # --- ТЕСТ 2: ПАРАЛЕЛЬНИЙ ---
            cores = os.cpu_count()
            print(f"\nЗапуск паралельного алгоритму ({cores} ядра, multiprocessing)...")
            start_par = time.perf_counter()
            res_par = solve_parallel(data, num_processes=cores)
            end_par = time.perf_counter()
            time_par = end_par - start_par
            print(f"Результат: {res_par}")
            print(f"Час: {time_par:.6f} сек")
            
            # --- ВИСНОВКИ ---
            print("\n--- ПІДСУМКИ ---")
            if res_seq != res_par:
                print("УВАГА: Результати не співпадають! Десь помилка в логіці.")
            else:
                print("Результати валідні (співпадають).")
                
            print(f"Прискорення (Speedup): {time_seq / time_par:.2f}x")
            if time_par > time_seq:
                print("(Паралельний повільніший через витрати на створення процесів та копіювання пам'яті. "
                      "Спробуйте збільшити масив до 20-50 млн або запустіть на Linux/MacOS)")
        
        elif choice == '3':
            break

if __name__ == '__main__':
    # Цей захист обов'язковий для Windows при використанні multiprocessing
    multiprocessing.freeze_support()
    main()