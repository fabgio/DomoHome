package com.fabiani.domohome;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ToggleButton;

import java.util.List;

/**
 * Created by Giovanni on 26/12/2014.
 */
public  class CommandGridFragment extends Fragment {
    //TODO: CoordinatorLayout
    static final String TAG = "CommandGridFragment";
    private Command mCommand;
    private List<Command> mCommands;
    private GridView mGridView;
    private CommandAdapter mCommandAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mCommands = Dashboard.get(getActivity()).getCommands();
        mCommands.removeIf(command -> command.getTitle()==null);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_grid, parent, false);
        Toolbar mToolbar = v.findViewById(R.id.tool_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mGridView = v.findViewById(R.id.gridView);
        setHasOptionsMenu(true);
        registerForContextMenu(mGridView);
        setupAdapter();
        FloatingActionButton addCommandButton = v.findViewById(R.id.add_button);
        addCommandButton.setOnClickListener(fab -> newCommandSelected());
        return v;
    }

    public void setupAdapter() {
        if (mCommands != null) {
            mCommandAdapter = new CommandAdapter(mCommands);
            mGridView.setAdapter(mCommandAdapter);
        } else mGridView.setAdapter(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_grid_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
            case R.id.menu_item_video:
                startActivity(new Intent(getActivity(), VideoActivity.class));
                break;
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.fragment_grid_context_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        mCommand = mCommandAdapter.getItem(position);
        switch (item.getItemId()) {
            case R.id.menu_item_edit_command:
                Intent i = new Intent(getActivity(), CommandActivity.class);
                i.putExtra(CommandFragment.EXTRA_COMMAND_ID, mCommand.getId());
                startActivity(i);
                break;
            case R.id.menu_item_delete_command:
                Dashboard.get(getActivity()).deleteCommand(mCommand);
                mCommandAdapter.notifyDataSetChanged();
                Dashboard.get(getActivity()).saveCommands();
                break;
        }
        return true;
    }

    public class CommandAdapter extends ArrayAdapter<Command> {
        CommandAdapter(List<Command> commands) {
            super(getActivity(), 0, commands);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_grid_item, parent, false);
            ToggleButton mItemToggleButton = convertView.findViewById(R.id.command_grid_item_toggleButton);
            Command mCommand = getItem(position);
            mItemToggleButton.setText(mCommand.getTitle());
            mItemToggleButton.setTextOff(mCommand.getTitle());
            mItemToggleButton.setTextOn(mCommand.getTitle());
            mItemToggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                mCommand.setWhat(isChecked ? 1 : 0);
                if (SettingsFragment.isNetworkActiveConnected(getActivity()) && SettingsFragment.isIpValid
                        &&(SettingsFragment.isPassordOpenValid))
                        new InviaCommand(mCommand).execute();
            });
            mItemToggleButton.setOnLongClickListener((View v) -> {
                getActivity().openContextMenu(v);
                return true;
            });
            return convertView;
        }
    }

    private void newCommandSelected() {
        mCommand = new Command();
        Dashboard.get(getActivity()).addCommand(mCommand);
        Intent i = new Intent(getActivity(), CommandActivity.class);
        i.putExtra(CommandFragment.EXTRA_COMMAND_ID, mCommand.getId());
        startActivity(i);
    }
}