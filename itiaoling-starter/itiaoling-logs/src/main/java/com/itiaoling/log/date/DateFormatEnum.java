package com.itiaoling.log.date;



/************************************************************
 * Copy Right Information :
 * Project :
 * JDK version used : 1.8.0
 * Comments :
 *       日期相关格式
 * Modification history :
 *
 * Sr Date         Modified By     Why & What is modified
 * 1. 2018/04/24   fulw            Initial
 * 2. 2020/05/07   fulw            add yyyyMMdd format
 ***********************************************************/
public enum DateFormatEnum {

    YYYYMMDD("yyyyMMdd"),
    YYYY_MM_DD("yyyy-MM-dd"),
    BACK_SLANT_yyyy_MM_dd("yyyy/MM/dd");

    private String format;

    public String getFormat() {
        return format;
    }

    DateFormatEnum(String format) {
        this.format = format;
    }


    //***** 临时记录一下*****/
//   private  static final String YYYYMMDD = "yyyyMMdd";
//   private  static final String BACK_SLANT_yyyy_MM_dd = "yyyy/MM/dd";
   private  static final String BACK_SLANT_yyyy_MM_ddHHmmss = "yyyy/MM/ddHH:mm:ss";
   private  static final String YYYYMMDDHHmm = "yyyyMMddHHmm";
   private  static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";
   private  static final String yyMMddHHmmssSSS = "yyMMddHHmmssSSS";
   private  static final String yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS";
   private  static final String yyMMddHHmmss = "yyMMddHHmmss";
   private  static final String YYYY_MM_DDTHHmmss = "yyyy-MM-dd'T'HH:mm:ss";
   private  static final String YYYY_MM_DDTHHmmssZ = "yyyy-MM-dd'T'HH:mm:ss'Z'";
   private  static final String EEE_MMM_dd_HHmmss_zzz_yyyy = "EEE MMM dd HH:mm:ss zzz yyyy";
}
