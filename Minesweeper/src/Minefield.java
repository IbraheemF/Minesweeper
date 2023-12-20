
import java.util.Random;
// written by Ibraheem Fannoun (fanno010)
public class Minefield {
    /**
    Global Section
    */
    public static final String ANSI_YELLOW_BRIGHT = "\u001B[33;1m";
    public static final String ANSI_BLUE_BRIGHT = "\u001b[34;1m";
    public static final String ANSI_BLUE = "\u001b[34m";
    public static final String ANSI_RED_BRIGHT = "\u001b[31;1m";
    public static final String ANSI_RED = "\u001b[31m";
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_PURPLE = "\u001b[35m";
    public static final String ANSI_CYAN = "\u001b[36m";
    public static final String ANSI_GREY_BACKGROUND = "\u001b[0m";

    /* 
     * Class Variable Section
     * 
    */
    private Cell[][] field; // array on which the game will be played
    private int flags;
    private boolean gameOver1 = false; // boolean that keep track if the game should end
    
    /**
     * Minefield
     * 
     * Build a 2-d Cell array representing your minefield.
     * Constructor
     * @param rows       Number of rows.
     * @param columns    Number of columns.
     * @param flags      Number of flags, should be equal to mines
     */
    public Minefield(int rows, int columns, int flags) {
        this.field = new Cell[rows][columns]; // intializing game board
        this.flags = flags;
    }

    /**
    * getSurrounding
    *
     * @function:
    * evaluatefield helper function, returns array of cells surrounding an indivual cell.
    *
    */
    public Cell[] getSurrounding(int x, int y){ // helper method, returns an array of all adajcent cells to the current cell we are working with
        if(x==0 && y==0){ // top left corner
            Cell[] surrounding = {field[0][1],field[1][0],field[1][1]};
            return surrounding;
        }
        else if (x==0 && y == field[0].length-1){ // top right corner
            Cell[] surrounding = {field[x][y-1],field[x+1][y-1],field[x+1][y]};
            return surrounding;
        }
        else if (x== field.length-1 && y==0){ // bottom right corner
            Cell[] surrounding = {field[x-1][y],field[x-1][y+1],field[x][y+1]};
            return surrounding;
        }
        else if (x== field.length-1 && y == field[0].length-1){ // bottom left corner
            Cell[] surrounding = {field[x][y-1],field[x-1][y-1],field[x-1][y]};
            return surrounding;
        }
        else if (x == 0){ //handles edges
            Cell[] surrounding = {field[x][y+1],field[x][y-1],field[x+1][y-1],field[x+1][y+1],field[x+1][y]};
            return surrounding;
        }
        else if (y == 0){ //handles edges
            Cell[] surrounding = {field[x-1][y],field[x+1][y],field[x][y+1],field[x-1][y+1],field[x+1][y+1]};
            return surrounding;
        }
        else if(y == field[0].length-1){ //handles edges
            Cell[] surrounding = {field[x-1][y],field[x+1][y],field[x-1][y-1],field[x+1][y-1],field[x][y-1]};
            return surrounding;
        }
        else if(x == field.length-1){ //handles edges
            Cell[] surrounding = {field[x][y+1],field[x][y-1],field[x-1][y],field[x-1][y+1],field[x-1][y-1]};
            return surrounding;
        }
        else{ // handles cells if they aren't a corner or an edge
            Cell[] surrounding = {field[x-1][y],field[x+1][y],field[x][y-1],field[x][y+1],field[x+1][y+1],field[x-1][y-1],field[x-1][y+1],field[x+1][y-1]};
            return surrounding;
        }
    }
    /**
     * evaluateField
     *
     *
     * @function:
     * Evaluate entire array.
     * When a mine is found checks the surrounding adjacent tiles. If another mine is found during this check, increments adjacent cells status by 1.
     *
     */
    public void evaluateField() { // sets all individual cells to their proper value based on the mines on the field
        for (int i = 0; i< field.length; i++){
            for (int j =0; j< field[0].length; j++){ // loop through all cells in array
                if(field[i][j].getStatus().equals("M")){ // if we find a mine we increment surronding cells
                    Cell[] checks = this.getSurrounding(i,j);
                    for (int k=0; k< checks.length; k++){ // loop that increments all surrounding non-min cells by 1
                        if (checks[k].getStatus().equals("M")){
                            continue;
                        }
                        else{
                            String incrementNum = String.valueOf(Integer.parseInt(checks[k].getStatus())+1); // calculates what current value of non-mine cell should be
                            checks[k].setStatus(incrementNum);
                        }
                    }
                }
            }
        }
    }

