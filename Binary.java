/**
 * Created by Nathan on 18/01/2016.
 */
public class Binary {

    private int dimension;

    public Binary(int dimension) {
        this.dimension = dimension;
    }

    public String getNext(String previousID) {
        String returnString = "";
        if (previousID == null) {
            for (int i = 0; i < dimension; i++) {
                returnString += "0";
            }
            return returnString;
        }
        else {
            returnString = previousID;
        }

        char[] binaryString = returnString.toCharArray();

        boolean carry = true;

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

        for (int j = 0; j < dimension; j++) {
            returnString += Character.toString(binaryString[j]);
        }

        return returnString;
    }

    public boolean hypercubeEdgeTest(String str1, String str2) {
        char[] arr1 = str1.toCharArray();
        char[] arr2 = str2.toCharArray();

        int numberDifferences = 0;

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

        if(str1.length() != str2.length() || str1.isEmpty() || str2.isEmpty()) {
            return false;
        }

        // Loop through the entire string
        for (int i = 0; i < str1.length(); i++) {
            // Checking that the strings are the same (!complement)
            if (!complement) {
                if (arr1[i] != arr2[i]) {
                    complement = true;
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

    public int binaryStringToInt(String binaryStirng) {
        char[] bits = binaryStirng.toCharArray();
        int returnNumber = 0;

        for(int i = dimension - 1; i >= 0; i--) {
            if(bits[i] == '1') {
                returnNumber += (int)Math.pow(2,(dimension - 1) - i);
            }
        }

        return returnNumber;
    }

}
