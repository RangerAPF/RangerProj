
import java.util.HashMap;
import java.util.Scanner;

// A class that recursively finds the best path in terms of score through a 2-Dimensional maze, the maze being inputted as a 2D array.
// The rules are that you must begin at the bottom left of the maze, the score beings at 0, you must end at the top right, can only move up and right, 
// must hop to dots and over any other kind of characters, and the maze must begin and end with a dot. 
// The characters besides dots are called gems, and when hopped over they affect the score. "*" doubles the score, "#" subtracts 5, and "$" adds 9. 
// In this version, the input should have the dimensions of the array on the first line, and then the 2D array can be submitted.
public class Gems {

    static int mapRowSize; // The number of rwos in the maze
    static int mapColumnSize;  // The number of columns in the maze
    static HashMap<String, Long> bestScores = new HashMap<>(); // The HashMap used to memoize the best scores of each coordinate during recursion.

    public static void main(String[] args) {

        // Example map: 
        // String[][] map = {
        //     {"*", ".", ".", "*", ".", "#", "."},
        //     {".", ".", "$", ".", "*", ".", "$"},
        //     {"$", ".", ".", "#", ".", ".", "."},
        //     {".", ".", "*", ".", "*", ".", "$"},
        //     {".", "$", ".", "#", ".", ".", "*"},
        //     {"*", ".", ".", ".", ".", ".", "#"},
        //     {".", "#", "#", ".", ".", "#", "#"}

        // };

        Scanner scan = new Scanner(System.in);
        // Scan in the size of the 2D array using the numbers given on the first line
        mapRowSize = scan.nextInt();
        mapColumnSize = scan.nextInt();
        scan.nextLine();
        String[][] maze = new String[mapRowSize][mapColumnSize];
        // Scan in the 2D array 
        for (int row = 0; row < mapRowSize; row++){
            // Iterate through the rows
            String[] array = new String[mapColumnSize];
            String s = scan.nextLine();
            array = s.split(" ");
            
            for (int column = 0; column < mapColumnSize; column++){
                // Iterate throught the columns
                maze[row][column] = array[column]; 
            }
        }

        scan.close();

        // Call the otpimalScoreFiner method to calculat the best possible score and then print it.
        Long bestScore = optimalScoreFinder(maze, 0, mapColumnSize - 1);
        System.out.println(bestScore);
    }

    // The recursive function that calculates the best possible score
    private static Long optimalScoreFinder(String[][] maze, int row, int column){

        // Create a String type key of the current coordinate of the maze for memoization
        String key = Integer.toString(row) + "-" + Integer.toString(column);
        // Check to see if the best score for this key/coordinate is already in the HashMap
        if (bestScores.containsKey(key)){
            // Return the score associated with the key/coordinate in the HashMap
            return bestScores.get(key);
        }

        // The actual recursion, which starts at the top right corner and calculates the best score for each square using the two previous to it in a path

        // Base case for when the score at the bottom left is reached in the recursion
        if (row == mapRowSize - 1 && column == 0){
            return 0L;
        }

        // Initialize the different scores. score1 is equal to the score found by moving down. score2 is equal to the score found from by moving left. bestScore
        // will be the best score out of the two in the end.
        Long score1 = -9999999999L;
        Long score2 = -999999999L;
        Long bestScore = -99999999999999999L;

        // The recurisve calls for calculating score1, which finds the value derived from the coordinate below the current one.

        // Checks to make sure that the index is not out of bounds if moving 1 step down in the maze.
        if (row < mapRowSize - 1){
            // Score found if the coordinate below in the maze contains a dot.
            String s1 = maze[row + 1][column];
            if (s1.equals(".")){
                // Calculates the score below recursively
                score1 = optimalScoreFinder(maze, row + 1, column); 
            } 

            // Checks to make sure that moving two steps down won't go out of bounds. This is necessary since gems are jumped over
            if (row < mapRowSize - 2){
                String s2 = maze[row + 2][column];
                // Checks to see if the coordinate below is a dollar sign gem
                if (s1.equals("$") && s2.equals(".")){
                    // Calculates the rest recursively
                    score1 = optimalScoreFinder(maze, row + 2, column) + 9;
                }
                // Checks to see if the coordinate below is a "#" gem
                if (s1.equals("#") && s2.equals(".")){
                    // Calculates the rest recursively
                    score1 = optimalScoreFinder(maze, row + 2, column) - 5;
                }
                // Checks to see if the coordinate below is a "*" gem
                if (s1.equals("*") && s2.equals(".")){
                    // Calculates the rest recursively
                    score1 = optimalScoreFinder(maze, row + 2, column) * 2;
                }
            }
        }

        // The recurisve calls for calculating score2, which finds the value derived from the coordinate to the left of the current one. This essentially
        // follows exactly the same process as the previous set of calls.
        if (column > 0){

            String s1 = maze[row][column - 1];
            if (s1.equals(".")){
                score2 = optimalScoreFinder(maze, row, column - 1); 
            }
            
            if (column > 1){
                String s2 = maze[row][column - 2];

                if (s1.equals("$") && s2.equals(".")){
                    score2 = optimalScoreFinder(maze, row, column - 2) + 9;
                }
                if (s1.equals("#") && s2.equals(".")){
                    score2 = optimalScoreFinder(maze, row, column - 2) - 5;
                }
                if (s1.equals("*") && s2.equals(".")){
                    score2 = optimalScoreFinder(maze, row, column - 2) * 2;
                }
            }
        }

        // Compares which score, the one derived from the coordinate below or the one derived from the coordiante to the left, is greater, and sets bestScore equal
        // to the greater of the two.
        if (score1 > score2){
             bestScore = score1;
        } else  bestScore = score2;

        // Stores the best value for this coordinate in the hHashMap, associating bestScore with the current key
        bestScores.put(key, bestScore);
        return bestScore;
    }
}
