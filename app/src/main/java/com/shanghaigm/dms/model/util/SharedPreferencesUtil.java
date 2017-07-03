package com.shanghaigm.dms.model.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.shanghaigm.dms.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * SharedPreferencesParam储存参数类
 * Created by Berlin on 2016/5/23.
 *@ author秦昌彪
 */

public class SharedPreferencesUtil {
    public static final String DB_SPLIT_FLAG = "#dms#";

    private Context context;

    public SharedPreferencesUtil(Context context) {
        this.context = context;
        PREFERENCE_NAME = context.getString(R.string.app_name);
    }

    private static String PREFERENCE_NAME = "unknown";
    /**
     * 保存参数
     * @ param参数键
     * @ param参数值
     */
    public void saveParam(String spName, String spstr) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(spName, spstr).commit();
    }

    /* *
     * 获取参数
     * @ param参数键
     * @ return参数值
     */
    public String getParam(String spName) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
        String param = sp.getString(spName, null);
        if(param==null){
            param="";
        }
        return param;
    }

    private SharedPreferencesUtil() {
        throw new AssertionError();
    }

    /**
     * put string preferences
     *
     * @param context   context
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putString(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     * get string preferences
     *
     * @param context  context
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or null. Throws ClassCastException if there is a preference with this
     *         name that is not a string
     * @see #getString(Context, String, String)
     */
    public static String getString(Context context, String key) {
        return getString(context, key, null);
    }

    /**
     * get string preferences
     *
     * @param context  context
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a string
     */
    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, defaultValue);
    }

    /**
     * put int preferences
     *
     * @param context  context
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putInt(Context context, String key, int value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    /**
     * get int preferences
     *
     * @param context  context
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     *         name that is not a int
     * @see #getInt(Context, String, int)
     */
    public static int getInt(Context context, String key) {
        return getInt(context, key, -1);
    }

    /**
     * get int preferences
     *
     * @param context  context
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a int
     */
    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getInt(key, defaultValue);
    }

    /**
     * put long preferences
     *
     * @param context  context
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putLong(Context context, String key, long value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    /**
     * get long preferences
     *
     * @param context  context
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     *         name that is not a long
     * @see #getLong(Context, String, long)
     */
    public static long getLong(Context context, String key) {
        return getLong(context, key, -1);
    }

    /**
     * get long preferences
     *
     * @param context  context
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a long
     */
    public static long getLong(Context context, String key, long defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getLong(key, defaultValue);
    }

    /**
     * put float preferences
     *
     * @param context  context
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putFloat(Context context, String key, float value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    /**
     * get float preferences
     *
     * @param context  context
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     *         name that is not a float
     * @see #getFloat(Context, String, float)
     */
    public static float getFloat(Context context, String key) {
        return getFloat(context, key, -1);
    }

    /**
     * get float preferences
     *
     * @param context  context
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a float
     */
    public static float getFloat(Context context, String key, float defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getFloat(key, defaultValue);
    }

    /**
     * put boolean preferences
     *
     * @param context  context
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putBoolean(Context context, String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * get boolean preferences, default is false
     *
     * @param context  context
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or false. Throws ClassCastException if there is a preference with this
     *         name that is not a boolean
     * @see #getBoolean(Context, String, boolean)
     */
    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    /**
     * get boolean preferences
     *
     * @param context  context
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a boolean
     */
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getBoolean(key, defaultValue);
    }

    /**
     *
     * @param context context
     * @param key 数据名字
     * @param list 需要存储的字符清单
     * @return 存储是否成功
     */
    public static boolean putStringList(Context context, String key, ArrayList<String> list) {
        if (list == null || list.size() == 0) {
            return false;
        }
        String info = list.get(0);
        if (list.size() > 1) {
            for (int i = 1; i < list.size(); i++) {
                info = info + DB_SPLIT_FLAG + list.get(i);
            }
        }
        return putString(context, key, info);
    }

    /**
     *
     * @param context context
     * @param key 数据名字
     * @return 存储的清单
     */
    public static ArrayList<String> getStringList(Context context, String key) {
        String[] array = getStringArray(context, key);
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, array);
        return list;
    }

    /**
     *
     * @param context context
     * @param key 数据名字
     * @param array 需要存储的字符数组
     * @return 存储是否成功
     */
    public static boolean putStringArray(Context context, String key, String[] array) {
        if (array == null || array.length == 0){
            return false;
        }
        String info = array[0];
        if (array.length > 1) {
            for (int i = 1; i < array.length; i++) {
                info = info + DB_SPLIT_FLAG + array[i];
            }
        }
        return putString(context, key, info);
    }

    /**
     *
     * @param context context
     * @param key 数据名字
     * @return 存储的字符数组
     */
    public static String[] getStringArray(Context context, String key) {
        return getString(context, key, "").split(DB_SPLIT_FLAG);
    }
}
