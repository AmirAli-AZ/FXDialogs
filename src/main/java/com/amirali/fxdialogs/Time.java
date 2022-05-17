package com.amirali.fxdialogs;

import java.util.Calendar;

public record Time(int hours, int minutes, AM_PM am_pm) {

    public Time {
        if (!(hours <= 12 && hours > 0))
            hours = 0;
        if (!(minutes <= 59 && minutes > 0))
            minutes = 0;
    }

    public static Time getCurrentTime() {
        return new Time(
                Calendar.getInstance().get(Calendar.HOUR),
                Calendar.getInstance().get(Calendar.MINUTE),
                Calendar.getInstance().get(Calendar.AM_PM) == Calendar.AM ? Time.AM_PM.AM : Time.AM_PM.PM
        );
    }

    public enum AM_PM {
        AM,PM
    }
}
