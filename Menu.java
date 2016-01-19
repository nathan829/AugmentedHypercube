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
    private boolean lexOrdering;

    // string literal constant, used for menu options
    private static final String MENU_OPTIONS = "[1. # of Vertices/Edges] [2. Lexicographic Wirelength] [3. Optimal Embedding] [4. Manual Order] [5. Exit]";

    public Menu() {
        //creating instances of required objects
        handler = new InputHandler();
        calculator = new Calculator();
        df = new DecimalFormat("###,###");

        // starting the program
        running = true;
        // setting to some value, equivalently could have been set to false
        lexOrdering = true;
    }

    public void prompt() {
        while(running) {
            System.out.println(MENU_OPTIONS);
            int selection = handler.getMenuInt();
            directToTask(selection);
        }
    }

    private void directToTask(int selection) {
        // based on the users input, the corresponding task will be executed
        switch(selection) {
            case 1:
                // this task will execute a quick calculation for vertices and edges of a given dimension
                getDimensionFromUser();
                computeQuantities();
                break;
            case 2:
                // this task will compute the wirelength for all possible types of grids based on
                // the lexicogrphic embedding/ordering
                lexOrdering = true;
                getDimensionFromUser();
                constructEdges();
                break;
            case 3:
                // this task will compute all possible embeddings/ordering of a fixed grid
                // and return the best result/ordering
                lexOrdering = false;
                getDimensionFromUser();
                constructEdges();
                break;
            case 4:
                // this task will allow the user to input their own ordering of vertices, to then have
                // its wirelength calculated over the fixed grid
                lexOrdering = false;
                getDimensionFromUser();
                manualConstructEdges();
                break;
            case 5:
                terminate();
                break;
        }
    }

    private void terminate() {
        running = false;
    }

    private void constructEdges() {
        EdgeConstructor constructor = new EdgeConstructor(dimension, lexOrdering);
        constructor.start();
    }

    private void manualConstructEdges() {
        // passes to a different start funtion in edgeContructor, with the parameter being the ordering
        // of vertices entered by user
        EdgeConstructor constructor = new EdgeConstructor(dimension, lexOrdering);
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
                    System.out.println("Please enter numbers between 0 and " + (int)calculator.getVertices(dimension));
                    accepted = false;
                    continue;
                }
                // to make sure they have not already entered the number
                else if (i != 0) {
                    for (int j = 0; j < i; j++) {
                        if (arr[j] == arr[i]) {
                            System.out.println("You have already entered the vertex number " + arr[i]);
                            i--;
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
        dimension = handler.getDimensionInt(lexOrdering);
    }

    private void computeQuantities() {
        long vertices = calculator.getVertices(dimension);
        long edges = calculator.getEdges(dimension);
        displayQuantities(edges, vertices);
    }

    private void displayQuantities(long edges, long vertices) {
        System.out.println("\n    ***" + dimension + "-dimensional Augmented Hypercube***");
        System.out.println("\t\t   EDGES: " + df.format(edges));
        System.out.println("\t\tVERTICES: " + df.format(vertices) + "\n\n");
    }

}
