Pizza Delivery System (PDS)
=====================

Team of 6 developers completed the project based on customer requirements.

Instructions
==============
To run the software you must type at the command line:

    java -jar pds.jar

In order for the software to successfully launch, two files must be present in the same directory: menu.txt and config.txt.  

This software also requires the Java Runtime Environment (JRE) for Java SE 6 to be installed. If you do not have it, you can download and install it from this website: http://www.oracle.com/technetwork/java/javase/downloads/index.html

menu.txt
--------------
This file represents the menu that will be parsed and displayed in the application. This file allows for the user to provide a custom menu. The file format is as follows:

    0 | size | price | price per topping | prep time | cook time | oven space units
    1 | name
    2 | name | price | prep time | cook time | oven space units

    # 0 means pizza
    # 1 means topping
    # 2 means other kind of food
    # price per topping is for a whole pizza; half pizza is half the topping price

and here is an example menu:

    0|small|5.00|0.75|10|5|2
    1|Sausage
    2|Salad|2|3|0|0
    
config.txt
--------------
This file stores arguments that can be configured at the store's opening. These arguments include: number of chefs, number of drivers, number of ovens, and the ovens' capacity.  

Each argument is represented as a single integer specified in the order as listed, and separated by newlines.  

Here is an example config.txt file that has 1 chef, 2 drivers, and 3 ovens of size 40 capacity:
    
    1
    2
    3
    40

Release notes
==============

Limitations
---------------
* Cannot remove or modify an item or topping from an order once it has been added. A new order cannot be placed for a customer until their previous order has been completed (the Add/Modify order button will not do anything if there is a current order in progress).
* Driver will only delivery one order at a time.
* If Add/Modify Order is pressed to place a new order, the order has to be placed at that point.
* The Manager views that are accessible from the Welcome Screen are not implemented yet.
* The View Orders table must be manually refreshed to see the status of an order change.
* Time estimation has not been incorporated into the UI yet.
* The database of customers and orders is not saved between different launches of pds.jar.

Bugs
---------------
* There is possibly one threading bug that will cause a ConcurrentModification exception to be thrown. Since it is a non-deterministic bugs, it is difficult to tell if the issue has been truly solved.
* Doesn't show toppings as being on the left or right sides in the "Add Topping" window. The price is halved correctly, but the radio buttons do not update when the topping is reselected.
	* If the program isn't closed by returning to the "Welcome" screen and clicking the exit button, the code (the javaw.exe process) does not exit.

Other files in public_html
===========================
* README.md - this file
* pds.jar - the executable jar file
* menu.txt - the menu file read by the executable file at startup
* config.txt - various parameter file read by the executable at startup
* index.html - a simple html webpage to easily access files in public_html
* Acceptance Testing.xlsx - a copy of the acceptance test plan document
* Requirements.docx - a copy of the requirements document
