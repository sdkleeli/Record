package com.swufe.record;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

public class RecordFragment extends Fragment {

    private static final String ARG_RECORD_ID="record_id";
    private static final String DIALOG_DATE = "DialogDate";

    private static final  int REQUEST_DATE = 0;

    private Record mRecord;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSlovedCheckBox;

    public static RecordFragment newInstance(UUID recordId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_RECORD_ID,recordId);

        RecordFragment fragment = new RecordFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID recordId = (UUID)getArguments().getSerializable(ARG_RECORD_ID);

        mRecord = RecordLab.get(getActivity()).getRecord(recordId);
    }

        //实例化fragment布局
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
        Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_record, container, false);

        mTitleField = (EditText)v.findViewById(R.id.record_title);
        mTitleField.setText(mRecord.getTitle());
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

        mDateButton = (Button)v.findViewById(R.id.record_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mRecord.getDate());
                dialog.setTargetFragment(RecordFragment.this,REQUEST_DATE);
                dialog.show(manager,DIALOG_DATE);
            }
        });

        mSlovedCheckBox = (CheckBox)v.findViewById(R.id.record_solved);
        mSlovedCheckBox.setChecked(mRecord.isSloved());
        mSlovedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mRecord.setSolved(isChecked);
            }
        });

        return v;
    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode!=Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_DATE){
            Date date = (Date)data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mRecord.setDate(date);
            updateDate();
        }
    }

    private void updateDate() {
        mDateButton.setText(mRecord.getDate().toString());
    }
}
