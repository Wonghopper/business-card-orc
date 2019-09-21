/*
 * 
 * Date Created: 9/21/19
 * Last Updated: 9/21/19
 * Last Update: 
 * TODO: 
 */
 
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

interface ContactInfo {
    String getName();
    String getPhoneNumebr();
    String getEmailAddress();
}

class Contact implements ContactInfo {
    String name = "Default Name";
    String phoneNumber = "Default Number";
    String emailAddress = "Default Email";

    Contact(String document) {
        name = "Constructor Name";
        phoneNumber = parseNumber(document);
        emailAddress = parseEmail(document);
    }

    public String parseName(String text) {
        return "TODO";
    }

    public String getName() {
        return name;
    }

    public String parseNumber(String text) {
        //Assuming North American Numbering Plan
        // Create regex to pattern match phone numbers
        String regex = "([A-Za-z]*[ \\t]?[A-Za-z]*)[ \\t:;#\\|]?[ \t]?\\+?(1)?[ \\t]?[ \\t\\(\\-]?(\\d{3})[ \\t\\)\\-]?[ \\t]?(\\d{3})[ \\t\\-]?(\\d{4})";
        Pattern pattern = Pattern.compile(regex);
        
        // Create matcher object
        Matcher matcher = pattern.matcher(text);

        // Store character string prefixing numebr
        ArrayList<String> numberDesc = new ArrayList<String>(1);
        // Stores numbers that match pattern (phone, fax, etc.)
        ArrayList<String> numbers = new ArrayList<String>(1);
        while (matcher.find()) {
            numberDesc.add(matcher.group(1).toLowerCase());
            if (matcher.group(2) != null) {
                numbers.add(matcher.group(2) + matcher.group(3) + matcher.group(4) + matcher.group(5));
            }
            else {
                numbers.add(matcher.group(3) + matcher.group(4) + matcher.group(5));
            }
        }

        if (numbers.size() == 1) {
            return numbers.get(0);
        }
        else if (numbers.size() > 1) {
            for (int i = 0; i < numbers.size(); i ++) {
                if (numberDesc.get(i).contains("tel") || numberDesc.get(i).contains("pho")) {
                    return numbers.get(i);
                }
            }
            // In case of no number description matche, assume first number is correct number
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
        //Can't parse comments i.e. "(.*)" with regex, need operational symmantics
        // Create regex to pattern match emails
        String regex = "[A-Za-z\\d](\\.?[A-Za-z\\d\\-_])*@[A-Za-z\\d](\\.?[A-Za-z\\d\\-]+)*";
        Pattern pattern = Pattern.compile(regex);

        // Create matcher object
        Matcher matcher = pattern.matcher(text);

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

class BuisnessCard implements BuisnessCardParser {
    public ContactInfo getContactInfo(String document) {
        ContactInfo contact = new Contact(document);
        return contact;
    }
}

class Driver {
    public static void main(String[] args) {
        if (args.length > 0) {
            BuisnessCard parser = new BuisnessCard();
            ContactInfo contact = parser.getContactInfo(args[0]);
            //TODO: Return cat of strings printed in final
            System.out.println("Name: " + contact.getName());
            System.out.println("Phone: " + contact.getPhoneNumebr());
            System.out.println("Email: " + contact.getEmailAddress());
        }
        else {
            System.out.println("Error: No Input");
        }
    }
}
