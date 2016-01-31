/**
 * Created by Nathan on 18/01/2016.
 */
public class EdgeConstructor {

    private final int numberOfFixedVertices = 8;

    private int leftTop1Value;
    private int leftTop2Value;
    private int rightTop1Value;
    private int rightTop2Value;
    private int leftBottom1Value;
    private int leftBottom2Value;
    private int rightBottom1Value;
    private int rightBottom2Value;

    private final int leftTop1Index;
    private final int leftTop2Index;
    private final int rightTop1Index;
    private final int rightTop2Index;
    private final int leftBottom1Index;
    private final int leftBottom2Index;
    private final int rightBottom1Index;
    private final int rightBottom2Index;



    // binary object: needed for binary operations, comparisons and edge construction of bit string
    // represented vertices
    private Binary binary;
    // Large scope grid object
    private Grid grid;

    Calculator calculator;

    // array of vertex objects, used for labelling and storing adjacent vertices
    static private Vertex[] vertexList;
    // holds minimum value over all embeddings
    private int minimumWirelength;
    // set to true if all embeddings have been searched through
    private boolean minimumFound;
    // used for when searching for optimal embedding over all vertex permutations
    private int[] gridOrdering;
    // array of ints with available vertex numbers, passed into permutation function
    // stores all available indices for potential vertices to be placed
    private int[] availableIndex;
    // stores all available vertex values, used to calculating permutations of allowe vertices
    private int[] availableValues;
    // counter variables for availableIndex and availableValues respectively
    private int availableIndexCounter = 0;
    private int availableValueCounter = 0;
    // counts the number of embeddings which induce minimum wirelength
    private int quantityOfMinimumEmbeddings;
    // dimension passed from the menu class
    private int dimension;
    // result calculated based on the dimension. = 2^n
    private int numberOfVertices;
    // lexOrdering passed from the menu class
    private boolean ordering;
    // counts the number of edges removed when removing repeated connections between respective vertices
    private int removalCount;
    // counts number of columns and resets when reaches end
    private int columnCount;

    // used only as a counter when printing all possible embeddings
    private int secondaryCounter = 0;

    private boolean improvedOrdering;
    private boolean fixedVertices = false;

    public EdgeConstructor(int dimension, boolean ordering, boolean improvedOrdering) {
        // setting values
        this.dimension = dimension;
        this.ordering = ordering;
        this.improvedOrdering = improvedOrdering;

        numberOfVertices = (int)Math.pow(2, dimension);
        minimumWirelength = 0;
        removalCount = 0;
        minimumFound = false;
        quantityOfMinimumEmbeddings = 0;
        columnCount = 0;

        // calculating the two vertices in each of the 4 corners
        leftTop1Index = 0;
        leftTop2Index = 1;
        rightTop1Index = (int)(Math.pow(2, Math.ceil((double)dimension / 2))) - 2;
        rightTop2Index = (int)(Math.pow(2, Math.ceil((double)dimension / 2))) - 1;
        leftBottom1Index = (int)(Math.pow(2, Math.ceil((double)dimension /2)) * (Math.pow(2, Math.floor((double)dimension /2)) - 1));
        leftBottom2Index = (int)(Math.pow(2, Math.ceil((double)dimension /2)) * (Math.pow(2, Math.floor((double)dimension /2)) - 1)) + 1;
        rightBottom1Index = numberOfVertices - 2;
        rightBottom2Index = numberOfVertices - 1;

        // creating instances of objects
        vertexList = new Vertex[numberOfVertices];
        binary = new Binary(dimension);
        calculator = new Calculator();

        // for when finding the optimal path of a given dimension. no need to be created otherwise
        if (!ordering) {
            gridOrdering = new int[numberOfVertices];
            availableIndex = new int[numberOfVertices - numberOfFixedVertices];
            availableValues = new int[numberOfVertices - numberOfFixedVertices];
        }
    }

    public void start() {
        fixedVertices = false;
        generateVertices();
        generateHypercubeAndComplementEdges();
        removeEdgeCopies();

        System.out.println("creating " + removalCount + " edges over " + numberOfVertices + " vertices");

        if (ordering) {
            // used for finding best grids for lexicographic
            computeAllWirelengths();
        }
        else {
            ordering = false; // try to debug why the manual path dimensions arent working for the optimal calculation
            populateIntArray();
            // this function will calculate wirelengths over all vertex permutations
            computeAllEmbeddings(gridOrdering);
            minimumFound = true;
            System.out.println("The minimum wirelength over all embeddings is: " + minimumWirelength);
            System.out.println("These orderings include: ");
            // this function needs to be run again to find the minimal orderings, since minnimumFound = true;
           computeAllEmbeddings(gridOrdering);
        }
    }

