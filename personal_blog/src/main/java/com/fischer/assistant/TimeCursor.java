package com.fischer.assistant;

import org.joda.time.DateTime;

public class TimeCursor {
    public static String toTime(DateTime dateTime)
    {
        return dateTime.toString("yyyy-MM-dd HH:mm:ss");
    };

}
