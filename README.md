# -DSA-10279-ARSH-
Java based Contact Manager using Linked List, BST, HashMap, Sorting, and File Handling
Contact Manager with Fast Search (PS 73)

Student: Arsh Kamal | Language: Java

A console-based contact directory using core DSA concepts.

Concepts Used

• Linked List - stores all contacts
• BST - indexes names, in-order traversal gives A-Z browsing
• HashMap - phone number to contact, for fast phone lookup
• Sorting - directory sorted by name (ascending / descending)
• File Handling - loads contacts.txt at startup, saves on exit

Features

Add, display, search by name, search by phone, update, delete, sort.

Rules: phone must be exactly 10 digits and unique. BST and HashMap are rebuilt after every add/update/delete, so they always stay consistent.

How to Run

javac ContactManager.java
java ContactManager

Sample Output

Contact added: C101 | Aman | 9876543210
C101 | Aman | 9876543210
Contact not found.
1. C102 | Riya | 9123456780
2. C101 | Aman | 9876543210
