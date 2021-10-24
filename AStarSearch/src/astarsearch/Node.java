package astarsearch;

/**
 *
 * @author Nathan Williams
 */
public class Node implements Comparable<Node> {

    private int row, col, f, g, h, type;
    private Node parent;
    private static int count = 0;
    private int id = 0;

    /**
     * Node constructor
     *
     * @param r Row of node as int
     * @param c Column of node as int
     * @param t Whether or not a node is traversable (0 being traversable, 1
     * being not) as int
     */
    public Node(int r, int c, int t) {
        row = r;
        col = c;
        type = t;
        parent = null;
        id = count++;
        //type 0 is traverseable, 1 is not
    }

    //mutator methods to set values
    /**
     * Sets F value by adding g and h
     */
    public void setF() {
        f = g + h;
    }

    /**
     * Sets G value
     *
     * @param value g value as int
     */
    public void setG(int value) {
        g = value;
    }

    /**
     * Sets H value
     *
     * @param value H as int
     */
    public void setH(int value) {
        h = value;
    }

    /**
     * Sets the node's parent
     *
     * @param n Node to be set as parent
     */
    public void setParent(Node n) {
        parent = n;
    }

    /**
     * Sets the type of the node
     *
     * @param n type as int
     */
    public void setType(int n) {
        type = n;
    }

    //accessor methods to get values
    /**
     * Gets F value
     * @return F value as int
     */
    public int getF() {
        return f;
    }

    /**
     * Gets G value
     * @return G value as int
     */
    public int getG() {
        return g;
    }

    /**
     * Gets H value
     * @return H value as int
     */
    public int getH() {
        return h;
    }

    /**
     * Gets parent of a node
     * @return parent as node
     */
    public Node getParent() {
        return parent;
    }

    /**
     * Gets row location of the node
     * @return row as int
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets column location of the node
     * @return column as int
     */
    public int getCol() {
        return col;
    }

    /**
     * Gets the type of the node
     * @return type as int
     */
    public int getType() {
        return type;
    }

    /**
     * Gets the Id of the node
     * @return id as int
     */
    public int getId() {
        return id;
    }

    public boolean equals(Object in) {
        //typecast to Node
        Node n = (Node) in;

        return row == n.getRow() && col == n.getCol();
    }

    public String toString() {
        return "Node: " + row + "_" + col;
    }

    @Override
    public int compareTo(Node o) {
        return Integer.compare(this.f, o.f);
    }

}