    public void fixVertices() {
        fixedVertices = true;
        generateVertices();
        generateHypercubeAndComplementEdges();
        removeEdgeCopies();

        System.out.println("creating " + removalCount + " edges over " + numberOfVertices + " vertices");

        ordering = false; // try to debug why the manual path dimensions arent working for the optimal calculation
        populateCustomIntArray();
        // this function will calculate wirelengths over all vertex permutations
        computeAllEmbeddings(availableValues);
        minimumFound = true;
        System.out.println("The minimum wirelength over all embeddings is: " + minimumWirelength);
        System.out.println("These orderings include: ");
        // this function needs to be run again to find the minimal orderings, since minnimumFound = true;
        computeAllEmbeddings(availableValues);
    }

    public void manualStart(int[] arr) {
        generateVertices();
        generateHypercubeAndComplementEdges();
        removeEdgeCopies();

        System.out.println(removalCount + " edges created over " + numberOfVertices + " vertices");

        int calculatedWirelength = computeWirelengthForCustomOrdering(arr);
        System.out.println("Calculated Wirelength is: " + calculatedWirelength);
    }

    public void calculateImprovedWirelength(int[] myOrdering, int columns, int rows) {
        generateVertices();
        generateHypercubeAndComplementEdges();
        removeEdgeCopies();

        calculateWirelengthOptimally(myOrdering, columns, rows);
    }

    private void populateIntArray() {
        for (int i = 0; i < numberOfFixedVertices; i++) {
            gridOrdering[i] = i;
        }
    }

    private void populateCustomIntArray() {
        InputHandler handler = new InputHandler();

        System.out.println("Enter the value indexed at " + leftTop1Index);
        leftTop1Value = handler.getInt();
        System.out.println("Enter the value indexed at " + leftTop2Index);
        leftTop2Value = handler.getInt();
        System.out.println("Enter the value indexed at " + rightTop1Index);
        rightTop1Value = handler.getInt();
        System.out.println("Enter the value indexed at " + rightTop2Index);
        rightTop2Value = handler.getInt();
        System.out.println("Enter the value indexed at " + leftBottom1Index);
        leftBottom1Value = handler.getInt();
        System.out.println("Enter the value indexed at " + leftBottom2Index);
        leftBottom2Value = handler.getInt();
        System.out.println("Enter the value indexed at " + rightBottom1Index);
        rightBottom1Value = handler.getInt();
        System.out.println("Enter the value indexed at " + rightBottom2Index);
        rightBottom2Value = handler.getInt();


        // top left vertices 1 and 2 respectively
        gridOrdering[leftTop1Index] = leftTop1Value;
        gridOrdering[leftTop2Index] = leftTop2Value;

        // top right vertices 1 and 2 respectively
        gridOrdering[rightTop1Index] = rightTop1Value;
        gridOrdering[rightTop2Index] = rightTop2Value;

        // bottom left vertices 1 and 2 respectively;
        gridOrdering[leftBottom1Index] = leftBottom1Value;
        gridOrdering[leftBottom2Index] = leftBottom2Value;

        // bottom right vertices 1 and 2 respectively
        gridOrdering[rightBottom1Index] = rightBottom1Value;
        gridOrdering[rightBottom2Index] = rightBottom2Value;

        for (int i = 0; i < numberOfVertices; i++) {
            // available indexes
            if (!((i == leftTop1Index) || (i == leftTop2Index) || (i == rightTop1Index) || (i == rightTop2Index) || (i == leftBottom1Index) || (i == leftBottom2Index) || (i == rightBottom1Index) || (i == rightBottom2Index))) {
                availableIndex[availableIndexCounter++] = i;
            }
            // available values
            if (!((i == leftTop1Value) || (i == leftTop2Value) || (i == rightTop1Value) || (i == rightTop2Value) || (i == leftBottom1Value) || (i == leftBottom2Value) || (i == rightBottom1Value) || (i  == rightBottom2Value))) {
                availableValues[availableValueCounter++] = i;
            }
        }

        if (availableValueCounter != availableIndexCounter) {
            System.out.println("ERROR: cannot fill the proper values");
        }

    }

    public void computeAllEmbeddings(int[] arr){
        // setting the parameter to 1 will fix the first vertex, this helps in processing
        // due to the transitivity/symmetry of the augmented cube
        permute(arr, 0);
    }

