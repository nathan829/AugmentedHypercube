/**
 * Created by Nathan on 18/01/2016.
 */
public class Vertex {

    String id;
    String[] adjacentIDs;
    private int adjacentIndex;

    private int dimension;

    public Vertex(String id, int dimension) {
        this.id = id;
        this.dimension = dimension;
        adjacentIDs = new String[2*dimension - 1];
        adjacentIndex = 0;
    }

    public String getID() {
        return id;
    }

    public String getAdjacent(int index) {
        return adjacentIDs[index];
    }

    public void setAdjacent(String adjacentVertexID) {
        adjacentIDs[adjacentIndex++] = adjacentVertexID;
    }

    public void removeAdjacentFromList(String adjacentID) {
        //find where in the list it is
        for (int i = 0; i < adjacentIndex; i++) {
            if (adjacentIDs[i].equals(adjacentID)) {
                //since it is now found, we remove by shuffling and reducing index
                shuffleDownList(i);
            }
        }
    }

    private void shuffleDownList(int index) {
        adjacentIndex--;
        adjacentIDs[index] = adjacentIDs[adjacentIndex];
        adjacentIDs[adjacentIndex] = null;
    }

    public boolean adjacentTest(String str1) {
        if(str1 == null || str1.isEmpty()) {
            return false;
        }

        for (int i = 0; i < adjacentIndex; i++) {
            if (adjacentIDs[i].equals(str1)) {
                return true;
            }
        }
        return false;
    }

    public int getAdjacentListLength() {
        return adjacentIndex;
    }

}
