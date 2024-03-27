package com.itiaoling.log.date;

public enum  TimeFormatEnum {
    HH_mm("HH:mm"),
    HH_mm_ss("HH:mm:ss");

    private String format;


    public String getFormat() {
        return format;
    }

    TimeFormatEnum(String format) {
        this.format = format;
    }
}
