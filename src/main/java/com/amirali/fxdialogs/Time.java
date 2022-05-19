package com.amirali.fxdialogs;

import java.util.Calendar;

/**
 * @author Amir Ali
 * @param hours hours
 * @param minutes minutes
 * @param am_pm am_pm
 */
public record Time(int hours, int minutes, AM_PM am_pm) {

    /**
     * checks if (hour is more than 12 or less than 0) and (minute is more than 59 or less than 0),
     * sets hours or minutes to 0 if it's true
     * @param hours hours
     * @param minutes minutes
     * @param am_pm am_pm
     */
    public Time {
        if (hours > 12 || hours < 0)
            hours = 0;
        if (minutes > 59 || minutes < 0)
            minutes = 0;
    }

    /**
     * current time
     * @return Time
     */
    public static Time getCurrentTime() {
        return new Time(
                Calendar.getInstance().get(Calendar.HOUR),
                Calendar.getInstance().get(Calendar.MINUTE),
                Calendar.getInstance().get(Calendar.AM_PM) == Calendar.AM ? Time.AM_PM.AM : Time.AM_PM.PM
        );
    }

    /**
     * to specify am or pm
     */
    public enum AM_PM {
        AM,PM
    }
}
