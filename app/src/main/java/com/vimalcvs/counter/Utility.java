package com.vimalcvs.counter;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Utility {
    /*delete the same groups*/
    static public List<String> deleteTheSameGroups(List<String> strings) {
        Set<String> set = new HashSet<>(strings);
        List<String> mList = Arrays.asList(set.toArray(new String[0]));
        Collections.sort(mList);
        return mList;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String formatDateToString(Date date){
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.YY HH:mm:ss", Locale.getDefault());
        return  dateFormat.format(date);
    }
}
