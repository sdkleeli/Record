package com.swufe.record.database.RecordDbSchema;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.swufe.record.Record;
import com.swufe.record.database.RecordDbSchema.RecordDbSchema.RecordTable;

import java.util.Date;
import java.util.UUID;

public class RecordCursorWrapper extends CursorWrapper {
    public RecordCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Record getRecord(){
        String uuidString = getString(getColumnIndex(RecordTable.Cols.UUID));
        String title = getString(getColumnIndex(RecordTable.Cols.TITLE));
        long date = getLong(getColumnIndex(RecordTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(RecordTable.Cols.SOLVED));

        Record record = new Record(UUID.fromString(uuidString));
        record.setTitle(title);
        record.setDate(new Date(date));
        record.setSolved(isSolved !=0);

        return record;
    }
}
