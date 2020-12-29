/**
 * This class contains the methods to pack and unpack JSON strings to
 * communicate with the API.
 */
package com.example.fitnessappmap;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonParser {


    /**
     * Converts the JSONObject to a User
     *
     * @param jsonObject: jsonObject of the user data.
     * @return User
     */
    public static User jsonObjectToUser(JSONObject jsonObject) {
        try {
            String userName = jsonObject.getString("_id");
            String password = jsonObject.getString("password");
            String name = jsonObject.getString("name");
            JSONArray friendsJsonArray = jsonObject.getJSONArray("friends");
            JSONArray runsJsonArray = jsonObject.getJSONArray("runs");
            JSONArray milestonesJsonArray = jsonObject.getJSONArray(
                    "milestones");
            String[] friendsArray = new String[friendsJsonArray.length()];
            Double[] runsArray = new Double[runsJsonArray.length()];
            String[] milestonesArray = new String[milestonesJsonArray.length()];
            for (int index = 0; index < friendsArray.length; index++) {
                friendsArray[index] = friendsJsonArray.getString(index);
            }
            for (int index = 0; index < runsArray.length; index++) {
                runsArray[index] = runsJsonArray.getDouble(index);
            }
            for (int index = 0; index < milestonesArray.length; index++) {
                milestonesArray[index] = milestonesJsonArray.getString(index);
            }
            User user = new User(userName, password, name, friendsArray,
                    runsArray, milestonesArray);
            return user;
        } catch (JSONException e) {
            return null;
        }

    }

    /**
     * Converts a string containing the JSON data to a User
     *
     * @param jsonData: String of JSON data
     * @return User
     */
    public static User stringToUser(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            if (jsonArray.length() == 0) {
                return null;
            } else {
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                User user = jsonObjectToUser(jsonObject);
                return user;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts a String containing a JSON Array to a list of Users.
     *
     * @param jsonData: String of JSON data.
     * @return Array of Users.
     */
    public static User[] stringToMultipleUsers(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            if (jsonArray.length() == 0) {
                return null;
            } else {
                User[] users = new User[jsonArray.length()];
                for (int index = 0; index < jsonArray.length(); index++) {
                    User user =
                            jsonObjectToUser(jsonArray.getJSONObject(index));
                    if (user != null) {
                        users[index] = user;
                    } else {
                        return null;
                    }
                }
                return users;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts a User to a string in JSON format.
     *
     * @param user: User to convert.
     * @return JSON formatted string.
     */
    public static String userToString(User user) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("_id", user.getUserName());
            jsonObject.put("password", user.getPassword());
            jsonObject.put("name", user.getName());
            jsonObject.put("friends", new JSONArray());
            jsonObject.put("runs", new JSONArray());
            jsonObject.put("milestones", new JSONArray());
            String jsonData = jsonObject.toString();
            return jsonData;
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * Converts a run to a string in JSON format.
     *
     * @param run: run to convert.
     * @return JSON formatted string.
     */
    public static String runToString(Double run) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("run", run);
            String jsonData = jsonObject.toString();
            return jsonData;
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * Converts a milestone to a string in JSON format.
     *
     * @param milestone: milestone to convert
     * @return JSON formatted string.
     */
    public static String milestoneToString(String milestone) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("milestone", milestone);
            String jsonData = jsonObject.toString();
            return jsonData;
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * Converts a friend to a string in JSON format.
     *
     * @param friend: friend to convert.
     * @return JSON formatted string.
     */
    public static String friendToString(String friend) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("friend", friend);
            String jsonData = jsonObject.toString();
            return jsonData;
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * Converts a JSON formatted string to the progress of a user.
     *
     * @param jsonData: JSON string to convert.
     * @return Double of progress.
     */
    public static Double stringToProgress(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            Double progress = Double.parseDouble(jsonObject.getString(
                    "progress"));
            return progress;
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * Converts a JSON formatted string to the Runs of a user.
     *
     * @param jsonData: JSON string to convert.
     * @return Array of runs of a user
     */
    public static Double[] stringToRuns(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            Double[] runsArray = new Double[jsonArray.length()];
            for (int index = 0; index < runsArray.length; index++) {
                runsArray[index] = jsonArray.getDouble(index);
            }
            return runsArray;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts a JSON formatted string array to an array os strings
     *
     * @param jsonData: JSON formatted data.
     * @return Array of strings.
     */
    public static String[] stringListToArray(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            String[] stringArray = new String[jsonArray.length()];
            for (int index = 0; index < stringArray.length; index++) {
                stringArray[index] = jsonArray.getString(index);
            }
            return stringArray;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
