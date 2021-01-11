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

    public static boolean isOpen(String startTime, String endTime) {
        LocalTime currentTime = LocalTime.now();
        if (endTime.equals("24:00")) { //24:00 doesn't exist
            endTime = "23:59:59";
        }
        return currentTime.isBefore(LocalTime.parse(endTime)) && currentTime.isAfter(LocalTime.parse(startTime));
    }

    public static String getDayOfWeek() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE");
        return currentDate.format(dtf);
    }

    public static void print(List<JSONObject> openCarts) {
        int n = openCarts.size();
        int count = 10;
        if (n <= count) {
            for (JSONObject cart : openCarts) {
                System.out.println(cart.getString("applicant"));
                System.out.println(cart.getString("location"));
                System.out.println();
            }
        } else {
            for (int i = 1; i < n+1; i++) {
                System.out.println(openCarts.get(i-1).getString("applicant"));
                System.out.println(openCarts.get(i-1).getString("location"));
                System.out.println();

                if (i % 10 == 0) {
                    promptEnter();
                }
            }
        }
    }
        public static void promptEnter(){
            System.out.println("Press \"ENTER\" to continue");
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
        }

}
