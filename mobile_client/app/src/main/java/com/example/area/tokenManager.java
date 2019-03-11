package com.example.area;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * The type Token manager.
 */
public class tokenManager {
    /**
     * Create shared pref.
     *
     * @param name    the name
     * @param id      the id
     * @param value   the value
     * @param context the context
     */
    public void createSharedPref(String name, String id, String value, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(id, value).apply();
    }

    /**
     * Retrieve shared pref string.
     *
     * @param name    the name
     * @param key     the key
     * @param context the context
     * @return the string
     */
    public String retrieveSharedPref(String name, String key, Context context) {
        SharedPreferences token = context.getSharedPreferences(name, MODE_PRIVATE);
        String restoredText = token.getString(key, null);
        return (restoredText);
    }

    /**
     * Clear shared pref.
     *
     * @param name    the name
     * @param context the context
     */
    public void clearSharedPref(String name, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().commit();
    }

    /**
     * Remove key shared pref.
     *
     * @param name    the name
     * @param key     the key
     * @param context the context
     */
    public void removeKeySharedPref(String name, String key, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }
}
