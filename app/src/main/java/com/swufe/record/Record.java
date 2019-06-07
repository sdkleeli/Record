package com.swufe.record;

import java.util.Date;
import java.util.UUID;

public class Record {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSloved;

    public Record(){
        //生成唯一标识符
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public UUID getId(){
        return mId;
    }

    public String getTitle(){
        return mTitle;
    }

    public void setTitle(String title){
        mTitle = title;
    }

    public Date getDate(){
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSloved(){
        return mSloved;
    }

    public void setSolved(boolean sloved) {
        mSloved = sloved;
    }
}
