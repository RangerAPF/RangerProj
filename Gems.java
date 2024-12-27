import java.util.HashMap;
import java.util.Scanner;

public class Gems {

    static int mapRowSize;
    static int mapColumnSize; 
    static HashMap<String, Long> bestScores = new HashMap<>();

    public static void main(String[] args) {
        
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

        mapRowSize = scan.nextInt();
        mapColumnSize = scan.nextInt();
        scan.nextLine();

        String[][] maze = new String[mapRowSize][mapColumnSize];

        for (int row = 0; row < mapRowSize; row++){
            String[] array = new String[mapColumnSize];
            String s = scan.nextLine();
            array = s.split(" ");

            for (int column = 0; column < mapColumnSize; column++){
                maze[row][column] = array[column]; 
            }
        }

        scan.close();

        // for (int row = 0; row < mapRowSize; row++){
        //     for (int column = 0; column < mapColumnSize; column++){
        //         System.out.print(maze[row][column]); 
        //     }
        //     System.out.println("");
        // }
        
        Long bestScore = optimalScoreFinder(maze, 0, mapColumnSize - 1);
        System.out.println(bestScore);
    }

    private static Long optimalScoreFinder(String[][] maze, int row, int column){

        String key = Integer.toString(row) + "-" + Integer.toString(column);
        if (bestScores.containsKey(key)){
            return bestScores.get(key);
        }
        // Base case
        if (row == mapRowSize - 1 && column == 0){
            return 0L;
        }
        
        Long score1 = -9999999999L;
        Long score2 = -999999999L;
        Long bestScore = -99999999999999999L;

        // First set of options
        if (row < mapRowSize - 1){
            String s1 = maze[row + 1][column];
            if (s1.equals(".")){
                score1 = optimalScoreFinder(maze, row + 1, column); 
            } 

            if (row < mapRowSize - 2){

                String s2 = maze[row + 2][column];

                if (s1.equals("$") && s2.equals(".")){
                    score1 = optimalScoreFinder(maze, row + 2, column) + 9;
                }
                if (s1.equals("#") && s2.equals(".")){
                    score1 = optimalScoreFinder(maze, row + 2, column) - 5;
                }
                if (s1.equals("*") && s2.equals(".")){
                    score1 = optimalScoreFinder(maze, row + 2, column) * 2;
                }
            }
        }
        
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

        if (score1 > score2){
             bestScore = score1;
        } else  bestScore = score2;
        
        bestScores.put(key, bestScore);
        return bestScore;
    }
}