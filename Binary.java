/**
 * Created by Nathan on 18/01/2016.
 */
public class Binary {
    // dimension passed by EdgeConstructor
    private int dimension;

    public Binary(int dimension) {
        this.dimension = dimension;
    }

    public String getNext(String previousID) {
        String returnString = "";
        // makes sure the first element will be a string full of 0's
        if (previousID == null) {
            for (int i = 0; i < dimension; i++) {
                returnString += "0";
            }
            return returnString;
        }
        else {
            // will add 1 to the previous string
            returnString = previousID;
        }

        // breaks up the string into a character array, easier for bitwise comparisons
        char[] binaryString = returnString.toCharArray();

        // assumes an initial carry from first element 000...0
        boolean carry = true;

        // algorithm for binary addition. Adds 1 to the previous string and then return result
        for (int k = dimension - 1; k >= 0; k--) {
            if (binaryString[k] == '0') {
                if (carry) {
                    binaryString[k] = '1';
                }
                else {
                    binaryString[k] = '0';
                }
                carry = false;
                break;
            }
            else {
                if (carry) {
                    binaryString[k] = '0';
                    carry = true;
                }
                else {
                    binaryString[k] = '1';
                    carry = false;
                    break;
                }
            }
        }

        returnString = "";

        // builds a string out of the character array
        for (int j = 0; j < dimension; j++) {
            returnString += Character.toString(binaryString[j]);
        }

        return returnString;
    }

    public boolean hypercubeEdgeTest(String str1, String str2) {
        char[] arr1 = str1.toCharArray();
        char[] arr2 = str2.toCharArray();

        // will count the number of difference in bits between str1 and str2
        int numberDifferences = 0;

        // obvious tests for non-equal strings
        if(str1.length() != str2.length() || str1.isEmpty() || str2.isEmpty()) {
            return false;
        }
        // Loop through the entire array to check if there are any differences
        // will return false if we find more than one difference
        for (int i = 0; i < str1.length(); i++) {
            if (arr1[i] != arr2[i]) {
                numberDifferences++;
                if (numberDifferences > 1) {
                    return false;
                }
            }
        }
        // Return false if the numberDifferences is 0 (meaning there are not any changes,
        // hence the 2 strings do not differ by 1)
        return !(numberDifferences == 0);
    }

    public boolean complementEdgeTest(String str1, String str2) {
        char[] arr1 = str1.toCharArray();
        char[] arr2 = str2.toCharArray();

        boolean complement = false;

        // obvious test for non-equal strings
        if(str1.length() != str2.length() || str1.isEmpty() || str2.isEmpty()) {
            return false;
        }

        // from left to right, when the bits become complement they must stay complement til the end
        // of the string
        for (int i = 0; i < str1.length(); i++) {
            // Checking that the strings are the same (!complement)
            if (!complement) {
                if (arr1[i] != arr2[i]) {
                    complement = true;
                    if ((i == str1.length() - 1) && (arr1[i-1] == arr2[i-1])) {
                        return false;
                    }
                }
            }
            else {
                if (arr1[i] == arr2[i]) {
                    return false;
                }
            }
        }

        return true;
    }

    public int binaryStringToInt(String binaryString) {
        char[] bits = binaryString.toCharArray();
        int returnNumber = 0;

        // by the definition of binary expressed as an integer, adding powers of 2 when
        // the respective bit is a 1
        for(int i = dimension - 1; i >= 0; i--) {
            if(bits[i] == '1') {
                returnNumber += (int)Math.pow(2,(dimension - 1) - i);
            }
        }

        return returnNumber;
    }
}
