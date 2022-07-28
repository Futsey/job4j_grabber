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
                System.out.println("��� ����� ������, ����������?"
                        + System.lineSeparator()
                        + "1. �������� �������"
                        + System.lineSeparator()
                        + "2. ���������� ���������"
                        + System.lineSeparator()
                        + "3. ���������� ���������"
                        + System.lineSeparator()
                        + "4. ���������� ��������"
                        + System.lineSeparator()
                        + "5. �����");
                menuCount++;
            } else {
                System.out.println("��, �������. ��� ������??"
                        + System.lineSeparator()
                        + "1. �������� �������"
                        + System.lineSeparator()
                        + "2. ���������� ���������"
                        + System.lineSeparator()
                        + "3. ���������� ���������"
                        + System.lineSeparator()
                        + "4. ���������� ��������"
                        + System.lineSeparator()
                        + "5. �����");
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
        System.out.println("������� ������ �������");
        arraySize = scanner.nextInt();
        data.insert(arraySize);
        System.out.println("������ ������!" + System.lineSeparator());
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
        System.out.println("�� �������!");
    }
}
