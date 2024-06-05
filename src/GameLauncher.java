import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

class GameLauncher {
    public static void main(String[] args) {
        Game g = new Game();
        g.startGame();
    }
}
class Game {
    private ArrayList<Ship> ships = new ArrayList<Ship>();
    private HelperUtil helper = new HelperUtil();
    private int numOfTries = 0;

    private void setUpGame() {
        helper.generateShipCells();
        helper.setShipCells();
        for (int i=0; i<3; i++) {
            ships.add(new Ship("ship " + (i + 1), helper.getShipCoord(i)));
        }
    }
    public void startGame() {
        setUpGame();
        String guess;
        Scanner scanner = new Scanner(System.in);
        while (!ships.isEmpty()) {
            System.out.print("Enter a guess [a1, g7]: ");
            guess = scanner.nextLine();
            checkUserInput(guess);
        }
        scanner.close();
        System.out.println("you took " + numOfTries + " try");
    }
    private void checkUserInput(String guess) {
        numOfTries++;
        int result;
        for (Ship ship : ships) {
            result = ship.check(guess);
            if (result == Ship.KILL) {
                ships.remove(ship);
                System.out.println(ship.getName() + " has SUNK !!");
                return;
            }
            if (result == Ship.HIT) {
                System.out.println("a HIT to " + ship.getName());
                return;
            }
        }
        System.out.println("a miss");
    }
}
class Ship {
    private ArrayList<String> shipCells;
    private String name;
    public final static int MISS = 0;
    public final static int HIT = 1;
    public final static int KILL = 2;

    public Ship(String name, ArrayList<String> shipCells) {
        this.name = name;
        this.shipCells = shipCells;
    }

    public void setShipCells(ArrayList<String> shipCells) {
        this.shipCells = shipCells;
    }
    public ArrayList<String> getshipCells() {
        return shipCells;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public int check(String guess) {
        int index = shipCells.indexOf(guess);
        if (index >= 0) {
            shipCells.remove(index);
            if (shipCells.isEmpty()) {
                return Ship.KILL;
            }
            else {
                return Ship.HIT;
            }
        }
        else {
            return Ship.MISS;
        }
    }
}
class HelperUtil {
    private int[][] nums = new int[7][7];
    private ArrayList<String> ship1 = new ArrayList<String>();
    private ArrayList<String> ship2 = new ArrayList<String>();
    private ArrayList<String> ship3 = new ArrayList<String>();

    ArrayList<String> getShipCoord(int index) {
        if (index == 0) {
            return ship1;
        }
        else if (index == 1) {
            return ship2;
        }
        else if (index == 2) {
            return ship3;
        }
        else {
            return null;
        }
    }
    void generateShipCells() {
        Random random = new Random();
        int i = random.nextInt(5);
        int j = random.nextInt(5);
        boolean horizontallyAvailable;
        boolean verticallyAvailable;
        nums[i][j] = 1;
        if (Math.random() >= 0.5) { // randomly decide if the ship is horizontal or vertical
            nums[i + 1][j] = 2;
            nums[i + 2][j] = 3;
        }
        else {
            nums[i][j + 1] = 2;
            nums[i][j + 2] = 3;
        }

        /* first ship indeces have the values (1,2,3)
         * the second and third ships will have the indices
         * (4,5,6) (7,8,9) respectively
         */
        for (int c=1; c<3; c++) { // loop 2 times for each remaining 2 ships
            int initialIndexValue = 3*c + 1; // = 4 for ship2 and = 7 for ship3
            horizontallyAvailable = false;
            verticallyAvailable = false;
            while (!(horizontallyAvailable || verticallyAvailable)) {
                i = random.nextInt(5);
                j = random.nextInt(5);
    
                horizontallyAvailable = (nums[i][j] == 0) &&
                                        (nums[i + 1][j] == 0) &&
                                        (nums[i + 2][j] == 0);
                
                verticallyAvailable = (nums[i][j] == 0) &&
                                      (nums[i][j + 1] == 0) &&
                                      (nums[i][j + 2] == 0);
            }
                
            nums[i][j] = initialIndexValue;
            if (horizontallyAvailable && verticallyAvailable) {
                if (Math.random() >= 0.5) {
                    nums[i + 1][j] = initialIndexValue + 1;
                    nums[i + 2][j] = initialIndexValue + 2;
                }
                else {
                    nums[i][j + 1] = initialIndexValue + 1;
                    nums[i][j + 2] = initialIndexValue + 2;  
                }
            }

            else if (horizontallyAvailable) {
                nums[i + 1][j] = initialIndexValue + 1;
                nums[i + 2][j] = initialIndexValue + 2;
            }
                
            else {
                nums[i][j + 1] = initialIndexValue + 1;
                nums[i][j + 2] = initialIndexValue + 2;
            }
        }
    }
    void setShipCells() {
        for (int i=0; i<7; i++) {
            for (int j=0; j<7; j++) {
                if (nums[i][j] == 1 || nums[i][j] == 2 || nums[i][j] == 3) {
                    ship1.add(convertIndexToLocation(i, j));
                }
                else if (nums[i][j] == 4 || nums[i][j] == 5 || nums[i][j] == 6) {
                    ship2.add(convertIndexToLocation(i, j));
                }
                else if (nums[i][j] == 7 || nums[i][j] == 8 || nums[i][j] == 9) {
                    ship3.add(convertIndexToLocation(i, j));
                }
            }
        }
    }
    String convertIndexToLocation(int i, int j) {
        return (char)(i + 97) + Integer.toString(j + 1);
    }
}