    /**
     * createMines
     * 
     * Randomly generates coordinates for possible mine locations.
     * If the coordinate has not already been generated and is not equal to the starting cell sets the cell to be a mine.
     * 
     * @param x       Start x, avoid placing on this square.
     * @param y        Start y, avoid placing on this square.
     * @param mines      Number of mines to place.
     */
    public void createMines(int x, int y, int mines) { // first thing done after the creation of the gamefield is setting the mines
        for (int i = 0; i< field.length; i++) {
            for (int j = 0; j < field[0].length; j++) { //loop through all of our cells in the 2d array
                field[i][j] = new Cell(false,"0"); // intialize every cell to 0
            }
        }
        Random r = new Random();
        while (mines != 0){ // loops until the proper amount of mines are placed at random on the field
            int mine_X = r.nextInt(21);
            int mine_Y = r.nextInt(21); // randomize mine coordinates
            if (mine_X != x || mine_Y != y){
                if (mine_X <= field.length-1&& mine_Y <=field[0].length-1){ // if mine is in bound and not the starting cell
                    if(!field[mine_X][mine_Y].getStatus().equals("M")){
                        field[mine_X][mine_Y] = new Cell(false, "M"); // set the cell to a mine
                        mines--; //decrement mine counter variable
                    }
                }
            }
        }
    }

    /**
     * guess
     * 
     * Checks if the guessed cell is inbounds.
     * Either places a flag on the designated cell if the flag boolean is true or clears it.
     * If the cell has is 0 calls the revealZeroes() method or if the cell has a mine ends the game.
     * At the end reveals the cell to the user.
     * 
     * 
     * @param x       The x value the user entered.
     * @param y       The y value the user entered.
     * @param flag    A boolean value that allows the user to place a flag on the corresponding square.
     * @return boolean Returns false if guess did not hit mine or if flag was placed, true if mine found.
     */
    public boolean guess(int x, int y, boolean flag) {
        if (field[x][y].getStatus().equals("M")&&!flag){ // if we hit a mine, reveal it and end the game by changing the gameover variable
            field[x][y].setRevealed(true);
            this.gameOver1 = true;
            return true;
        }
        else if(flag){ // places a flag at a chosen coordinate
            this.flags--;
            field[x][y].setStatus("F");
            field[x][y].setRevealed(true);
        }
        else { //reveals chosen cell if its not a mine and not being flagged
            field[x][y].setRevealed(true);
            if (field[x][y].getStatus().equals("0")){ // if we hit a zero, reveal all connected zeros
                this.revealZeroes(x,y);
            }
        }
        return false;
    }

    /**
     * gameOver
     *
     * 
     * @return boolean Returns false if game is not over and squares have yet to be revealed, otheriwse return true.
     */
    public boolean gameOver() {
        if (gameOver1){ // handles hitting a mine
            System.out.println("You hit a mine!");
            System.out.println("Game Over!");
            return gameOver1;
        }
        else{ // checks if every non-mine cell has been revealed, if so, player wins
            for (int i=0; i<field.length;i++){
                for(int j=0; j<field[0].length;j++){
                    if(field[i][j].getStatus().equals("M")){
                        continue;
                    }
                    else{
                        if(!field[i][j].getRevealed()){ // if we hit a single non-mine square that is unrevealed, we know the game isn't 0ver yet
                            return false;
                        }
                    }
                }
            }
            System.out.println("You won!");
            gameOver1 = true;
            return gameOver1;
        }
    }

    /**
     * Reveals the cells that contain zeroes that surround the inputted cell.
     * Continue revealing 0-cells in every direction until no more 0-cells are found in any direction.
     * Utilizes a STACK to accomplish this.
     *
     * This method should follow the psuedocode given in the lab writeup.
     * Why might a stack be useful here rather than a queue?
     *
     * @param x      The x value the user entered.
     * @param y      The y value the user entered.
     */
    public void revealZeroes(int x, int y) {
        int[] starting = {x,y};
        Stack1Gen<int[]> stack = new Stack1Gen<>(); // intializing stack
        stack.push(starting);
        while(!stack.isEmpty()){ //loop until we have no more surrounding zeros pushed to the stack
            int[] cur = stack.pop();
            field[cur[0]][cur[1]].setRevealed(true); // reveal the current cell we are working on
            if (((cur[0] >= 0 && cur[0] <= field.length - 1) && (cur[1] - 1 >= 0 && cur[1] - 1 <= field[0].length - 1) && !field[cur[0]][cur[1] - 1].getRevealed())&& field[cur[0]][cur[1] - 1].getStatus().equals("0")){
                stack.push(new int[]{cur[0], cur[1] - 1}); // if cell is in bounds and is a zero, push to stack
            }
            if (((cur[0] >= 0 && cur[0] <= field.length - 1) && (cur[1] + 1 >= 0 && cur[1] + 1 <= field[0].length - 1) && !field[cur[0]][cur[1] + 1].getRevealed())&& (field[cur[0]][cur[1] + 1].getStatus().equals("0"))) {
                stack.push(new int[]{cur[0], cur[1] + 1}); // if cell is in bounds and is a zero, push to stack
            }
            if (((cur[0] - 1 >= 0 && cur[0] - 1 <= field.length - 1) && (cur[1] >= 0 && cur[1] <= field[0].length - 1) && !field[cur[0] - 1][cur[1]].getRevealed())&& (field[cur[0]-1][cur[1]].getStatus().equals("0"))) {
                stack.push(new int[]{cur[0] - 1, cur[1]}); // if cell is in bounds and is a zero, push to stack
            }
            if (((cur[0] + 1 >= 0 && cur[0] + 1 <= field.length - 1) && (cur[1] >= 0 && cur[1] <= field[0].length - 1) && !field[cur[0] + 1][cur[1]].getRevealed())&& (field[cur[0]+1][cur[1]].getStatus().equals("0"))){
                stack.push(new int[]{cur[0] + 1, cur[1]}); // if cell is in bounds and is a zero, push to stack
            }
        }
    }

