package com.insight.insight.utils;

import java.util.Date;

/**
 * Created by Manouchehr on 2/1/2015.
 */
public class RowData {
    public Date time;
    public String statusText;
    public int icon;

    public RowData(Date time, String statusText, int icon) {
        this.time = time;
        this.statusText = statusText;
        this.icon = icon;
    }
}
