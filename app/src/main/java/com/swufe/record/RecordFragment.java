package com.swufe.record;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.Date;
import java.util.UUID;

public class RecordFragment extends Fragment {

    private static final String ARG_RECORD_ID="record_id";
    private static final String DIALOG_DATE = "DialogDate";

    private static final  int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO=1;
    private static final int REQUEST_TIME=2;

    private Record mRecord;
    private File mPhotoFile;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSlovedCheckBox;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

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
        setHasOptionsMenu(true);
        mPhotoFile = RecordLab.get(getActivity()).getPhotoFile(mRecord);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.delete_record, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_record:
                RecordLab.get(getActivity()).deleteRecord(mRecord);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause(){
        super.onPause();

        RecordLab.get(getActivity())
                .updateRecord(mRecord);
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

        mTimeButton = (Button) v.findViewById( R.id.record_time);
        mTimeButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance( mRecord.getHour(),mRecord.getMinute());
                dialog.setTargetFragment( RecordFragment.this,REQUEST_TIME);
                dialog.show(fragmentManager,DIALOG_DATE );
            }
        } );

        mSlovedCheckBox = (CheckBox)v.findViewById(R.id.record_solved);
        mSlovedCheckBox.setChecked(mRecord.isSloved());
        mSlovedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mRecord.setSolved(isChecked);
            }
        });

        mPhotoButton = (ImageButton)v.findViewById(R.id.record_camera);
        final  Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        PackageManager packageManager = getActivity().getPackageManager();
        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        if(canTakePhoto){
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage,REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView)v.findViewById(R.id.record_photo);
        updatePhotoView();

        return v;
    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode!=Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_DATE){
            Date date = (Date)data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mRecord.setDate(date);
            updateDate();
        }if(requestCode == REQUEST_TIME){
            int hour = (int) data.getSerializableExtra( TimePickerFragment.EXTRA_HOUR );
            int minute = (int) data.getSerializableExtra( TimePickerFragment.EXTRA_MINUTE );
            mRecord.setHour( hour );
            mRecord.setMinute( minute );
            updateTime();
        }if(requestCode == REQUEST_PHOTO){
            updatePhotoView();
        }
    }

    private void updateTime(){
        mTimeButton.setText(mRecord.getHour()+":"+mRecord.getMinute());
    }
    private void updateDate() {
        String mmDate = (String) DateFormat.format("EEEE, MMMM dd, yyyy kk:mm",mRecord.getDate());
        mDateButton.setText(mmDate);
    }

    private void updatePhotoView(){
        if(mPhotoFile == null || !mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
        }else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(),getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }
}
