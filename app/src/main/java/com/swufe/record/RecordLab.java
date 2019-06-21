package com.swufe.record;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.swufe.record.database.RecordDb.RecordBaseHelper;
import com.swufe.record.database.RecordDb.RecordCursor;
import com.swufe.record.database.RecordDb.RecordDb.RecordTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecordLab {
    private static RecordLab sRecordLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static RecordLab get(Context context){
        if(sRecordLab == null){
            sRecordLab = new RecordLab(context);
        }
        return sRecordLab;
    }

    private RecordLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new RecordBaseHelper(mContext)
                .getWritableDatabase();
    }

    public void addRecord(Record r){
        ContentValues values = getContentValues(r);

        mDatabase.insert(RecordTable.NAME,null,values);
    }

    public List<Record> getRecords(){
        List<Record> records = new ArrayList<>();

        RecordCursor cursor = queryRecords(null,null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                records.add(cursor.getRecord());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return records;
    }

    public Record getRecord(UUID id){
        RecordCursor cursor = queryRecords(
                RecordTable.Cols.UUID + "=?",
                new String[] {id.toString()}
        );
        try {
            if(cursor.getCount() ==0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getRecord();
        }finally {
            cursor.close();
        }
    }

    public File getPhotoFile(Record record){
        File externalFileSDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFileSDir == null){
            return null;
        }

        return new File(externalFileSDir,record.getPhotoFilename());
    }

    public void updateRecord(Record record){
        String uuidString = record.getId().toString();
        ContentValues values = getContentValues(record);

        mDatabase.update(RecordTable.NAME,values,
                RecordTable.Cols.UUID + "=?" ,
                new String[]{uuidString});
    }

    private static ContentValues getContentValues(Record record){
        ContentValues values = new ContentValues();
        values.put(RecordTable.Cols.UUID,record.getId().toString());
        values.put(RecordTable.Cols.TITLE,record.getTitle());
        values.put(RecordTable.Cols.DATE,record.getDate().getTime());
        values.put(RecordTable.Cols.SOLVED,record.isSloved() ? 1 : 0);

        return values;
    }

    private RecordCursor queryRecords(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                RecordTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new RecordCursor(cursor);
    }
}
