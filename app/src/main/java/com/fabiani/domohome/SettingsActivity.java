package com.fabiani.domohome;

import android.app.Fragment;

public class SettingsActivity extends SingleFragmentActivity {

   @Override
   protected Fragment createFragment() {
       return  SettingsFragment.newInstance();
   }

}
