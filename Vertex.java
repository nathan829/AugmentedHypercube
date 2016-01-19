/**
 * Created by Nathan on 18/01/2016.
 */
public class Vertex {
    // binary representation of the current vertex
    String id;
    // an array of the ID's of all the adjacent vertices
    String[] adjacentIDs;
    // the index for the next available spot in the adjacentID array
    private int adjacentIndex;
    // dimension passed by EdgeConstructor
    private int dimension;

    public Vertex(String id, int dimension) {
        // setting values
        this.id = id;
        this.dimension = dimension;
        adjacentIndex = 0;
        // the maximum number of adjacent vertices for given dimension
        adjacentIDs = new String[2*dimension - 1];
    }

    public void setAdjacent(String adjacentVertexID) {
        adjacentIDs[adjacentIndex++] = adjacentVertexID;
    }

    public void removeAdjacentFromList(String adjacentID) {
        //find where in the list it is
        for (int i = 0; i < adjacentIndex; i++) {
            if (adjacentIDs[i].equals(adjacentID)) {
                //since it is now found, we remove by shuffling down and reducing adjacentIndex
                shuffleDownList(i);
            }
        }
    }

    private void shuffleDownList(int index) {
        // fills in the gap with the last element, a way of removing it from the list
        adjacentIndex--;
        adjacentIDs[index] = adjacentIDs[adjacentIndex];
        adjacentIDs[adjacentIndex] = null;
    }

    public boolean adjacentTest(String str1) {
        if(str1 == null || str1.isEmpty()) {
            return false;
        }

        // if the parameter ID is found in the list of adjacent vertices
        for (int i = 0; i < adjacentIndex; i++) {
            if (adjacentIDs[i].equals(str1)) {
                return true;
            }
        }
        return false;
    }

    // accessor functions
    public int getAdjacentListLength() {
        return adjacentIndex;
    }

    public String getID() {
        return id;
    }

    public String getAdjacent(int index) {
        return adjacentIDs[index];
    }
}