    /**
     * revealStartingArea
     * On the starting move only reveals the neighboring cells of the inital cell and continue revealing the surrounding concealed cells until a mine is found.
     * Utilizes a QUEUE to accomplish this.
     *
     *
     * @param x     The x value the user entered.
     * @param y     The y value the user entered.
     */
    public void revealStartingArea(int x, int y) {
        int[] starting = {x,y};
        Q1Gen<int[]> queue = new Q1Gen<>(); //intialize queue
        queue.add(starting);
        while(queue.length()!=0){ // loop until we have nothing in the queue or we hit a mine
            int[] cur = queue.remove();
            field[cur[0]][cur[1]].setRevealed(true); //reveal current cell were working on
            if(field[cur[0]][cur[1]].getStatus().equals("M")){ // if we hit a mine, end loop
                break;
            }
            if (((cur[0] >= 0 && cur[0] <= field.length - 1) && (cur[1] - 1 >= 0 && cur[1] - 1 <= field[0].length - 1) && !field[cur[0]][cur[1] - 1].getRevealed())){
                queue.add(new int[]{cur[0], cur[1] - 1}); // if the cell is inbounds and not revealed already, add to the queue
            }
            if (((cur[0] >= 0 && cur[0] <= field.length - 1) && (cur[1] + 1 >= 0 && cur[1] + 1 <= field[0].length - 1) && !field[cur[0]][cur[1] + 1].getRevealed())) {
                queue.add(new int[]{cur[0], cur[1] + 1}); // if the cell is inbounds and not revealed already, add to the queue
            }
            if (((cur[0] - 1 >= 0 && cur[0] - 1 <= field.length - 1) && (cur[1] >= 0 && cur[1] <= field[0].length - 1) && !field[cur[0] - 1][cur[1]].getRevealed())) {
                queue.add(new int[]{cur[0] - 1, cur[1]}); // if the cell is inbounds and not revealed already, add to the queue
            }
            if (((cur[0] + 1 >= 0 && cur[0] + 1 <= field.length - 1) && (cur[1] >= 0 && cur[1] <= field[0].length - 1) && !field[cur[0] + 1][cur[1]].getRevealed())) {
                queue.add(new int[]{cur[0] + 1, cur[1]}); // if the cell is inbounds and not revealed already, add to the queue
            }
        }
    }

