package com.itiaoling.log.date;


/************************************************************
 * Copy Right Information :
 * Project :
 * JDK version used : 1.8.0
 * Comments :
 *       时间相关格式
 * Modification history :
 *
 * Sr Date         Modified By     Why & What is modified
 * 1. 2018/04/24   fulw            Initial
 ***********************************************************/
public enum DateTimeFormatEnum {

    yyyyMMddHHmmss("yyyyMMddHHmmss"),
    YYYY_MM_DDHHmm("yyyy-MM-dd HH:mm"),
    YYYY_MM_DDHHmmss("yyyy-MM-dd HH:mm:ss"),
    YYYY_MM_DDHHmmssSSS("yyyy-MM-dd HH:mm:ss.SSS"),

    // yyyy-MM-dd'T'HH:mm:ss'Z'

    /**
     * iso8601 格式 yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
     */
    ISO8601_YYYY_MM_DD_T_HH_mm_ss_SSS_Z("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
    ISO8601_YYYY_MM_DD_T_HH_mm_ss_Z("yyyy-MM-dd'T'HH:mm:ss'Z'"),

    ISO8601_YYYY_MM_DD_HH_mm("yyyy-MM-dd'T'HH:mm"),

    // yyyy-MM-dd'T'HH:mm:ss
    ISO8601_YYYY_MM_DD_T_HH_mm_ss("yyyy-MM-dd'T'HH:mm:ss");


    private String format;


    public String getFormat() {
        return format;
    }

    DateTimeFormatEnum(String format) {
        this.format = format;
    }
}
