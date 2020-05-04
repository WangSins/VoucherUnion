package com.eshen.voucherunion.utils;

/**
 * Created by Sin on 2020/5/3
 */
public class UrlUtils {
    public static String CreateHomePagerUrl(int materialId, int page) {
        return "discovery/" + materialId + "/" + page;
    }

    public static String getCoverPath(String pict_url, int size) {
        return "https:" + pict_url + "_" + size + "x" + size + ".jpg";
    }
}