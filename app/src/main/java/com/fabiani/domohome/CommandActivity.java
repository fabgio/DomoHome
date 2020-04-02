package com.fabiani.domohome;

import java.util.UUID;

public class CommandActivity extends SingleFragmentActivity {
   @Override
   protected CommandFragment createFragment() {
       UUID commandId=(UUID)getIntent().getSerializableExtra(CommandFragment.EXTRA_COMMAND_ID);
       return  CommandFragment.newInstance(commandId);
   }

}