    private void permute(int[] arr, int index){
        // recursively finds all permutations of the given integer array
        int currentWirelength;

        //If we are at the last element - nothing left to permute
        if(index >= arr.length - 1) {

            if (fixedVertices) {
                for (int i = 0; i < arr.length; i++) {
                    gridOrdering[availableIndex[i]] = arr[i];
                }
            }

            if(minimumFound) {
                currentWirelength = computeWirelengthForCustomOrdering(gridOrdering);
                if (currentWirelength == minimumWirelength) {
                   quantityOfMinimumEmbeddings++;
                   System.out.println(quantityOfMinimumEmbeddings);
                   System.out.print("[");
                   columnCount = 0;
                   for (int i = 0; i < gridOrdering.length - 1; i++) {
                       System.out.print(gridOrdering[i]);
                       if (columnCount++ == (grid.getGridDimensionX() - 1)) {
                           columnCount = 0;
                           System.out.println("]");
                           System.out.print("[");
                       } else {
                           System.out.print(",");
                       }
                   }
                   System.out.print(gridOrdering[gridOrdering.length - 1] + "]    ");
                   System.out.println();
                }
                else {
                    return;
                }
            }
            else {
                computeWirelengthForCustomOrdering(gridOrdering);
//                System.out.println("For " + secondaryCounter++);
//                System.out.print("[");
//                columnCount = 0;
//                for (int i = 0; i < gridOrdering.length - 1; i++) {
//                    System.out.print(gridOrdering[i]);
//                    if(columnCount++ == (3)) { // for a 4 dimensional space
//                        columnCount = 0;
//                        System.out.println("]");
//                        System.out.print("[");
//                    }
//                    else {
//                        System.out.print(",");
//                    }
//                }
//                System.out.print(gridOrdering[gridOrdering.length - 1] + "]    ");
//                System.out.println();
//
//                System.out.println("WL = " + currentWirelength);
            }

            return;
        }

        // For each index in the sub array arr[index...end]
        for (int i = index; i < arr.length; i++){ //
                //Swap the elements at indices index and i
                int t = arr[index];
                arr[index] = arr[i];
                arr[i] = t;

                //Recurse on the sub array arr[index+1...end]
                permute(arr, index + 1);

                //Swap the elements back
                t = arr[index];
                arr[index] = arr[i];
                arr[i] = t;
//            }
        }
    }

    private int computeWirelengthForCustomOrdering(int[] arr) {
        int listLength, vertexIndex;
        int wirelength = 0;

        // change these values of these grid dimensions to change the fixed host graph
        int gridSizeX = (int)(Math.pow(2, Math.floor((double)dimension/2)));
        int gridSizeY = (int)(Math.pow(2, Math.ceil((double)dimension/2)));

//        int gridSizeX = (int)(Math.pow(2, (double)dimension -1));
//        int gridSizeY = 2;

        // dimensions for a path
//        int gridSizeX = (int)(Math.pow(2, (double)dimension));
//        int gridSizeY = 1;


//        int gridSizeX = (int)Math.pow(2, dimension);
//        int gridSizeY = (int)Math.pow(2, 0);

        // new instance of Grid
        grid = new Grid(gridSizeX, gridSizeY);

        // goes through all connecting vertices and computes the shortest distance between each pair of
        // vertices by using a function in Grid object. The sum of all distances between connecting vertices
        // is by definition the wirelength of this particular embedding/ordering
        for (int i = 0; i < numberOfVertices; i++) {
            listLength = vertexList[i].getAdjacentListLength();
            for (int j = 0; j < listLength; j++) {
                vertexIndex = binary.binaryStringToInt(vertexList[i].getAdjacent(j));
                wirelength += grid.getCustomPathLength(i, vertexIndex, arr);
            }
        }
        if(!minimumFound) {
            // finds the minimum wirelength out of all embeddings that pass through this function
            if ((wirelength < minimumWirelength) || (minimumWirelength == 0)) {
                minimumWirelength = wirelength;
            }
        }

        return wirelength;
    }

    public void calculateWirelengthOptimally(int[] arr, int gridSizeX, int gridSizeY) {
        int listLength, vertexIndex;
        int wirelength = 0;

        // new instance of Grid
        grid = new Grid(gridSizeX, gridSizeY);

        // goes through all connecting vertices and computes the shortest distance between each pair of
        // vertices by using a function in Grid object. The sum of all distances between connecting vertices
        // is by definition the wirelength of this particular embedding/ordering
        for (int i = 0; i < numberOfVertices; i++) {
            listLength = vertexList[i].getAdjacentListLength();
            for (int j = 0; j < listLength; j++) {
                vertexIndex = binary.binaryStringToInt(vertexList[i].getAdjacent(j));
                wirelength += grid.getCustomPathLength(i, vertexIndex, arr);
            }
        }

        System.out.println("WL(AQ_(" + dimension + "), M[" + gridSizeY + "x" + gridSizeX + "]) = " + wirelength);
    }

