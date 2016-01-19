/**
 * Created by Nathan on 19/01/2016.
 */
public class Grid {
    // this holds the dimensions of the required grid, used for calculating distances
    int[][] vertexGrid;
    // grid dimensions
    int gridSizeX;
    int gridSizeY;

    public Grid(int gridSizeX, int gridSizeY) {
        // setting grid dimensions
        this.gridSizeX = gridSizeX;
        this.gridSizeY = gridSizeY;

        // initialising the size of the grid
        vertexGrid = new int[gridSizeX][gridSizeY];
    }

    public int getLexPathLength(int a, int b) {
        // since the shortest path can be taken as direct right angles to the other vertex
        // we can just use the simple formula |ax-bx|+|ay+by|
        int ax = (a / gridSizeX);
        int ay = (a % gridSizeX);

        int bx = (b / gridSizeX);
        int by = (b % gridSizeX);

        return Math.abs(ax - bx) + Math.abs(ay - by);
    }

    public int getCustomPathLength(int a, int b, int[] arr) {
        int vertexIndex1 = -1;
        int vertexIndex2 = -1;

        // now to find the INDEX of the respective values a and b in arr and
        // compare them to the positions on our grid array, which is computed in the
        // getLexPathLength() function
        for (int i = 0; i < arr.length; i++) {
            // finding index of a
            if (a == arr[i]) {
                vertexIndex1 = i;
            }
            // finding index of b
            else if (b == arr[i]) {
                vertexIndex2 = i;
            }
            // if they have already been found, no need to keep looking
            if ((vertexIndex1 != -1) && (vertexIndex2 != -1)) {
                break;
            }
        }

        // if one or both of them was not found, then display error
        if((vertexIndex1 == -1) || (vertexIndex2 == -1)) {
            System.out.print("ERROR:: could not find " + a + " or " + b + " in ordering: ");
            for (int i = 0; i < arr.length; i++) {
                System.out.print(arr[i] + ",");
            }
            System.out.print("   v1 = " + vertexIndex1 + ", v2 = " + vertexIndex2);
            System.out.println();
        }

        return getLexPathLength(vertexIndex1, vertexIndex2);
    }

    // accessor functions
    public int getGridDimensionX() {
        return gridSizeX;
    }

    public int getGridDimensionY() {
        return gridSizeY;
    }

}
