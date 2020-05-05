package com.eshen.voucherunion.utils;

/**
 * Created by Sin on 2020/5/3
 */
public class UrlUtils {
    public static String CreateHomePagerUrl(int materialId, int page) {
        return "discovery/" + materialId + "/" + page;
    }

    public static String getCoverPath(String pict_url) {
        if (pict_url.startsWith("http") || pict_url.startsWith("https")) {
            return pict_url;
        } else {
            return "https:" + pict_url;
        }
    }

    public static String getCoverPath(String pict_url, int size) {
        if (pict_url.startsWith("http") || pict_url.startsWith("https")) {
            return pict_url;
        } else {
            return "https:" + pict_url + "_" + size + "x" + size + ".jpg";
        }
    }

    public static String getTicketUrl(String url) {
        if (url.startsWith("http") || url.startsWith("https")) {
            return url;
        } else {
            return "https:" + url;
        }
    }

    public static String getRecommendPageContentUrl(int categoryId) {
        return "recommend/" + categoryId;
    }
}
