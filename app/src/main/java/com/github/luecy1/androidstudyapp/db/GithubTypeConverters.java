package com.github.luecy1.androidstudyapp.db;

import android.arch.persistence.room.TypeConverter;

import java.util.Collections;
import java.util.List;

/**
 * Created by you on 2018/01/31.
 */

public class GithubTypeConverters {

    @TypeConverter
    public static List<Integer> stringToIntList(String data) {

        if (data == null) {
            return Collections.emptyList();
        }
//        return StringUtil.splitToIntList(data);
        return null;
    }

    @TypeConverter
    public static String intListToString(List<Integer> ints) {
//        return StringUtil.joinIntoString(ints);
        return null;
    }
}
