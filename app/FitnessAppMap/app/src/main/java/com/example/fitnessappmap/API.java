/**
 * This class contains the methods that allows the app to communicate with
 * the API to access the database.
 */
package com.example.fitnessappmap;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class API {


    private static final String BASE_URL = "http://10.0.2.2:5000";

    /**
     * Allows the app to get a specific attribute of the user
     *
     * @param userName:  The user to get.
     * @param attribute: The attribute to get.
     * @return The attribute of the user.
     */
    public static String getAttribute(String userName, String attribute) {
        HttpURLConnection connection;
        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();
        try {
            URL url = new URL(BASE_URL + "/get/" + attribute + "/" +
                    userName);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();

            if (status > 299) {
                return null;
            } else {
                reader =
                        new BufferedReader(new InputStreamReader(connection
                                .getInputStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            }
            return responseContent.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (ProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Allows the app to create a user.
     *
     * @param jsonData: The userdata of the new user.
     * @return true if user is created.
     */
    public static boolean postUser(String jsonData) {
        HttpURLConnection connection;
        try {
            URL url = new URL(BASE_URL + "/add/user");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Content-Type", "application/json, " +
                    "charset=UTF-8");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            os.write(jsonData.getBytes("UTF-8"));
            os.close();
            int status = connection.getResponseCode();
            connection.disconnect();
            if (status > 299) {
                return false;
            } else {
                return true;
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
            return false;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Allows the app to add data to a user
     *
     * @param jsonData:  Data to send to the database.
     * @param userName:  User to add the data to.
     * @param attribute: Attribute that is being modified.
     * @return true if added.
     */
    public static boolean addAttribute(String jsonData, String userName,
                                       String attribute) {
        HttpURLConnection connection;
        try {
            URL url = new URL(BASE_URL + "/add/" + attribute + "/" +
                    userName);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Content-Type", "application/json, " +
                    "charset=UTF-8");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            os.write(jsonData.getBytes("UTF-8"));
            os.close();
            int status = connection.getResponseCode();
            connection.disconnect();
            if (status > 299) {
                return false;
            } else {
                return true;
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
            return false;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
