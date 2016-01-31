/**
 * Created by Nathan on 18/01/2016.
 */

import java.text.DecimalFormat;

public class Menu {

    // Input handler object: to detect out of range numbers and avoid floating points/characters to be entered
    InputHandler handler;
    // Calculator object: used for small calculations such as vertex and edge quantities
    Calculator calculator;
    // imported from java.text: displaying commas separating large numbers
    DecimalFormat df;

    // program runs only when this boolean is true
    private boolean running;
    // dimension of cube, inputted from user and widely used
    private int dimension;
    // if a lexicographic ordering is required
    private boolean ordering;

    private boolean improvedOrdering;

    // string literal constant, used for menu options
    private static final String MENU_OPTIONS = "[1. Improved Ordering WL (Specific Grid)] [2. WL of all Grids] [3. Optimal Embedding] [4. Manual Order] [5. Exit]";

    public Menu() {
        //creating instances of required objects
        handler = new InputHandler();
        calculator = new Calculator();
        df = new DecimalFormat("###,###");

        // starting the program
        running = true;
        // setting to some value, equivalently could have been set to false
        ordering = true;

        improvedOrdering = false;
    }

    public void prompt() {
        while(running) {
            System.out.println(MENU_OPTIONS);
            int selection = handler.getMenuInt(5);
            directToTask(selection);
        }
    }

    private void directToTask(int selection) {
        // based on the users input, the corresponding task will be executed
        switch(selection) {
            case 1:
                ordering = true;
                getDimensionFromUser();
                computeOptimalVertexOrdering();
                break;
            case 2:
                // this task will compute the wirelength for all possible types of grids based on
                // the lexicogrphic embedding/ordering
                ordering = true;
                improvedOrdering = false;
                getDimensionFromUser();
                allGrids();
                break;
            case 3:
                // this task will compute all possible embeddings/ordering of a fixed grid
                // and return the best result/ordering
                ordering = false;
                improvedOrdering = false;
                getDimensionFromUser();
                optimalEmbeddings();
//                constructEdges();
                break;
            case 4:
                // this task will allow the user to input their own ordering of vertices, to then have
                // its wirelength calculated over the fixed grid
                ordering = false;
                improvedOrdering = false;
                getDimensionFromUser();
                manualConstructEdges();
                break;
            case 5:
                terminate();
                break;
        }
    }

    private void optimalEmbeddings() {
        System.out.println("Would you like to fix vertices? (y/n)");
        int selection  = handler.getYesNo();
        if (selection == 'y') {
            EdgeConstructor constructor = new EdgeConstructor(dimension, ordering, improvedOrdering);
            constructor.fixVertices();
        }
        else {
            constructEdges();
        }
    }

    private void terminate() {
        running = false;
    }

    private void constructEdges() {
        EdgeConstructor constructor = new EdgeConstructor(dimension, ordering, improvedOrdering);
        constructor.start();
    }

    private void allGrids() {
        System.out.println("[1. Lexicographic] [2. Improved]");
        int selection = handler.getMenuInt(2);
        if (selection  != 1) {
            improvedOrdering = true;
        }
        constructEdges();
    }

    private void manualConstructEdges() {
        // passes to a different start funtion in edgeContructor, with the parameter being the ordering
        // of vertices entered by user
        EdgeConstructor constructor = new EdgeConstructor(dimension, ordering, improvedOrdering);
        int numberOfVertices = (int)calculator.getVertices(dimension);
        // set the size of array to be the number of vertices in graph
        int[] arr = new int[numberOfVertices];
        System.out.println("Enter an ordering of vertices from 0 to " + (numberOfVertices-1) + " and no repeats:");
        // ensures that the entered order of numbers is an acceptable input
        arr = handleCustomOrderingInput(arr);
        constructor.manualStart(arr);
    }

    private int[] handleCustomOrderingInput(int[] arr) {
        boolean accepted;
        for (int i = 0; i < (int)calculator.getVertices(dimension); i++) {
            accepted = false;
            while (!accepted) {
                arr[i] = handler.getInt();
                // to make sure the entered number is in range
                if ((arr[i] < 0) || (arr[i] >= (int)calculator.getVertices(dimension))) {
                    System.out.println("Please enter numbers between 0 and " + ((int)calculator.getVertices(dimension)-1));
                    accepted = false;
                    continue;
                }
                // to make sure they have not already entered the number
                else if (i != 0) {
                    for (int j = 0; j < i; j++) {
                        if (arr[j] == arr[i]) {
                            System.out.println("You have already entered the vertex number " + arr[i--]);
                            accepted = false;
                            break;
                        }
                    }
                    accepted = true;
                }
                else {
                    accepted = true;
                }
            }
        }
        return arr;
    }

    private void getDimensionFromUser() {
        System.out.println("Enter the required dimension of the Augmented Hypercube");
        dimension = handler.getDimensionInt(ordering);
    }

    private void computeOptimalVertexOrdering() {
        int columns = (int)(Math.pow(2, Math.floor((double)dimension/2)));
        int rows = (int)(Math.pow(2, Math.ceil((double)dimension/2)));
        System.out.println("The default grid size is M[" + rows + "x" + columns + "]");
        System.out.println("Would you like to change these dimensions? (y/n)");
        if(handler.getYesNo() == 'y') {
            System.out.println("You entered yes");
            getCustomGridSize();
        }
        else {
            System.out.println("You entered no");
            // enable this to print out all of the vertex ordering
//            calculator.generateOptimalOrdering(dimension, true, 0);
            int[] arr = calculator.getOrdering(dimension);
            EdgeConstructor constructor = new EdgeConstructor(dimension, ordering, improvedOrdering);
            constructor.calculateImprovedWirelength(arr, columns, rows);
        }
    }

    private void getCustomGridSize() {
        System.out.println("Enter the number of columns");
        int columns = handler.getColumns(dimension);
        int colPower = getPower(columns);
        int rows = (int)Math.pow(2, (double)dimension-colPower);
        System.out.println("The chosen grid is M[" + rows + "x" + columns + "]");
        // enable this if you want to print out the vertex ordering
//        calculator.generateOptimalOrderingCustomGrid(dimension, true, 0, columns);
        int[] arr = calculator.getOrderingCustomGrid(dimension, columns, rows);
        EdgeConstructor constructor = new EdgeConstructor(dimension, ordering, improvedOrdering);
        constructor.calculateImprovedWirelength(arr, columns, rows);
    }

    private int getPower(int n) {
        return (int)((Math.log(n))/(Math.log(2)));
    }


}
