package controller;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Class to handle inputted data and validation
 */
public class InputController {

    /**
     * Input Validator for an account NIF value
     * Loops until a valid value is inserted
     * Uses regex for validation
     * @param message small string to indicate the parameter being inputed ("NIF>")
     * @return the value of the inserted NIF
     */
    //Para testar os Regex's: https://regex101.com/r/aiwNmf/1
    public static String readNif(String message){
        while(true){
            String nif = readString(message);
             if(Pattern.matches("\\d{9}", nif)){
                 return nif;
             }
            System.out.println("Not a valid NIF!");
        }
    }

    /**
     * Input Validator for an account email
     * Loops until a valid value is inserted
     * Uses regex for validation
     * @param message small string to indicate the parameter being inputed ("Email>")
     * @return the value of the inserted email
     */
    public static Boolean validateEmail(String message){
        String email = message.trim().toLowerCase();
        if(Pattern.matches("^[a-z0-9]{1}[a-z0-9._-]+@[a-z]{1}[a-z.]+[a-z]{1}\\.[a-z]{2,3}$", email)){
            return true;
        }
        return false;
    }

    /**
     * Input Validator for an account phone number
     * Loops until a valid value is inserted
     * Uses regex for validation
     * @param message small string to indicate the parameter being inputed ("Phone>")
     * @return the value of the inserted phone number
     */
    public static String readPhone(String message){
        while(true){
            String phone = readString(message);
            if(Pattern.matches("[239]{1}\\d{8}", phone)){
                return phone;
            }
            System.out.println("Not a valid Phone Number!");
        }
    }

    /**
     * Validates an inputted int value
     * Loops until a valid value is inserted
     * @param message small string to indicate the parameter being inputted ("id>")
     * @return the value of the parsed inputed value
     */
    public static int readInt(String message){
        while(true){
            try{
                return Integer.valueOf(readString(message).trim()).intValue();
            }
            catch(Exception e){
                System.out.println("!!! Not an integer !!!");
            }
        }
    }

    /**
     * Turns an inputted date into a epoch timestamp
     * Loops until a valid value is inserted
     * Forms a timestamp based on inputted values for day/mounth/year
     * @return the value of the epoch timestamp
     */
    public static long readDate( ){
        while(true){
            //https://stackoverflow.com/questions/6687433/convert-a-date-format-in-epoch
            System.out.println("Manufacture Date: ");
            int year = readInt("Year>");
            int month = readInt("Mouth>");
            int day = readInt("Day>");

            try{
                String readData = year + "-" + month + "-" + day;
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date date = df.parse(readData);
                long epoch = date.getTime()/1000;


                return epoch;
            }
            catch(Exception e){
                System.out.println("!!! Invalid Date. !!!");
            }
        }
    }

    /**
     * Validates an inputted double value
     * Loops until a valid value is inserted
     * @param message small string to indicate the parameter being inputted (">")
     * @return the value of the parsed double value
     */
    public static double readDouble(String message){
        while(true){
            try{
                return Double.valueOf(readString(message).trim()).doubleValue();
            }
            catch(Exception e){
                System.out.println("!!! Not a double !!!");
            }
        }
    }

    /**
     * Validates an inputted long value
     * Loops until a valid value is inserted
     * @param message small string to indicate the parameter being inputted (">")
     * @return the value of the parsed long value
     */
    public static long readLong(String message){
        while(true)
            try{
                return Long.valueOf(readString(message).trim()).longValue() ;
            }
            catch(Exception e){
                System.out.println("!!! Not a long !!!");
            }
    }


    /**
     * Validates an inputted float value
     * Loops until a valid value is inserted
     * @param message small string to indicate the parameter being inputted (">")
     * @return the value of the parsed float value
     */
    public static float readFloat(String message){
        while(true){
            try{
                return Float.valueOf(readString(message).trim()).floatValue();
            }
            catch(Exception e){
                System.out.println("!!! Not a float !!!");
            }
        }
    }

    /**
     * Validates an inputted string value
     * Loops until a valid value is inserted
     * @param message small string to indicate the parameter being inputted ("Name>")
     * @return the value of the string
     */
    public static String readString(String message){
        String s = "";

        try{
            System.out.print(message);
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in), 1);
            s = in.readLine();
        }
        catch (IOException e){
            System.out.println("Error reading from the input stream.");
        }

        return s;
    }


}