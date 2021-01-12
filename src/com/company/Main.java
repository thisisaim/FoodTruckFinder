package com.company;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        HttpClient client = HttpClient.newHttpClient();
        String dow = getDayOfWeek();
        String url = "https://data.sfgov.org/resource/jjew-r69b.json?$query=SELECT%20applicant,%20location,%20dayofweekstr,%20start24,%20end24%20WHERE%20dayofweekstr%20=%20%27" + dow + "%27%20order%20by%20applicant,%20location";
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(Main::parse)
                .join();
    }

    public static String parse(String responseBody) {
        JSONArray foodCarts = new JSONArray(responseBody);
        List<JSONObject> openCarts = new ArrayList<>();
        for (int i = 0; i < foodCarts.length(); i++) {
            JSONObject foodCart = foodCarts.getJSONObject(i);
            String name = foodCart.getString("applicant");
            String open = foodCart.getString("start24");
            String close = foodCart.getString("end24");
            String addr = foodCart.getString("location");
            if (isOpen(open, close)) {
                foodCart.put("isOpen", true);
                openCarts.add(foodCart);
            }
        }
        print(openCarts);
        return null;
    }
    /*
    Summary: This method takes in two times (24 hour format); Opening time and closing time for a particular cart
            and returns if the food cart is open or not; True -> Open, False -> Close

    Input: Start time and end time as Strings in 24 hour format. ex 09:00, 19:00
    Output: Boolean value of whether the car is open or not

    The reason there is a check for 24:00 is because in the response from the API, there are some carts that have 24:00
    as their close time.  24:00 can also be written as 00:00
    */

    public static boolean isOpen(String startTime, String endTime) {
        LocalTime currentTime = LocalTime.now();
        if (endTime.equals("24:00")) { //24:00 doesn't exist
            endTime = "23:59:59";
        }
        return currentTime.isBefore(LocalTime.parse(endTime)) && currentTime.isAfter(LocalTime.parse(startTime));
    }

    /*
    Summary: This method does not take in any arguments and returns the day of the week e.g. Monday, Tuesday, etc...

    Input: None
    Output: Boolean value of the day of the week (Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday)
     */
    public static String getDayOfWeek() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE");
        return currentDate.format(dtf);
    }

    /*
    Summary: Prints the list of open food carts (name and location) 10 at a time.  If there are more than 10 carts in the list, it prompts
    the user to hit a key to continue to see the list.  This is to avoid overwhelming the user.

    Input: List of JSONObjects that contain all the food carts that are open.
    Output: Prints all the food carts out to the console that are open.
     */
    public static void print(List<JSONObject> openCarts) {
        int count = 10;
        if (openCarts.size() <= count) {
            for (JSONObject cart : openCarts) {
                System.out.println(cart.getString("applicant"));
                System.out.println(cart.getString("location"));
                System.out.println();
            }
        } else {
            for (int i = 1; i < openCarts.size() + 1; i++) {
                System.out.println(openCarts.get(i - 1).getString("applicant"));
                System.out.println(openCarts.get(i - 1).getString("location"));
                System.out.println();

                if (i % 10 == 0) {
                    promptEnter();
                }
            }
        }
    }
    /*
    Summary: Prompts the user on the console to hit a key, to continue
    Input: None
    Output: None
     */
        public static void promptEnter(){
            System.out.println("Press \"ENTER\" to continue");
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
        }

}
