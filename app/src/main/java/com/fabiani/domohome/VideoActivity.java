package com.fabiani.domohome;

import android.app.Fragment;


public class VideoActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return VideoFragment.newInstance();
    }

}

