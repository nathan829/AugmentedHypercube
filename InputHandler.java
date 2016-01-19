/**
 * Created by Nathan on 18/01/2016.
 */
import java.util.Scanner;

public class InputHandler {

    Scanner scanner;

    public InputHandler() {
        scanner = new Scanner(System.in);
    }

    public int getMenuInt() {
        boolean valid = false;
        int value = 0;
        while (!valid) {
            try {
                if (scanner.hasNextInt()) {
                    value = scanner.nextInt();
                    valid = true;
                    if (value < 1 || value > 5) {
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
                        if (value < 1 || value > 4) {
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

    public void resetScanner() {
        scanner.nextLine();
    }

}
