package me.ureact.tracker.util;

import android.util.Log;

import java.util.ArrayList;

/**
 * Logging of the app with {@link ULogger#TAG} tag and multiline support
 *
 * Created by Valeriy Palamarchuk on 6/6/16
 */
public class ULogger {
    private static final String TAG = "ureact.me";

    /**
     * Divides the string into chunks for displaying them into the <code>debug</code> LogCat.
     *
     * @param text The text to be split and shown in LogCat
     */
    public static void d(String text) {
        ArrayList<String> messageList = splitString(text);
        for (String message : messageList) {
            Log.d(TAG, message);
        }
    }

    /**
     * Divides the string into chunks for displaying them into the <code>error</code>  LogCat.
     *
     * @param text The text to be split and shown in LogCat
     */
    public static void e(String text) {
        ArrayList<String> messageList = splitString(text);
        for (String message : messageList) {
            Log.e(TAG, message);
        }
    }

    /**
     * Divides the string into chunks for displaying them into the <code>warning</code>  LogCat.
     *
     * @param text The text to be split and shown in LogCat
     */
    public static void w(String text) {
        ArrayList<String> messageList = splitString(text);
        for (String message : messageList) {
            Log.w(TAG, message);
        }
    }

    /**
     * Divides the string into chunks for displaying them into the <code>info</code>  LogCat.
     *
     * @param text The text to be split and shown in LogCat
     */
    public static void i(String text) {
        ArrayList<String> messageList = splitString(text);
        for (String message : messageList) {
            Log.i(TAG, message);
        }
    }

    /**
     * Divides a string into chunks of a given character size.
     *
     * @param text      String text to be sliced
     * @param sliceSize int Number of characters
     * @return ArrayList<String>   Chunks of strings
     */
    private static ArrayList<String> splitString(String text, int sliceSize) {
        ArrayList<String> textList = new ArrayList<>();
        if (text.length() > sliceSize) {
            int chunkCount = text.length() / sliceSize;
            for (int i = 0; i <= chunkCount; i++) {
                int max = sliceSize * (i + 1);
                if (max >= text.length()) {
                    textList.add(text.substring(sliceSize * i));
                } else {
                    textList.add(text.substring(sliceSize * i, max));
                }
            }
        } else {
            textList.add(text);
        }
        return textList;
    }

    /**
     * Divides a string into chunks. 4000 - max string length of 1 logcat output.
     *
     * @param text String text to be sliced
     * @return ArrayList<String>
     */
    private static ArrayList<String> splitString(String text) {
        return splitString(text, 4000);
    }
}
