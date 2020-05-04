package com.eshen.voucherunion.model.domain;

/**
 * Created by Sin on 2020/5/4
 */
public class TicketParams {

    private String url;
    private String title;

    public TicketParams(String url) {
        this.url = url;
    }

    public TicketParams(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
