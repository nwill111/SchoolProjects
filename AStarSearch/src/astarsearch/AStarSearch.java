package astarsearch;

import java.util.*;

/**
 * 
 * @author Nathan Williams
 *
 */
public class AStarSearch {

    /**
     * Main
     * @param args
     */
    public static void main(String[] args) {

        AStar board = new AStar();

        System.out.println("Below is the raw board:");
        printBoard(board);

        Scanner sc = new Scanner(System.in);
        boolean validInput = false;
        int startX = -1;
        int startY = -1;
        int endX = -1;
        int endY = -1;

        // Loops until a valid input is entered
        while (!validInput) {
       //     try {
                System.out.println("Enter the X cord of the starting area (0-14)");
                startX = Integer.parseInt(sc.nextLine());

                System.out.println("Enter the Y cord of the starting area (0-14)");
                startY = Integer.parseInt(sc.nextLine());

                System.out.println("Enter the X cord of the ending area (0-14)");
                endX = Integer.parseInt(sc.nextLine());

                System.out.println("Enter the Y cord of the ending area (0-14)");
                endY = Integer.parseInt(sc.nextLine());

                if (isValid(board, startX, startY, endX, endY)) {
                    
                    // Sets valid input to true to break the loop
                    validInput = true;
                    
                    // Uses user inputted cords to set start and ending nodes
                    board.setStartNode(startX, startY);
                    board.setEndNode(endX, endY);

                    board.aStar();

                    if (board.isTraversable()) {
                        System.out.println("Path Found! Printing path...");

                        printBoard(board);
                        board.printPath();
                    } else {
                        System.out.println("Path could not be found!");
                    }

                } else {
                    System.out.println("Invalid input! Make sure the selected nodes are both in bounds and on traversable nodes. Try again!");
                }

         //   } catch (Exception e) {
          //      System.out.println("Inputted value must be a valid integer. Try again! " + e.getMessage());
         //   }
        }

    }

    /**
     * Prints out the board by calling the visualizeBoard method in AStar
     * @param board The board to be printed as an AStar object
     */
    public static void printBoard(AStar board) {
        String[][] visualBoard = board.visualizeBoard();
        for (int n = 0; n < visualBoard.length; n++) {
            System.out.println(Arrays.toString(visualBoard[n]));
        }
    }

    /**
     * Combines inBounds and isLegal to check if the selected coordinate is a
     * valid selection
     *
     * @param b The AStar Object
     * @param startX The starting X coordinate inputted by the user as an int
     * @param startY The starting y coordinate inputted by the user as an int
     * @param endX The ending x coordinate inputted by the user as an int
     * @param endY The ending y coordinate inputted by the user as an int
     * @return Boolean value; True if the coordinates are valid, False if it is
     * not
     */
    public static boolean isValid(AStar b, int startX, int startY, int endX, int endY) {
        return inBounds(startX, startY) && inBounds(endX, endY)
                && isLegal(b, startX, startY) && isLegal(b, endX, endY);
    }

    /**
     * Checks to make sure that the selected coordinate is in bounds
     *
     * @param x The selected X coordinate as an int
     * @param y The selected Y coordinate as an int
     * @return Boolean value; True if it is in bounds, False if it is not
     */
    public static boolean inBounds(int x, int y) {
        return x >= 0 && x <= 15 && y >= 0 && y <= 15;
    }

    /**
     * Checks to make sure that the selected coordinate is traversable
     * @param b The current board as an AStar Object
     * @param x The selected X coordinate as an int
     * @param y The selected Y coordinate as an int
     * @return Boolean value; True if it is legal, False if it is not
     */
    public static boolean isLegal(AStar b, int x, int y) {
        Node node = b.getNode(x, y);
        return node.getType() == 0;
    }

}
