/**
 * Created by Nathan on 18/01/2016.
 */
import java.util.Scanner;

public class InputHandler {

    Scanner scanner;

    public InputHandler() {
        scanner = new Scanner(System.in);
    }

    public int getMenuInt(int max) {
        boolean valid = false;
        int value = 0;
        while (!valid) {
            try {
                if (scanner.hasNextInt()) {
                    value = scanner.nextInt();
                    valid = true;
                    if (value < 1 || value > max) {
                        valid = false;
                        throw new Exception("ERROR: Please enter one of the menu options");
                    }
                }
                else {
                    throw new Exception("ERROR: Please enter a whole number");
                }
            }
            catch (Exception error) {
                System.out.println(error.getMessage());
                resetScanner();
            }
        }

        return value;
    }

    public int getDimensionInt(boolean lexOrdering) {
        boolean valid = false;
        int value = 0;
        while (!valid) {
            try {
                if (scanner.hasNextInt()) {
                    value = scanner.nextInt();
                    valid = true;
                    if (lexOrdering) {
                        if (value < 1 || value > 30) {
                            valid = false;
                            throw new Exception("ERROR: Please enter dimension between 1 and 30");
                        }
                    }
                    else {
                        if (value < 1 || value > 15) {
                            valid = false;
                            throw new Exception("ERROR: Please enter dimension between 1 and 4");
                        }
                    }
                }
                else {
                    throw new Exception("ERROR: Please enter a whole number");
                }
            }
            catch (Exception error) {
                System.out.println(error.getMessage());
                resetScanner();
            }
        }

        return value;
    }

    public int getInt() {
        boolean valid = false;
        int value = 0;
        while (!valid) {
            try {
                if (scanner.hasNextInt()) {
                    value = scanner.nextInt();
                }
                else {
                    throw new Exception("ERROR: Please enter a whole number");
                }
                valid = true;
            }
            catch (Exception error) {
                System.out.println(error.getMessage());
                resetScanner();
            }
        }

        return value;
    }

    public int getColumns(int dimension) {
        boolean valid = false;
        int value = 0;
        double temp;
        while (!valid) {
            try {
                if (scanner.hasNextInt()) {
                    value = scanner.nextInt();
                }
                else {
                    throw new Exception("ERROR: Please enter a whole number");
                }
                temp = value;
                for (int i = 0; i < dimension; i++) {
                    temp /= 2;
                    if (temp == 1.0) {
                        break;
                    }
                }
                if (((int)temp != 1) || (value > (int)Math.pow(2, dimension))) {
                    throw new Exception("ERROR: Please enter a valid column size");
                }
                valid = true;
            }
            catch (Exception error) {
                System.out.println(error.getMessage());
                resetScanner();
            }
        }

        return value;
    }

    public char getYesNo() {
        boolean valid = false;
        String value = "";
        char returnValue;
        while(!valid) {
            try {
                if(scanner.hasNext()) {
                    value = scanner.next();
                }
                else {
                    throw new Exception("ERROR: Please enter a character");
                }

                if(value.equals("y") || value.equals("n")) {
                    valid = true;
                }
                else {
                    throw new Exception("ERROR: Please enter y or n");
                }
            }
            catch(Exception error) {
                System.out.println(error.getMessage());
                resetScanner();
            }
        }

        returnValue = value.charAt(0);

        return returnValue;
    }

    public void resetScanner() {
        scanner.nextLine();
    }

}
