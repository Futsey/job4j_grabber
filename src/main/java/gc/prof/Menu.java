package gc.prof;

import java.util.Random;
import java.util.Scanner;

public class Menu {

    private int button;
    private int arraySize = 0;
    private Data data = new RandomArray(new Random());
    private Scanner scanner = new Scanner(System.in);

    public void mainMenu() {
        int menuCount = 0;
        int randomArray = 1;
        int bubbleSort = 2;
        int insertSort = 3;
        int mergeSort = 4;
        int exit = 5;
        while (button != exit) {
            if (menuCount == 0) {
                System.out.println("Что будем делать, насяльника?"
                        + System.lineSeparator()
                        + "1. Создание массива"
                        + System.lineSeparator()
                        + "2. Сортировка пузырьком"
                        + System.lineSeparator()
                        + "3. Сортировка вставками"
                        + System.lineSeparator()
                        + "4. Сортировка слиянием"
                        + System.lineSeparator()
                        + "5. Выход");
                menuCount++;
            } else {
                System.out.println("Ок, сделали. Что дальше??"
                        + System.lineSeparator()
                        + "1. Создание массива"
                        + System.lineSeparator()
                        + "2. Сортировка пузырьком"
                        + System.lineSeparator()
                        + "3. Сортировка вставками"
                        + System.lineSeparator()
                        + "4. Сортировка слиянием"
                        + System.lineSeparator()
                        + "5. Выход");
            }
            button = scanner.nextInt();
            if (button == randomArray) {
                createRandomArray();
            }
            if (button == bubbleSort) {
                doBubbleSort();
            }
            if (button == insertSort) {
                doInsertSort();
            }
            if (button == mergeSort) {
                doMergeSort();
            }
            if (button == exit) {
                exit();
            }
        }
    }

    private void createRandomArray() {
        System.out.println("Задайте размер массива");
        arraySize = scanner.nextInt();
        data.insert(arraySize);
        System.out.println("Массив создан!" + System.lineSeparator());
    }

    private void doBubbleSort() {
        BubbleSort bSort = new BubbleSort();
        bSort.sort(data);
    }

    private void doInsertSort() {
        InsertSort iSort = new InsertSort();
        iSort.sort(data);
    }

    private void doMergeSort() {
        MergeSort mSort = new MergeSort();
        mSort.sort(data);
    }

    private void exit() {
        System.out.println("До встречи!");
    }
}
