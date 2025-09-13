import java.util.*;
import java.util.stream.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        // Создание списка из 1,000,000 случайных чисел
        List<Integer> numbers = generateRandomNumbers(1_000_000);

        System.out.println("Сравнение производительности stream() vs parallelStream()");
        System.out.println("Размер списка: " + numbers.size() + " элементов\n");

        // Тестирование последовательного потока
        System.out.println("=== ПОСЛЕДОВАТЕЛЬНЫЙ ПОТОК (stream()) ===");
        long sequentialTime = measureSequentialStream(numbers);

        // Тестирование параллельного потока
        System.out.println("\n=== ПАРАЛЛЕЛЬНЫЙ ПОТОК (parallelStream()) ===");
        long parallelTime = measureParallelStream(numbers);

        // Анализ результатов
        System.out.println("\n=== АНАЛИЗ РЕЗУЛЬТАТОВ ===");
        System.out.printf("Последовательный поток: %d мс\n", sequentialTime);
        System.out.printf("Параллельный поток: %d мс\n", parallelTime);
        System.out.printf("Разница: %d мс\n", Math.abs(sequentialTime - parallelTime));

        if (sequentialTime < parallelTime) {
            double speedup = (double) sequentialTime / parallelTime;
            System.out.printf("Последовательный поток быстрее в %.2f раз\n", 1/speedup);
        } else {
            double speedup = (double) parallelTime / sequentialTime;
            System.out.printf("Параллельный поток быстрее в %.2f раз\n", 1/speedup);
        }
    }

    // Генерация случайных чисел
    private static List<Integer> generateRandomNumbers(int size) {
        Random random = new Random();
        return IntStream.range(0, size)
                .map(i -> random.nextInt(1000))
                .boxed()
                .collect(Collectors.toList());
    }

    // Измерение времени для последовательного потока
    private static long measureSequentialStream(List<Integer> numbers) {
        long startTime = System.currentTimeMillis();

        long result = numbers.stream()
                .filter(n -> n % 2 == 0)          // Фильтрация: только чётные числа
                .map(n -> n * 2)                  // Преобразование: умножение на 2
                .mapToLong(Integer::longValue)    // Конвертация в long
                .sum();                           // Агрегация: сумма всех чисел

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("Результат: " + result);
        System.out.println("Время выполнения: " + duration + " мс");

        return duration;
    }

    // Измерение времени для параллельного потока
    private static long measureParallelStream(List<Integer> numbers) {
        long startTime = System.currentTimeMillis();

        long result = numbers.parallelStream()
                .filter(n -> n % 2 == 0)          // Фильтрация: только чётные числа
                .map(n -> n * 2)                  // Преобразование: умножение на 2
                .mapToLong(Integer::longValue)    // Конвертация в long
                .sum();                           // Агрегация: сумма всех чисел

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("Результат: " + result);
        System.out.println("Время выполнения: " + duration + " мс");

        return duration;
    }
}