/**
 * Created by Nathan on 19/01/2016.
 */
public class Grid {
    int[][] vertexGrid;
    int gridSizeX;
    int gridSizeY;

    public Grid(int gridSizeX, int gridSizeY) {
        this.gridSizeX = gridSizeX;
        this.gridSizeY = gridSizeY;
        vertexGrid = new int[gridSizeX][gridSizeY];
    }

    public int getLexPathLength(int a, int b) {
        int ax = (a / gridSizeX);
        int ay = (a % gridSizeX);

        int bx = (b / gridSizeX);
        int by = (b % gridSizeX);

        return Math.abs(ax - bx) + Math.abs(ay - by);
    }

    public int getCustomPathLength(int a, int b, int[] arr) {
        int vertexIndex1 = -1;
        int vertexIndex2 = -1;

        //now to find the index of the values a and b in arr
        for (int i = 0; i < arr.length; i++) {
            if (a == arr[i]) {
                vertexIndex1 = i;
            }
            else if (b == arr[i]) {
                vertexIndex2 = i;
            }
            if ((vertexIndex1 != -1) && (vertexIndex2 != -1)) {
                break;
            }
        }

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

    public int getGridDimensionX() {
        return gridSizeX;
    }

    public int getGridDimensionY() {
        return gridSizeY;
    }

}
