package exam.sai.com.designpattern.utils;

import android.databinding.InverseMethod;

/**
 * Created by sai on 2017-10-01.
 * for converting data type in 2-way data binding
 */

public class Converter {
    @InverseMethod("toInt")
    public static String toString(int num) {
        return String.valueOf(num);
    }

    public static int toInt(String string) {
        return string.isEmpty() ? 0 : Integer.parseInt(string);
    }
}
