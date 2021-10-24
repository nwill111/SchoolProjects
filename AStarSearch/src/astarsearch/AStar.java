/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package astarsearch;

import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.math.*;
import java.util.Collections;

/**
 * AStar class
 * @author Nathan Williams
 */
public class AStar {

    private Node[][] nodes;
    private String[][] visualizedBoard;
    private ArrayList<Node> surroundingNodes;
    private ArrayList<Node> path;
    private PriorityQueue<Node> openList = new PriorityQueue<Node>();
    private HashMap closedList = new HashMap(225);
    private boolean traversable = false;

    /**
     * Node; Used to keep track of the starting node selected by the user
     */
    public Node startNode;

    /**
     * Node; Used to keep track of the ending node selected by the user 
     */
    public Node endNode;

    /**
     * Constructor for AStar
     */
    public AStar() {
        nodes = new Node[15][15];
        surroundingNodes = new ArrayList<Node>();
        visualizedBoard = new String[15][15];
        createBoard();
    }

    /**
     * Method used to create a 15x15 board 
     * 10 percent chance of a node being non-traversable
     */
    public void createBoard() {
        Random rand = new Random();

        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 15; y++) {
                int rng = rand.nextInt(10);

                if (rng == 0) {
                    nodes[x][y] = new Node(x, y, 1);
                } else {
                    nodes[x][y] = new Node(x, y, 0);
                }
            }
        }
    }

    /**
     * Checks the neighbors above and below the current node
     * If it is traversable, it is then added to the surroundingNodes arrayList
     * @param node Node to check
     */
    public void checkVerticalNeighbors(Node node) {
        int row = node.getRow();
        int col = node.getCol();
        int topNeighborRow = row - 1;
        int bottomNeighborRow = row + 1;

        if (row > 0) {
            if (nodes[topNeighborRow][col].getType() == 0) {
                surroundingNodes.add(nodes[topNeighborRow][col]);
            }
        }

        if (row < 14) {
            if (nodes[bottomNeighborRow][col].getType() == 0) {
                surroundingNodes.add(nodes[bottomNeighborRow][col]);
            }
        }
    }

    /**
     * Checks the neighbors left and right of the current node
     * If it is traversable, it is then added to the surroundingNodes arrayList
     * @param node Node to check
     */
    public void checkHorizontalNeighbors(Node node) {
        int row = node.getRow();
        int col = node.getCol();
        int leftNeighborCol = col - 1;
        int rightNeighborCol = col + 1;

        if (col > 0) {
            if (nodes[row][leftNeighborCol].getType() == 0) {
                surroundingNodes.add(nodes[row][leftNeighborCol]);
            }
        }

        if (col < 14) {
            if (nodes[row][rightNeighborCol].getType() == 0) {
                surroundingNodes.add(nodes[row][rightNeighborCol]);
            }
        }
    }

    /**
     * Checks the neighbors diagonal to the passed in node
     * Bottom left, bottom right, top left, and top right
     * @param node Node to check
     */
    public void checkDiagonalNeighbors(Node node) {
        int row = node.getRow();
        int col = node.getCol();

        //Top Left
        if (col > 0 && row > 0) {

            if (nodes[row - 1][col - 1].getType() == 0) {
                surroundingNodes.add(nodes[row - 1][col - 1]);
            }
        }

        // Bottom Left
        if (col > 0 && row < 14) {

            if (nodes[row + 1][col - 1].getType() == 0) {
                surroundingNodes.add(nodes[row + 1][col - 1]);
            }
        }

        // Top Right
        if (col < 14 && row < 0) {

            if (nodes[row - 1][col + 1].getType() == 0) {
                surroundingNodes.add(nodes[row - 1][col + 1]);
            }
        }

        //Bottom Right
        if (col < 14 && row < 14) {
            if (nodes[row + 1][col + 1].getType() == 0) {
                surroundingNodes.add(nodes[row + 1][col + 1]);
            }
        }
    }

    /**
     * Generates a list of all traversable neighbors around a node
     * If the neighbor is not in the closed list or non-traversable, 
     * then updates the neighboring nodes F, G, H and parent values
     * @param node Node to check
     */
    public void genNeighbors(Node node) {
        surroundingNodes.clear();

        checkVerticalNeighbors(node);
        checkHorizontalNeighbors(node);
        checkDiagonalNeighbors(node);

        for (Node n : surroundingNodes) {
            if (n.getType() == 0 && !(closedList.containsKey(n.getId()))) {
                calcHeuristic(n);
                calcGCost(n);
                n.setF();
                n.setParent(node);

                //Adds the node to the open list if not already contained
                if (!openList.contains(n)) {
                    openList.add(n);

                }

            }
        }
    }

    /**
     * Calculates the heuristic value of the node using the manhattan method
     * @param node Node to calculate heuristic value for
     */
    public void calcHeuristic(Node node) {
        int currentX = node.getRow();
        int currentY = node.getCol();
        int endX = endNode.getRow();
        int endY = endNode.getCol();

        int heuristic = (Math.abs(currentX - endX) + Math.abs(currentY - endY)) * 10;

        node.setH(heuristic);
    }

    /**
     * Calculates the G value of the node by calculating distance from the start
     * @param node Node to calculate g value for
     */
    public void calcGCost(Node node) {
        int row = node.getRow();
        int col = node.getCol();
        Node parent = node.getParent();

        if (parent != null) {

            int moveCost = parent.getG();

            if (parent.getRow() == row || parent.getCol() == col) {
                moveCost += 10;
            } else {
                moveCost += 14;
            }

            if (node.getG() > moveCost && node.getG() > 0) {
                node.setG(moveCost);
            }

        }

    }

    /**
     * Converts node array to a readable string array
     * @return String array containing the visualized board
     */
    public String[][] visualizeBoard() {
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 15; y++) {
                if (nodes[x][y].getType() == 0) {
                    // Traversable
                    visualizedBoard[x][y] = "O";
                } else {
                    //Non-traversable
                    visualizedBoard[x][y] = "X";
                }

                if (traversable) {
                    for (Node n : path) {
                        //Part of path
                        visualizedBoard[n.getRow()][n.getCol()] = "-";
                    }
                }

                if (startNode != null) {
                    //Starting node
                    visualizedBoard[startNode.getRow()][startNode.getCol()] = "🚗";
                }

                if (endNode != null) {
                    //Ending node
                    visualizedBoard[endNode.getRow()][endNode.getCol()] = "🏁";
                }
            }
        }
        return visualizedBoard;
    }

    /**
     * Generates a path between starting and ending nodes
     */
    public void generatePath() {
        path = new ArrayList<Node>();
        Node currentNode = endNode;

        while (!currentNode.equals(startNode)) {
            path.add(currentNode);
            currentNode = currentNode.getParent();
        }

        path.add(currentNode);
    }

    /**
     * AStar algorithm
     */
    public void aStar() {

        Node cur;
        openList.add(startNode);

        while (openList.size() != 0) {

            //Pop off node w/ lowest F
            cur = openList.poll();
            
            // Check if current is the goal node
            if (!cur.equals(endNode)) {
                
                /*If no, generate neighboring nodes and put current node into 
                the closed list */
                genNeighbors(cur);
                closedList.put(cur.getId(), cur);

            } else {
                //If yes, generate path
                generatePath();
                traversable = true;
                break;
            }

        }
    }
    
    /**
     * Print path arrayList
     */
    public void printPath() {
        
        ArrayList<Node> printedPath = path;
        Collections.reverse(printedPath);
        System.out.println("Path taken: " + printedPath);
    }

    /**
     * Sets the starting node
     * @param x x position of the starting node as int
     * @param y y position of the starting node as int
     */
    public void setStartNode(int x, int y) {
        startNode = nodes[x][y];
    }

    /**
     * Sets the ending node
     * @param x x position of the ending node as int
     * @param y y position of the ending node as int
     */
    public void setEndNode(int x, int y) {
        endNode = nodes[x][y];
    }

    /**
     * Gets the node at the specified coordinates
     * @param x x position of the node as int
     * @param y y position of the node as int
     * @return Node at position
     */
    public Node getNode(int x, int y) {
        return nodes[x][y];
    }

    /**
     * Checks if a path has been found
     * @return Boolean value; True if the path is traversable, false if not
     */
    public boolean isTraversable() {
        return traversable;
    }

}
