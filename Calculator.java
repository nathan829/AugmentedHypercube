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

//    public void calculateFormula(int dimension, int m) {
//        Binary binary = new Binary(dimension);
//        String value = binary.intToBinaryString(m);
//        char[] binaryString = value.toCharArray();
//        int[] binaryValues = new int[dimension+1];
//        binaryValues[0] = 0;
//
//        for (int i = 0; i < dimension; i++) {
//            if (binaryString[i] == '0') {
//                binaryValues[i+1] = 0;
//            }
//            else {
//                binaryValues[i+1] = 1;
//            }
//        }
//
//        int internalSum = 0;
//
//        for (int i = 1; i <= dimension; i++) {
//            internalSum += binaryValues[dimension - i + 1] * (2 * i - 1) * (int)Math.pow(2, i - 2);
//            System.out.println("value added: " + binaryValues[dimension - i + 1 ] * (2 * i - 1) *(int)Math.pow(2, i - 2));
//        }
//
//        System.out.println("Binary Values are");
//        for (int i = 1; i < binaryValues.length; i++) {
//            System.out.print(binaryValues[i]);
//        }
//        System.out.println();
//        System.out.println("Internal Sum = " + internalSum);
//
//        System.exit(0);
//
//        // -------------------------------------------------------------------
//
//        int connectingSum = 0;
//        int miniSum = 0;
//
//        for (int i = 0; i < dimension - 1; i++) {
//            for (int j = 0; j <= i; j++) {
//                miniSum += binaryValues[dimension - j - 1] * (int)Math.pow(2, j+1);
//            }
//            connectingSum += miniSum * binaryValues[dimension - i - 2];
//            System.out.println("adding value " + miniSum * binaryValues[dimension - i - 2]);
//            miniSum = 0;
//        }
//        System.out.println("Connecting Sum = " + connectingSum);
//
//        System.out.println("\nTotal Edges calcuated on " + m + " vertices is " + (connectingSum + internalSum));
//
//    }

    public void calculateFormula(int dimension, int m) {
        Binary binary = new Binary(dimension);
        String value = binary.intToBinaryString(m);
        char[] binaryString = value.toCharArray();
        int[] binaryValues = new int[dimension];

        for (int i = 0; i < dimension; i++) {
            if (binaryString[i] == '0') {
                binaryValues[i] = 0;
            }
            else {
                binaryValues[i] = 1;
            }
        }

        int internalSum = 0;

        for (int i = 0; i < dimension; i++) {
            internalSum += binaryValues[dimension - i - 1]*(2 * i - 1) * (int)Math.pow(2, i - 1);
            System.out.println("value added: " + binaryValues[dimension - i- 1 ] * (2 * i - 1) *(int)Math.pow(2, i - 1));
        }

        System.out.print("Binary Values are: ");
        for (int i = 0; i < binaryValues.length; i++) {
            System.out.print(binaryValues[i]);
        }
        System.out.println();
        System.out.println("Internal Sum = " + internalSum);

        // -------------------------------------------------------------------

        int connectingSum = 0;
        int miniSum = 0;

        for (int i = 0; i < dimension - 1; i++) {
            for (int j = 0; j <= i; j++) {
                miniSum += binaryValues[dimension - j - 1] * (int)Math.pow(2, j+1);
            }
            connectingSum += miniSum * binaryValues[dimension - i - 2];
            System.out.println("adding value " + miniSum * binaryValues[dimension - i - 2]);
            miniSum = 0;
        }
        System.out.println("Connecting Sum = " + connectingSum);

        System.out.println("\nTotal Edges calcuated on " + m + " vertices is " + (connectingSum + internalSum));

    }

    public void complementEquation(int r1, int r2) {
        int value = (int)Math.pow(2, r1) * (int)Math.floor(Math.pow(2, 2 * r2 - 2)) + (int)Math.pow(2, r2) * (int)Math.floor(Math.pow(2, 2 * r1 - 3));
        System.out.println("\nvalue = " + value + "\n");
    }

    public void testEquation(int r1, int r2, int dimension) {
        int wirelength = 0;
        int c1 = 0;
        int h1 = 0;
        int temp = 0;
        // --------------------
        // c(1)
        if (r1 == 0) {
            c1 += (int)Math.pow(2, 2 * dimension - 2);
        }
        else {
            c1 += (int)Math.pow(2, 2 * r1 + r2 - 2);
        }
        wirelength += c1;
        System.out.println("c(1) = " + c1);
        // --------------------
        // h(1)
        if (r1 == 0) {
            h1 += (int)Math.pow(2, 2 * dimension - 2);   
        }
        else if (r1 == 1) {
            h1 += (int)Math.pow(2, 2 * dimension - 3) + (int)Math.pow(2, dimension - 1);
        }
        else {
            h1 += (int)Math.pow(2, 2 * r1 + r2 - 2);
        }
        wirelength += h1;
        System.out.println("h(1) = " + h1);
        // --------------------
        // h(i)
        for (int i = 2; i <= dimension; i++) {
            if (r1 <= i - 1) {
                temp = (int)Math.pow(2, 2 * dimension - (i + 1)); 
                wirelength += temp;
            }
            else if (r1 == i) {
                temp = (int)Math.pow(2, 2 * dimension - (i + 2)) + (int)Math.pow(2, dimension - 1);   
                wirelength += temp;
            }
            else {
                temp = (int)Math.pow(2, 2 * r1 + r2 - 2);
                wirelength += temp;
            }
            System.out.println("h(" + i + ") = " + temp);
        }
        int hypercube = wirelength - c1;
        System.out.println("Hypercube edges = " + hypercube);
        // --------------------
        // c(i)
        for (int i = 2; i < dimension; i++) {
            if (r1 <= i - 1) {
                 temp = (int)Math.floor(3 * (int)Math.pow(2, i - 3)) * (int)Math.pow(2, 2 * dimension - (2 * i - 1));
                 wirelength += temp;
            }
            else if (r1 == i) {
                temp = (int)Math.pow(2, 2 * dimension - (i + 2)) + (int)Math.pow(2, dimension - 1);
                wirelength += temp;
            }
            else {
                temp = (int)Math.floor(3 * (int)Math.pow(2, i - 3)) * (int)Math.pow(2, 2 * r1 + r2 - (2 * i - 1));
                wirelength += temp;
            }
            System.out.println("c(" + i + ") = " + temp);
        }
        System.out.println("WL = " + wirelength);
    }

    public void getComplementWL() {

    }
}
