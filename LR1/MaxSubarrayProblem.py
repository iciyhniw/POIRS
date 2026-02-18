import sys
import random
import time
import os

class MaxSubarraySolver:
    """
    Клас для розв'язання задачі пошуку підмасиву з максимальною сумою.
    Забезпечує незалежність логіки від інтерфейсу.
    """

    @staticmethod
    def generate_data(size, filename="input.txt", min_val=-100, max_val=100):
        """Генерує випадкові числа і записує їх у файл."""
        print(f"Генерація {size} елементів...")
        try:
            with open(filename, 'w') as f:
                # Використовуємо генератор для економії пам'яті при запису
                data = (str(random.randint(min_val, max_val)) for _ in range(size))
                f.write(" ".join(data))
            print(f"Дані успішно записано у '{filename}'.")
        except IOError as e:
            print(f"Помилка запису файлу: {e}")

    @staticmethod
    def read_data(filename="input.txt"):
        """Зчитує дані з файлу."""
        try:
            if not os.path.exists(filename):
                print(f"Файл '{filename}' не знайдено.")
                return None
            
            with open(filename, 'r') as f:
                content = f.read()
                # Перетворення рядка чисел у список цілих чисел
                return list(map(int, content.split()))
        except ValueError:
            print("Помилка: Файл містить некоректні дані (не цілі числа).")
            return None
        except IOError as e:
            print(f"Помилка читання файлу: {e}")
            return None

    @staticmethod
    def solve_kadane(arr):
        """
        Знаходить підмасив з максимальною сумою, використовуючи алгоритм Кадейна.
        Складність: O(n).
        Повертає: (максимальна_сума, список_елементів_підмасиву)
        """
        if not arr:
            return 0, []

        max_so_far = arr[0]      # Глобальний максимум
        current_max = arr[0]     # Локальний максимум
        
        start_index = 0
        end_index = 0
        temp_start_index = 0

        # Починаємо з другого елемента
        for i in range(1, len(arr)):
            # Якщо поточний елемент більший, ніж (поточна сума + елемент),
            # то починаємо нову послідовність з цього елемента.
            if arr[i] > current_max + arr[i]:
                current_max = arr[i]
                temp_start_index = i
            else:
                current_max += arr[i]

            # Оновлюємо глобальний максимум, якщо знайшли кращу суму
            if current_max > max_so_far:
                max_so_far = current_max
                start_index = temp_start_index
                end_index = i

        return max_so_far, arr[start_index : end_index + 1]

def main():
    solver = MaxSubarraySolver()
    input_file = "input_data.txt"
    output_file = "result.txt"
    
    while True:
        print("\n--- МЕНЮ ---")
        print("1. Згенерувати вхідні дані (тестовий режим - мало даних)")
        print("2. Згенерувати вхідні дані (великий об'єм - для тесту 5 сек)")
        print("3. Розв'язати задачу (з файлу)")
        print("4. Вихід")
        
        choice = input("Ваш вибір: ")

        if choice == '1':
            # Для відлагодження (п.4 інструкції)
            solver.generate_data(size=10, filename=input_file, min_val=-20, max_val=20)
            print("Створено малий набір даних для ручної перевірки.")

        elif choice == '2':
            # Для тесту навантаження (п.5 інструкції)
            # Python інтерпретований, тому 5 секунд це приблизно 20-40 млн елементів для O(n)
            # Але зчитування файлу займе більше часу, ніж сам алгоритм.
            # Встановимо 10,000,000 елементів для балансу.
            size = int(input("Введіть кількість елементів (рекомендується 50000000 для ~5 сек): ") or 50000000)
            solver.generate_data(size=size, filename=input_file)

        elif choice == '3':
            print("Зчитування даних...")
            data = solver.read_data(input_file)
            
            if data:
                print(f"Зчитано {len(data)} елементів. Початок обчислень...")
                
                # Замір часу (п.6 інструкції)
                # Вимірюємо ТІЛЬКИ час роботи алгоритму
                start_time = time.perf_counter()
                max_sum, subarray = solver.solve_kadane(data)
                end_time = time.perf_counter()
                
                execution_time = end_time - start_time
                
                # Вивід результатів
                print(f"\n--- РЕЗУЛЬТАТ ---")
                print(f"Час виконання обчислень: {execution_time:.6f} сек")
                print(f"Максимальна сума: {max_sum}")
                
                # Якщо масив занадто великий, не виводимо його весь на екран
                if len(subarray) <= 20:
                    print(f"Послідовність: {subarray}")
                else:
                    print(f"Послідовність (перші 10...останні 10): {subarray[:10]} ... {subarray[-10:]}")
                    print(f"Довжина послідовності: {len(subarray)}")

                # Збереження у файл
                with open(output_file, 'w') as f:
                    f.write(f"Max Sum: {max_sum}\n")
                    f.write(f"Execution Time: {execution_time:.6f} sec\n")
                    f.write(f"Sequence: {subarray}\n")
                print(f"Повний результат збережено у '{output_file}'")

        elif choice == '4':
            print("Роботу завершено.")
            break
        else:
            print("Невірний вибір.")

if __name__ == "__main__":
    main()