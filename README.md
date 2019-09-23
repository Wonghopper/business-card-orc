# business-card-orc

This repository contains the code and test data for the Asymmetrik Business Card
OCR coding challenge. The challenge was to create a command line tool that 
parsed the text of a business card.

The text is parsed through the use of three regular expressions to retrieve the 
following information:

### Name

This tool makes the assumption that the name on the business card is in the 
format of two or three strings of characters, delimitered by spaces, tabs, 
periods, or commas. All strings that are of of this format are stored. The tool 
also assumes that because the email is on a business card, the local-part of the
email will contain part of the business card's owner's name. Under this 
assumption, the potential names are compared to the email on the business card 
and the potential name which contains a substring contained in the local-part of
the email is returned as the name of the business card owner.

### Phone Number

This tool assumes that the phone number on the business card uses the North 
American Numbering Plan (NANP). The formatting convention for this numbering 
plan is XXX-XXX-XXXX, prefixed by the country code 1. The two alphabetic strings
that appear before the number are also parsed. These strings are used in the 
case that the business card contains multiple strings that match the format of a
phone number. It is assumed that if there are multiple numbers, the telephone 
will be labeled with some "telephone".

There are numerous other formatting conventions for phone numbers, including 
varying number of total digits. Expanding the tool to cover these other national
conventions would require a more complex regular expression.

### Email Address

This tool assumes a simplified formatting convension for email addresses. It is 
assumed that the email address is of the format local-part@domain. The 
local-part is a string of alphanumeric characters, underscores, hypens, and 
periods. The string must begin with with an alphanumeric character, and not end 
with a period, or have two periods consecutively. The domain name is a string 
starting and ending with an alphanumeric character, and contains alphanumeric 
characters, hypens, and periods, again not have two periods consecutively.

The assumed, simplified version of an email address was chosen based on the 
formatting used by common email providers. Further expansion of the tool would 
require a more complex regular expression or to use a different matching system,
such as a lexer and parser to match formats that can not be matched by a regular
expression (i.e. email address comments).

## Usage
Requires the Java Runtime Environment and Java Development Kit to compile and 
run the program.
To install the Java Runtime Environment
```bash
apt install default-jre
```
To install the Java Development Kit
```bash
sudo apt install default-jdk
```

To compile the program
```bash
javac business_card_parser.java
```
To use from the command line, pass the text file of the business card into the 
Driver executable
```bash
java Driver "$(< Business_Card_Text)"
```
