package com.swufe.record;

import java.util.UUID;

public class Record {
    private UUID mId;
    private String mTitle;

    public Record(){
        //生成唯一标识符
        mId = UUID.randomUUID();
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
}
