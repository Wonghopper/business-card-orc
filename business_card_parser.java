/*
 * This program is for the Asymmetrik coding challenge Business Card OCR. The 
 * program takes the text of a business card as an input and outputs the name, 
 * phone number, and email address of the owner of the business card.
 * Date Created: 9/21/19
 * Last Updated: 9/23/19
 * Last Update: Added name dictionary to name parser
 * TODO: Testing
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

interface ContactInfo {
    String getName();
    String getPhoneNumebr();
    String getEmailAddress();
}

/*
 * This class defines the Contact object. The object stores the information 
 * parsed from a business card. The class also defines the methods that parse 
 * the business card text for information.
 */
class Contact implements ContactInfo {
    String name = "Default Name";
    String phoneNumber = "Default Number";
    String emailAddress = "Default Email";

    Contact(String document) {
        name = parseName(document);
        phoneNumber = parseNumber(document);
        emailAddress = parseEmail(document);
    }

    public String parseName(String text) {
        // Get possible names using regex
        // Create regex to pattern match names
        String regex = "([A-Za-z])+[,\\.]?[ \\t]?([A-Za-z])+"
                     + "[,\\.]?[ \\t]?([A-Za-z])*";
        Pattern pattern = Pattern.compile(regex);

        // Create matcher object
        Matcher matcher = pattern.matcher(text);
        /* Store possible names
         * First element of each array is entire potential name
         * Second, third, fourth elements are first, second, third strings in 
         * potential names
         */
        ArrayList<String> possNames = new ArrayList<String>(1);
        while (matcher.find()) {
            possNames.add(matcher.group(0));
        }
        /* Store first name found in case no potential name is in the name 
         * dictionary
         */
        String possNames1 = possNames.get(0);

        /* Create array of booleans to track which potential name is found in 
         * the name dictionary
         */
        boolean[] dicNames = new boolean[possNames.size()];
        for (int i = 0; i < dicNames.length; i ++) {
            dicNames[i] = false;
        }

        // Create file and scanner object to read the names dictionary file
        File file = new File("names_dic.txt");
        try {
            Scanner scanner  = new Scanner(file);

            /* For every name in the dictionary, if that name is in the list 
             * potential names, mark the corresponding boolean in the array 
             * dicNames
             */
            while (scanner.hasNextLine()) {
                String dicName = scanner.nextLine();
                for (int i = 0; i < dicNames.length; i ++) {
                    if (possNames.get(i).contains(dicName)) {
                        dicNames[i] = true;
                    }
                }
            }
        } catch(Exception e) {
            System.out.println("ERROR: File Not Found");
        }

        /* Removes potential names if they were not contained in the name 
         * dictionary
         */
        for (int i = possNames.size() - 1; i > 0; i --) {
            if (!dicNames[i]) {
                possNames.remove(i);
            }
        }

        // Return only potential name remaining
        if (possNames.size() == 1) {
            return possNames.get(0);
        }
        /* If multiple potential names are found in dictionary, assume the 
         * local-part of email address contains part of the name
         */
        else if (possNames.size() > 1) {
            // Get the local-part of the email address
            String email = parseEmail(text);
            String localEmail = email.substring(0, email.indexOf('@'));

            /* Cross compare possible names with the local-part of the email 
             * address
             */
            for (int i = 0; i < possNames.size(); i ++) {
                for (int j = 0; j < (localEmail.length() - 3); j ++) {
                    /* Checks:
                     * 1. Possible name is not local part of email address
                     * 2. Possible name contains substring from local part of 
                     *    email address at least 3 characters long
                     */
                    if (!possNames.get(i).equals(localEmail) 
                        && possNames.get(i)
                        .contains(localEmail.substring(j, j + 3))) {
                        return possNames.get(i);
                    }
                }
            }
            // If email does not align with possible names, assume first name
            return possNames.get(0);
        }
        else {
            /* Case where no potential name is in dictionary, returns first 
             * possible name parsered
             */
            return possNames1;
        }
    }

