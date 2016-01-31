/**
 * Created by Nathan on 18/01/2016.
 */
public class Calculator {
    public long getEdges(int n) {
        return (2*n-1)*(long)Math.pow(2,n-1);
    }

    public long getVertices(int n) {
        return (long)Math.pow(2,n);
    }

    private int[] getNextRow(int dimension, boolean lexicographic, int from, int rowSize) {

        int[] arr = new int[rowSize];
        int count = 0;

        if (lexicographic) {
            for (int i = from; i < from + rowSize; i++) {
                arr[count++] = i;
            }
        }
        else {
            for (int i = rowSize + from - 1; i >= from; i--) {
                arr[count++] = i;
            }
        }
        return arr;
    }

    public int[] getOrdering(int dimension) {
        int rowSize = (int)(Math.pow(2, Math.floor((double)dimension/2)));
        int numberOfRows = (int)Math.pow(2, Math.ceil((double)dimension / 2));
        int[] arr = new int[(int)Math.pow(2, (double)dimension)];
        int[] thisRow;
        int from = -rowSize;
        boolean lexicographic = true;

        for (int i = 0; i < numberOfRows; i++) {
            from += rowSize;
            thisRow = getNextRow(dimension, lexicographic, from, rowSize);
            lexicographic = !lexicographic;
            for (int j = 0; j < rowSize; j++) {
                arr[i*rowSize+j] = thisRow[j];
            }
        }
        return arr;
    }

    public int[] getOrderingCustomGrid(int dimension, int columns, int rows) {
        int[] arr = new int[(int)Math.pow(2, (double)dimension)];
        int[] thisRow;
        int from = -columns;
        boolean lexicographic = true;

        for (int i = 0; i < rows; i++) {
            from += columns;
            thisRow = getNextRow(dimension, lexicographic, from, columns);
            lexicographic = !lexicographic;
            for (int j = 0; j < columns; j++) {
                arr[i*columns+j] = thisRow[j];
            }
        }
        return arr;
    }

    // functions printing out vertex values to console
    public void generateOptimalOrderingCustomGrid(int dimension, boolean lexicographic, int from, int rowSize) {

        if (from == (Math.pow(2, (double)dimension))) {
            return;
        }

        if (lexicographic) {
            for (int i = from; i < from + rowSize; i++) {
                System.out.println(i);
            }
        }
        else {
            for (int i = rowSize + from - 1; i >= from; i--) {
                System.out.println(i);
            }
        }

        generateOptimalOrderingCustomGrid(dimension, !lexicographic, from + rowSize, rowSize);
    }

    public void generateOptimalOrdering(int dimension, boolean lexicographic, int from) {
        int rowSize = (int)(Math.pow(2, Math.floor((double)dimension/2)));

        if (from == (Math.pow(2, (double)dimension))) {
            return;
        }

        if (lexicographic) {
            for (int i = from; i < from + rowSize; i++) {
                System.out.println(i);
            }
        }
        else {
            for (int i = rowSize + from - 1; i >= from; i--) {
                System.out.println(i);
            }
        }



        generateOptimalOrdering(dimension, !lexicographic, from + rowSize);
    }
}