    /**
     * debug
     *
     * @function This method prints the entire minefield, regardless if the user has guessed a square.
     * *This method prints out when debug mode has been selected.
     */
    public void debug() {
        StringBuilder out = new StringBuilder();
        out.append("  ");
        for(int i = 0; i < field.length; i++){
            out.append(" ");
            if (i <10){
                out.append(" ");
            }
            out.append(i); // numbers the top of our board to better see coordinates
        }
        out.append('\n');
        for(int i = 0; i < field.length; i++) {
            if (i <10){
                out.append(" ");
            }
            out.append(i); // numbers side of our board to better see coordinates
            for(int j = 0; j < field[0].length; j++) { // adds all cells with according ANSI color to our output string
                out.append("  ");
                if (field[i][j] == null){
                    out.append("-");
                }
                else if (field[i][j].getStatus().equals("M")) {
                    out.append(ANSI_RED_BRIGHT);
                    out.append(field[i][j].getStatus());
                    out.append(ANSI_GREY_BACKGROUND);
                }
                else if(field[i][j].getStatus().equals("1")){
                    out.append(ANSI_BLUE_BRIGHT);
                    out.append(field[i][j].getStatus());
                    out.append(ANSI_GREY_BACKGROUND);
                }
                else if(field[i][j].getStatus().equals("2")){
                    out.append(ANSI_GREEN);
                    out.append(field[i][j].getStatus());
                    out.append(ANSI_GREY_BACKGROUND);
                }
                else if(field[i][j].getStatus().equals("3")){
                    out.append(ANSI_RED);
                    out.append(field[i][j].getStatus());
                    out.append(ANSI_GREY_BACKGROUND);
                }
                else if(field[i][j].getStatus().equals("4")){
                    out.append(ANSI_YELLOW_BRIGHT);
                    out.append(field[i][j].getStatus());
                    out.append(ANSI_GREY_BACKGROUND);
                }
                else if(field[i][j].getStatus().equals("5")){
                    out.append(ANSI_PURPLE);
                    out.append(field[i][j].getStatus());
                    out.append(ANSI_GREY_BACKGROUND);
                }
                else if(field[i][j].getStatus().equals("6")){
                    out.append(ANSI_CYAN);
                    out.append(field[i][j].getStatus());
                    out.append(ANSI_GREY_BACKGROUND);
                }
                else if(field[i][j].getStatus().equals("7")){
                    out.append(ANSI_RED);
                    out.append(field[i][j].getStatus());
                    out.append(ANSI_GREY_BACKGROUND);
                }
                else if(field[i][j].getStatus().equals("8")){
                    out.append(ANSI_BLUE);
                    out.append(field[i][j].getStatus());
                    out.append(ANSI_GREY_BACKGROUND);
                }
                else if(field[i][j].getStatus().equals("F")){
                    out.append(ANSI_RED_BRIGHT);
                    out.append(field[i][j].getStatus());
                    out.append(ANSI_GREY_BACKGROUND);
                }
                else{
                    out.append(field[i][j].getStatus());
                }
            }
            out.append("\n");
        }
        System.out.println(out);
    }

    /**
     * toString
     *
     * @return String The string that is returned only has the squares that has been revealed to the user or that the user has guessed.
     */
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("  ");
        for(int i = 0; i < field.length; i++){
            out.append(" ");
            if (i <10){
                out.append(" ");
            }
            out.append(i); // numbers the top of the board
        }
        out.append('\n');
        for(int i = 0; i < field.length; i++) {
            if (i <10){
                out.append(" ");
            }
            out.append(i); // numbers the side of the board
            for(int j = 0; j < field[0].length; j++) { //  adds all cells with according ANSI color to our output string
                out.append("  ");
                if (field[i][j] == null|| !field[i][j].getRevealed()){ // if a cell isnt revealed, we add a "-" in its place
                    out.append("-");
                }
                else if (field[i][j].getStatus().equals("M")) {
                    out.append(ANSI_RED_BRIGHT);
                    out.append(field[i][j].getStatus());
                    out.append(ANSI_GREY_BACKGROUND);
                }
                else if(field[i][j].getStatus().equals("1")){
                    out.append(ANSI_BLUE_BRIGHT);
                    out.append(field[i][j].getStatus());
                    out.append(ANSI_GREY_BACKGROUND);
                }
                else if(field[i][j].getStatus().equals("2")){
                    out.append(ANSI_GREEN);
                    out.append(field[i][j].getStatus());
                    out.append(ANSI_GREY_BACKGROUND);
                }
                else if(field[i][j].getStatus().equals("3")){
                    out.append(ANSI_RED);
                    out.append(field[i][j].getStatus());
                    out.append(ANSI_GREY_BACKGROUND);
                }
                else if(field[i][j].getStatus().equals("4")){
                    out.append(ANSI_YELLOW_BRIGHT);
                    out.append(field[i][j].getStatus());
                    out.append(ANSI_GREY_BACKGROUND);
                }
                else if(field[i][j].getStatus().equals("5")){
                    out.append(ANSI_PURPLE);
                    out.append(field[i][j].getStatus());
                    out.append(ANSI_GREY_BACKGROUND);
                }
                else if(field[i][j].getStatus().equals("6")){
                    out.append(ANSI_CYAN);
                    out.append(field[i][j].getStatus());
                    out.append(ANSI_GREY_BACKGROUND);
                }
                else if(field[i][j].getStatus().equals("7")){
                    out.append(ANSI_RED);
                    out.append(field[i][j].getStatus());
                    out.append(ANSI_GREY_BACKGROUND);
                }
                else if(field[i][j].getStatus().equals("8")){
                    out.append(ANSI_BLUE);
                    out.append(field[i][j].getStatus());
                    out.append(ANSI_GREY_BACKGROUND);
                }
                else if(field[i][j].getStatus().equals("F")){
                    out.append(ANSI_RED_BRIGHT);
                    out.append(field[i][j].getStatus());
                    out.append(ANSI_GREY_BACKGROUND);
                }
                else{
                    out.append(field[i][j].getStatus());
                }
            }
            out.append("\n");
        }
        return out.toString();
    }

}