    public String getName() {
        return name;
    }

    public String parseNumber(String text) {
        // Assuming North American Numbering Plan
        // Create regex to pattern match phone numbers
        /* Match Results:
         * 1: Number Description: Alphabetic characters prefixing phone number. 
         *    Used to match phone number vs fax, etc.
         * 2: Country calling code
         * 3: Area code
         * 4: First 3 digits of subscriber number
         * 5: Last 4 digits of subscriber number
         */
        String regex = "([A-Za-z]*[ \\t]?[A-Za-z]*)[ \\t:;#\\|]?[ \t]?" 
                     + "\\+?(1)?[ \\t]?[ \\t\\(\\-\\.]?(\\d{3})[ \\t\\)\\-\\.]?"
                     + "[ \\t]?(\\d{3})[ \\t\\-\\.]?(\\d{4})";
        Pattern pattern = Pattern.compile(regex);
        
        // Create matcher object
        Matcher matcher = pattern.matcher(text);

        // Store character string prefixing numebr
        ArrayList<String> numberDesc = new ArrayList<String>(1);
        // Stores numbers that match pattern (phone, fax, etc.)
        ArrayList<String> numbers = new ArrayList<String>(1);
        while (matcher.find()) {
            // Store number description
            numberDesc.add(matcher.group(1).toLowerCase());
            // If country calling code included in phone number 
            if (matcher.group(2) != null) {
                numbers.add(matcher.group(2) + matcher.group(3) 
                            + matcher.group(4) + matcher.group(5));
            }
            else {
                numbers.add(matcher.group(3) + matcher.group(4) 
                            + matcher.group(5));
            }
        }

        // If only one number listed on card, assume as phone number
        if (numbers.size() == 1) {
            return numbers.get(0);
        }
        else if (numbers.size() > 1) {
            /* If any number's corresponding number description contains "tel" 
             * or "pho", assume it is the phone number
             */
            for (int i = 0; i < numbers.size(); i ++) {
                if (numberDesc.get(i).contains("tel") 
                    || numberDesc.get(i).contains("pho")) {
                    return numbers.get(i);
                }
            }
            /* In case of no number description contains "tel" or "pho", assume 
             * first number is correct number
             */
            return numbers.get(0);
        }
        else {
            return "No Phone Number Found";
        }
    }

    public String getPhoneNumebr() {
        return phoneNumber;
    }

    public String parseEmail(String text) {
        // Create regex to pattern match emails
        String regex = "[A-Za-z\\d](\\.?[A-Za-z\\d\\-_])*" 
                     + "@[A-Za-z\\d](\\.?[A-Za-z\\d\\-]+)*";
        Pattern pattern = Pattern.compile(regex);

        // Create matcher object
        Matcher matcher = pattern.matcher(text);

        // Return first match as email address
        // Assuming no string will match the pattern not be an email address
        if (matcher.find()) {
            return matcher.group(0);
        }
        else {
            return "No Email Found";
        }
    }

    public String getEmailAddress() {
        return emailAddress;
    }
}

interface BuisnessCardParser {
    ContactInfo getContactInfo(String document);
}

/*
 * This class defines the BusinessCard object. The class stores only a single 
 * getContactInfo method which takes a String and returns a Contact object.
 */
class BuisnessCard implements BuisnessCardParser {
    public ContactInfo getContactInfo(String document) {
        return new Contact(document);
    }
}

class Driver {
    public static void main(String[] args) {
        // Checks for an input String
        if (args.length > 0) {
            // Define parser object
            BuisnessCard parser = new BuisnessCard();
            // Parse business card text
            ContactInfo contact = parser.getContactInfo(args[0]);
            String output = "Name: " + contact.getName() + "\nPhone: " 
                            + contact.getPhoneNumebr() + "\nEmail: " 
                            + contact.getEmailAddress();
                            
            System.out.println(output);
        }
        else {
            System.out.println("Error: No Input");
        }
    }
}
