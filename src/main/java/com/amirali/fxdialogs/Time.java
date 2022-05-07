package com.amirali.fxdialogs;

public record Time(int hours, int minutes, AM_PM am_pm) {

    public Time {
        if (!(hours <= 12 && hours > 0))
            hours = 0;
        if (!(minutes <= 59 && minutes > 0))
            minutes = 0;
    }

    public enum AM_PM {
        AM,PM
    }
}
