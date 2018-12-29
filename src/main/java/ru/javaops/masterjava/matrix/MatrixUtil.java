package ru.javaops.masterjava.matrix;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    public static class MT implements Runnable {

        private final int row;
        private final int[][] matrixA;
        private final int[][] matrixB;
        int[][] matrixC;

        public MT(int row, int[][] matrixA, int[][] matrixB, int[][] matrixC) {
            this.row = row;
            this.matrixA = matrixA;
            this.matrixB = matrixB;
            this.matrixC = matrixC;
        }

        @Override
        public void run() {
            calculateForRow(matrixA, matrixB, matrixC, row, matrixA.length);
        }
    }

    //Вычисляет значение row'ого элемента
    static void calculateForRow(int[][] matrixA, int[][] matrixB, int[][] matrixC, int row, int length) {
        //Без транспонирования
        for (int j = 0; j < length; j++) {//кол-во стлб 2 матрицы
            int summand = 0;
            for (int k = 0; k < length; k++) {//кол-во строк 2 матрицы
                summand += matrixA[row][k] * matrixB[k][j];
            }
            matrixC[row][j] = summand;
        }
    }

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        //Если закроем (shutdown) - будет исключение при 2 выполнении метода, поэтому создаю новый каждый раз
        final ExecutorService es = Executors.newFixedThreadPool(10);

//        for (int i = 0; i < matrixSize; i++) {
//            for (int j = 0; j < matrixSize; j++) {
//                double summand = 0.0;
//                for (int k = 0; k < matrixSize; k++) {
//                    summand += matrixA[i][k] * matrixB[k][j];
//                }
//                matrixC[i][j] = (int) summand;
//            }
//        }

        for (int i = 0; i < matrixSize; i++) {
            MT thread = new MT(i, matrixA, matrixB, matrixC);
            es.submit(thread);
//            completionService.submit(thread, thread);
        }

        es.shutdown();
        es.awaitTermination(100, TimeUnit.SECONDS);

        return matrixC;
    }

    // TODO optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += matrixA[i][k] * matrixB[k][j];
                }
                matrixC[i][j] = sum;
            }
        }
        return matrixC;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
