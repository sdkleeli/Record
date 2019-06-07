package com.swufe.record;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class RecordFragment extends Fragment {
    private Record mRecord;
    private EditText mTitleField;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mRecord = new Record();
    }

        //实例化fragment布局
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
        Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_record, container, false);

        mTitleField = (EditText)v.findViewById(R.id.record_title);
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //这个空间故意留空
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRecord.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //这个空间也故意留空
            }
        });

        return v;
    }
}
