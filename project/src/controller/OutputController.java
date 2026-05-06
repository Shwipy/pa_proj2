package controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * Class to manage date and time Output formatting
 */
public class OutputController {

    /**
     * Function that turns a Long epoch timestamp into a string with the formated date
     * @param timestamp Long value referring to the epoch timestamp
     * @param type string variable used to identify the formating needed for the date
     * @return a string containing the formated time
     */
    //https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
    public static String dateFormater(Long timestamp, String type) {

        Instant instant = Instant.ofEpochSecond(timestamp);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = null;

        switch (type) {
            case "log":
                formatter = DateTimeFormatter.ofPattern("eeee; yyyy-MM-dd HH:mm:ss");
                break;

            case "date":
                formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                break;

            case "day":
                formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                break;

            case "hour":
                formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                break;

            case null:
            default:
                return "--";
        }

        return localDateTime.format(formatter);
    }

    /**
     * Displays the total amount of time the app was running for when it is closed
     * @param start epoch timestamp refering to the moment the app started
     */
    public static void appTimer(Long start) {
        Long end = System.currentTimeMillis();
        Long duration = end - start;

        Long totalSeconds = duration / 1000;
        Long sec = totalSeconds % 60;
        Long min = (totalSeconds / 60) % 60;
        Long hour = (totalSeconds / 3600);

        System.out.println(OutputController.dateFormater(start / 1000, "log"));
        System.out.println(OutputController.dateFormater(end / 1000, "log"));
        System.out.println("App was running for: " + duration + " Milliseconds" + "(" + sec + " Seconds; " + min + " Minutes; " + hour + " Hours)");

    }
}
