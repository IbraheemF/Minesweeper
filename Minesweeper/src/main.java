//Import Section
import java.util.Random;
import java.util.Scanner;

// written by Ibraheem Fannoun
public class main{
    public static void main(String[] args) {
        System.out.print("Select a mode:");
        System.out.println("EASY    MEDIUM    HARD");
        Scanner sc = new Scanner(System.in);
        String difficulty = sc.nextLine(); // user enters chosen difficulty in all caps
        System.out.println("Debug Mode?");
        System.out.println("ON   OFF");
        String debug = sc.nextLine(); // user selects debug mode or not
        int r;
        int c;
        int f;
        if (difficulty.equals("EASY")){ // if user picked easy, we give a board with proper dimensions and mine count
            r = 5;
            c = 5;
            f = 5;
        }
        else if (difficulty.equals("MEDIUM")){ //if user picked medium, we give a board with proper dimensions and mine count
            r = 9;
            c = 9;
            f = 12;
        }
        else{ //if user picked hard, we give a board with proper dimensions and mine count
            r = 20;
            c = 20;
            f = 40;
        }
        Minefield gameField = new Minefield(r,c,f); // intialize cell  array
        System.out.println("Enter Starting coordinates as integers (each on their own line, hit enter for each):");
        System.out.println("[y]");
        System.out.println("[x]");
        System.out.println(gameField);
        int startX = Integer.parseInt(sc.nextLine());
        int startY = Integer.parseInt(sc.nextLine());
        gameField.createMines(startX,startY,f); // creates mines on the board
        gameField.evaluateField(); // properly changes all cells status
        gameField.revealStartingArea(startX, startY); // reveals the status of the starting area the user chose
        System.out.println(gameField); // shows the board, with only things revealed
        if (debug.equals("ON")){ //shows the whole board if debug mode selected
            gameField.debug();
        }
        while(!gameField.gameOver()){
            System.out.println("Enter coordinates, and if this is a flag or not (each on their own line, hit enter for each): ");
            System.out.println("[y]");
            System.out.println("[x]");
            System.out.println("[1 for flag, 0 for not]");
            int curX = Integer.parseInt(sc.nextLine()); // takes in users y-coordinate
            int curY =Integer.parseInt(sc.nextLine()); // takes in users x-coordinate
            String flagCheck = sc.nextLine(); // checks if this is placing a flag at the coordinate
            boolean flagCheck2;
            if (flagCheck.equals("1")){
                flagCheck2 = true;
            }
            else{
                flagCheck2 = false;
            }
            gameField.guess(curX, curY, flagCheck2); // users guess is implements
            gameField.revealZeroes(curX, curY); // if we hit a zero we reveal zeros
            System.out.println(gameField); // show current field
            if (debug.equals("ON")){ // if debug mode, we show revealed field
                gameField.debug();
            }
        }
    }

    
}
