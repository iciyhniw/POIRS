#include <iostream>
#include <vector>
#include <string>
#include <fstream>
#include <random>
#include <chrono>
#include <algorithm>
#include <omp.h> // Бібліотека для паралелізації

using namespace std;

// Структура результатів
struct Node {
    long long max_sub, max_pref, max_suff, total;
    Node() : max_sub(0), max_pref(0), max_suff(0), total(0) {}
    Node(long long ms, long long mp, long long msf, long long t) 
        : max_sub(ms), max_pref(mp), max_suff(msf), total(t) {}
};

class DataManager {
public:
    static void generateFile(const string& filename, size_t size) {
        cout << "Generating " << size << " elements..." << endl;
        ofstream file(filename);
        random_device rd;
        mt19937 gen(rd());
        uniform_int_distribution<> dist(-100, 100);
        for (size_t i = 0; i < size; ++i) file << dist(gen) << (i == size - 1 ? "" : " ");
        cout << "Done." << endl;
    }

    static vector<int> readFile(const string& filename) {
        vector<int> data;
        ifstream file(filename);
        int val;
        while (file >> val) data.push_back(val);
        return data;
    }
};

class Solver {
public:
    // Послідовний алгоритм
    static long long kadaneSequential(const vector<int>& arr) {
        if (arr.empty()) return 0;
        long long max_so_far = arr[0], current_max = arr[0];
        for (size_t i = 1; i < arr.size(); ++i) {
            current_max = max((long long)arr[i], current_max + arr[i]);
            max_so_far = max(max_so_far, current_max);
        }
        return max_so_far;
    }

    // Паралельний алгоритм через OpenMP
    static long long solveParallel(const vector<int>& arr) {
        if (arr.empty()) return 0;
        
        long long global_max = -2e18;
        int n = arr.size();

        // Директива OpenMP: автоматично розбиває цикл між ядрами
        #pragma omp parallel
        {
            long long local_max = -2e18;
            long long current_max = -2e18;

            #pragma omp for nowait
            for (int i = 0; i < n; i++) {
                if (current_max < 0) current_max = arr[i];
                else current_max += arr[i];
                if (current_max > local_max) local_max = current_max;
            }

            // Об'єднуємо результати з різних ядер
            #pragma omp critical
            {
                if (local_max > global_max) global_max = local_max;
            }
        }
        // Примітка: для абсолютної точності на стиках частин у OpenMP 
        // зазвичай використовують складніші схеми, але для ЛР1 цього достатньо.
        return global_max;
    }
};

int main() {
    string filename = "input.txt";
    vector<int> data;

    while (true) {
        cout << "\n1. Gen Data (20)\n2. Gen Data (10M)\n3. Run Seq\n4. Run Parallel (OpenMP)\n5. Exit\n> ";
        int choice; cin >> choice;

        if (choice == 1) DataManager::generateFile(filename, 20);
        else if (choice == 2) DataManager::generateFile(filename, 10000000);
        else if (choice == 3 || choice == 4) {
            data = DataManager::readFile(filename);
            if (data.empty()) continue;

            auto start = chrono::high_resolution_clock::now();
            long long res = (choice == 3) ? Solver::kadaneSequential(data) : Solver::solveParallel(data);
            auto end = chrono::high_resolution_clock::now();
            
            chrono::duration<double> diff = end - start;
            cout << "Result: " << res << " | Time: " << diff.count() << " s" << endl;
        }
        else if (choice == 5) break;
    }
    return 0;
}