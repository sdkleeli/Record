package com.swufe.record;

import android.support.v4.app.Fragment;

public class RecordActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new RecordFragment();
    }
}
