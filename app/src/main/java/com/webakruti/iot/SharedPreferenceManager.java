package com.webakruti.iot;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;



/**
 * Manages the shared preferences all over the application
 */
public class SharedPreferenceManager {
    private static Context applicationContext;
    private static SharedPreferences tuitionPlusPreferences;
    public static void setApplicationContext(Context context) {
        applicationContext = context;
        setPreferences();
    }

    private static void setPreferences() {
        if (tuitionPlusPreferences == null) {
            tuitionPlusPreferences = applicationContext.getSharedPreferences("niramlrail",
                    Context.MODE_PRIVATE);
        }
    }

    public static void clearPreferences() {
        tuitionPlusPreferences.edit().clear().commit();
    }



    public static void storeUserResponseObjectInSharedPreference(LoginModel user) {
        SharedPreferences.Editor prefsEditor = tuitionPlusPreferences.edit();
        //  prefsEditor.clear();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString("UserResponseObject", json);
        prefsEditor.commit();
    }

    public static LoginModel getUserObjectFromSharedPreference() {
        Gson gson1 = new Gson();
        String json1 = tuitionPlusPreferences.getString("UserResponseObject", "");
        LoginModel obj = gson1.fromJson(json1, LoginModel.class);
//		Log.e("RetrivedName:", obj.getFirstName());
        return obj;
    }


}
