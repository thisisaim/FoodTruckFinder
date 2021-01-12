# FoodTruckFinder
RDFN Assessment

This is a simple console applicaiton that utilized the City of San Francisco's pulbic API to get the opening food trucks around the city.


# Usage
This applicaiton utilizes a simple GET request with no authentication.  If the user would like to configure such that it does authentication, it would need to be implemented.

# Construct
There are a couple things needed to contruct the query before passing it to the GET request.
- Getting the day of the week ```getDayofWeek()```.  Either as a numeric number or a literal day e.g. Monday, Tuesday, etc...
- In this instance, I chose to used the literal day and set the ```WHERE``` clause in the query to filter out the unwanted days.
- Next is paring the response, in parsing there are a couple things that we want; Name, location, openHours, closeHours.
- While parsing, I'm using ```.put()``` to append another field to the ```JSONObject``` afte checking that the food truck is open.
- Using the ```openHours``` and ```closeHours``` as parameters, the ```isOpen()``` method can be constructed using
the built-in function ```isBefore()``` and ```inAfter```.
- After having all the nesscary data, I use an ```ArrayList``` to store my final result, then pass it to ```print()``` method to display out on the console.
- The ```print()``` method prints the result list by ten food carts at a time, if the ```size()``` of the arrayList is greater than 10. Otherwise, it would display all the food carts.

# Installation
- Open your IDE and open Main.java
- Add a reference to the Java project, selecting the *.jar file (json-20201115.jar).  
- After referencing the jar file, you should be able to compile/run the application in your IDE.