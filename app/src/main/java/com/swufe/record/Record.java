package com.swufe.record;

import java.util.Date;
import java.util.UUID;

public class Record {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSloved;



    private int mHour;
    private int mMinute;

    public int getHour() {
        return mHour;
    }
    public void setHour(int mHour) {
        this.mHour = mHour;
    }
    public int getMinute() {
        return mMinute;
    }
    public void setMinute(int mMinute) {
        this.mMinute = mMinute;
    }


    public Record(){
        //生成唯一标识符
       this( UUID.randomUUID());
    }

    public Record(UUID id){
        mId = id;
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

    public String getPhotoFilename(){
        return "IMG_" + getId().toString() + ".jpg";
    }

}
