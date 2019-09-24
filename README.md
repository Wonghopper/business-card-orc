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
then uses a dictionary of names to narrow down the list of potential names. In 
the case where that is not enough, the tool also assumes that because the email 
is on a business card, the local-part of the email will contain part of the 
business card's owner's name. Under this assumption, the potential names are 
compared to the email on the business card and the potential name which contains
a substring contained in the local-part of the email is returned as the name of 
the business card owner.

Note that name prefixes and suffixes are not currently covered by the parser. 
Neither are names longer than 3 strings or names containing symbols. 

There is a known bug in the case where the name is less than 3 strings long and 
is followed by or is following another character string that is delimited with a
comma, period, space, or tab. In this case the non-name string is returned as 
part of the name.

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

## Files

### business_card_parser.java

Java file containing the source code for the business card parser tool.

### names_dic.txt

Text file containing given names in the population of U.S. births where the 
individual has a Social Security Number between the years 1950 and 2018. This 
file is used to check potential names against a list of known names. Data 
gathered from the [Social Security Administration](https://www.ssa.gov/oact/babynames/limits.html).

### test_data

Folder containing text files of sample business card information used to test 
parser.

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
