/**
 * Created by Nathan on 18/01/2016.
 */
public class EdgeConstructor {

    // binary object: needed for binary operations, comparisons and edge construction of bit string
    // represented vertices
    Binary binary;
    // Large scope grid object
    Grid grid;
    // array of vertex objects, used for labelling and storing adjacent vertices
    static private Vertex[] vertexList;

    // holds minimum value over all embeddings
    private int minimumWirelength;
    // set to true if all embeddings have been searched through
    private boolean minimumFound;
    // used for when searching for optimal embedding over all vertex permutations
    private int[] gridOrdering;
    // counts the number of embeddings which induce minimum wirelength
    private int quantitiyOfMinimumEmbeddings;
    // dimension passed from the menu class
    private int dimension;
    // result calculated based on the dimension. = 2^n
    private int numberOfVertices;
    // lexOrdering passed from the menu class
    private boolean lexOrdering;
    // counts the number of edges removed when removing repeated connections between respective vertices
    private int removalCount;

    public EdgeConstructor(int dimension, boolean lexOrdering) {
        // setting values
        this.dimension = dimension;
        this.lexOrdering = lexOrdering;
        numberOfVertices = (int)Math.pow(2, dimension);
        minimumWirelength = 0;
        removalCount = 0;
        minimumFound = false;
        quantitiyOfMinimumEmbeddings = 0;

        // creating instances of objects
        vertexList = new Vertex[numberOfVertices];
        binary = new Binary(dimension);

        if (!lexOrdering) {
            gridOrdering = new int[numberOfVertices];
        }
    }

    public void start() {
        generateVertices();
        generateHypercubeAndComplementEdges();
        removeEdgeCopies();

        System.out.println(removalCount + " edges created over " + numberOfVertices + " vertices");

        if (lexOrdering) {
            computeAllWirelengths();
        }
        else {
            populateIntArray();
            // this function will calculate wirelengths over all vertex permutations
            computeAllEmbeddings(gridOrdering);
            minimumFound = true;
            System.out.println("The minimum wirelength over all embedding is: " + minimumWirelength);
            System.out.println("These orderings include:");
            // needs to go through function again to find the minimal orderings
            computeAllEmbeddings(gridOrdering);
        }
    }

    public void manualStart(int[] arr) {
        generateVertices();
        generateHypercubeAndComplementEdges();
        removeEdgeCopies();

        System.out.println(removalCount + " edges created over " + numberOfVertices + " vertices");

        int calculatedWirelength = computeWirelengthForCustomOrdering(arr);
        System.out.println("Calculated Wirelength is: " + calculatedWirelength);
    }

    private void populateIntArray() {
        for (int i = 0; i < numberOfVertices; i++) {
            gridOrdering[i] = i;
        }
    }

    public void computeAllEmbeddings(int[] arr){
        permute(arr, 0);
    }

    // recursively finds all permutations of the given integer array
    private void permute(int[] arr, int index){
        int currentWirelength = 0;

        if(index >= arr.length - 1){ //If we are at the last element - nothing left to permute
            //System.out.println(Arrays.toString(arr));
            if(minimumFound) {
                currentWirelength = computeWirelengthForCustomOrdering(arr);
                if(currentWirelength == minimumWirelength) {
                    quantitiyOfMinimumEmbeddings++;
                    System.out.print(quantitiyOfMinimumEmbeddings + ": [");
                    for (int i = 0; i < arr.length - 1; i++) {
                        System.out.print(arr[i] + ",");
                    }
                    System.out.print(arr[arr.length - 1] + "]    " + currentWirelength);
                    System.out.println();
                }
                else {
                    return;
                }
            }
            else {
                currentWirelength = computeWirelengthForCustomOrdering(arr);
            }

            return;
        }

        for(int i = index; i < arr.length; i++){ //For each index in the sub array arr[index...end]

            //Swap the elements at indices index and i
            int t = arr[index];
            arr[index] = arr[i];
            arr[i] = t;

            //Recurse on the sub array arr[index+1...end]
            permute(arr, index+1);

            //Swap the elements back
            t = arr[index];
            arr[index] = arr[i];
            arr[i] = t;
        }
    }

    private int computeWirelengthForCustomOrdering(int[] arr) {
        int listLength, vertexIndex;
        int wirelength = 0;
        int gridSizeX = (int)(Math.pow(2, (int)(Math.ceil((double)(dimension / 2)))));
        int gridSizeY = (int)(Math.pow(2, (int)(Math.floor((double)(dimension / 2)))));

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

        //enable this if you want the wirelengths of ALL the orderings printed out
        //------------------------------------------------------------
//        System.out.print("Ordering: [");
//        for (int i = 0; i < arr.length - 1; i++) {
//            System.out.print(arr[i] + ",");
//        }
//        System.out.print(arr[arr.length-1] + "]       WL(AQ^" + dimension + ",M[" + gridSizeY + "x" + gridSizeX + "]) = " + wirelength);
//        System.out.println();
        //------------------------------------------------------------

        return wirelength;
    }

    private void computeAllWirelengths() {
        System.out.println("\n\n     ***Lexicographic Embeddings***");

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

        // looking at all the edges we have formed, we can accumulate the distances between them in order
        // to calculate the total wirelength
        for (int i = 0; i < numberOfVertices; i++) {
            listLength = vertexList[i].getAdjacentListLength();
            for (int j = 0; j < listLength; j++) {
                vertexIndex = binary.binaryStringToInt(vertexList[i].getAdjacent(j));
                wirelength += grid.getLexPathLength(i, vertexIndex);
            }
        }
        // we wish to find the minimum wirelength over all types of grids
        if ((wirelength < minimumWirelength) || (minimumWirelength == 0)) {
            minimumWirelength = wirelength;
        }

        // the following lines are purely for presentation
        int gridX = grid.getGridDimensionX();
        int gridY = grid.getGridDimensionY();

        System.out.println("WL(AQ^" + dimension + ",M[" + gridY + "x" + gridX + "]) = " + wirelength);
    }


}
