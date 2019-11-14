package com.adaptor;

import java.util.Comparator;

/**
 * Created by DELL on 13-Sep-17.
 */

public class IgnorCaseComprator implements Comparator<String> {
    @Override
    public int compare(String strA, String strB) {
        return strA.compareToIgnoreCase(strB);
    }
}