    private void computeAllWirelengths() {
        if (improvedOrdering) {
            System.out.println("\n\n     *** Improved Ordering of Dimension " + dimension + " ***");
        }
        else {
            System.out.println("\n\n     *** Lexicographic Ordering of Dimension " + dimension + " ***");
        }

        int gridSizeX;
        int gridSizeY;

        // goes through all possible combinations of grid dimensions to find the best host graph for
        // the lexicographic embedding
        for (int i = 0; i <= dimension; i++) {
            gridSizeX = (int)(Math.pow(2, i));
            gridSizeY = (int)(Math.pow(2, dimension - i));

            grid = new Grid(gridSizeX, gridSizeY);
            calculateWirelength();
        }

        System.out.println("The minimum wirelength is " + minimumWirelength + " over all types of grids");
        System.out.println("\n\n");

    }

    private void generateVertices() {
        String previous = null;
        String tempID;

        // generates a new instance of each vertex along with a binaryID which will be used
        // later to determine which vertices are connected by an edge
        for (int count = 0; count < numberOfVertices; count++) {
            if(count != 0) {
                previous = vertexList[count - 1].getID();
            }
            tempID = binary.getNext(previous);
            vertexList[count] = new Vertex(tempID, dimension);
        }
    }

    private void generateHypercubeAndComplementEdges() {
        // goes through all combinations of vertex pairs to determine if they need to be connected
        for (int i = 0; i < numberOfVertices; i++) {
            for (int j = 0; j < numberOfVertices; j++) {
                if(i == j) {
                    continue;
                }
                //adding the hypercube edges
                if(binary.hypercubeEdgeTest(vertexList[i].getID(),vertexList[j].getID())) {
                    vertexList[i].setAdjacent(vertexList[j].getID());
                }
                //adding the complementary edges
                else if(binary.complementEdgeTest(vertexList[i].getID(),vertexList[j].getID())) {
                    vertexList[i].setAdjacent(vertexList[j].getID());
                }
            }
        }
    }

    private void removeEdgeCopies() {
        int listLength;
        int vertexIndex;

        // we do not want to double count edges so if one vertex is adjacent to the other
        // then it is redundant for it to be the other way around
        for (int i = 0; i < numberOfVertices; i++) {
            listLength = vertexList[i].getAdjacentListLength();
            //looking at the list of adjacent vertices for vertexList[i]
            for (int j = 0; j < listLength; j++) {
                vertexIndex = binary.binaryStringToInt(vertexList[i].getAdjacent(j));
                //if the vertex is pointing back, remove it from list
                if (vertexList[vertexIndex].adjacentTest(vertexList[i].getID())) {
                    vertexList[vertexIndex].removeAdjacentFromList(vertexList[i].getID());
                    removalCount++;
                }
            }
        }
    }

    private void calculateWirelength() {
        int listLength;
        int wirelength = 0;
        int vertexIndex;

        int gridX = grid.getGridDimensionX();
        int gridY = grid.getGridDimensionY();

        if (improvedOrdering) {
            int[] arr = calculator.getOrderingCustomGrid(dimension, gridX, gridY);

            for (int i = 0; i < numberOfVertices; i++) {
                listLength = vertexList[i].getAdjacentListLength();
                for (int j = 0; j < listLength; j++) {
                    vertexIndex = binary.binaryStringToInt(vertexList[i].getAdjacent(j));
                    wirelength += grid.getCustomPathLength(i, vertexIndex, arr);
                }
            }
        }
        else {
            // looking at all the edges we have formed, we can accumulate the distances between them in order
            // to calculate the total wirelength
            for (int i = 0; i < numberOfVertices; i++) {
                listLength = vertexList[i].getAdjacentListLength();
                for (int j = 0; j < listLength; j++) {
                    vertexIndex = binary.binaryStringToInt(vertexList[i].getAdjacent(j));
                    wirelength += grid.getLexPathLength(i, vertexIndex);
                }
            }
        }
        // we wish to find the minimum wirelength over all types of grids
        if ((wirelength < minimumWirelength) || (minimumWirelength == 0)) {
            minimumWirelength = wirelength;
        }

        System.out.println("WL(AQ^" + dimension + ",M[" + gridY + "x" + gridX + "]) = " + wirelength);
    }


}
