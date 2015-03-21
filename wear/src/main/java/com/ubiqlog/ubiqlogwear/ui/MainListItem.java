package com.ubiqlog.ubiqlogwear.ui;

import android.content.Context;
import android.content.Intent;

/**
 * Created by User on 2/4/15.
 */
public class MainListItem {
    private String title;
    private Integer imgId;
    private Class intentClass; //this is used for launching the activity in OnClick

    public MainListItem (String title, Integer imgId, Class intentClass ){
        this.title = title;
        this.imgId = imgId;
        this.intentClass = intentClass;
    }

    public String getTitle(){
        return title;
    }
    public Integer getImgId(){
        return imgId;
    }

    public Intent getIntent(Context context){
        return new Intent(context,intentClass);

    }
}
