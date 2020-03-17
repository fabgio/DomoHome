package com.fabiani.domohome.controller;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.fabiani.domohome.R;
import com.fabiani.domohome.model.Command;
import com.fabiani.domohome.model.Dashboard;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class CommandFragment extends Fragment {
	static final String TAG = "CommandFragment";
	static final String EXTRA_COMMAND_ID = "com.fabiani.domohome.app_extra_command_id";
	private static String sWhoSelected;
	private static int sWhereSelected;
	private static int sTimeOutSelected;
	private Spinner mWhoSpinner;
	private Spinner mWhereSpinner;
	private Spinner mTimeOutSpinner;
	private Command mCommand;
	private Set<String> mTitles;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCommand = new Command();
		UUID commandId = (UUID) getArguments().getSerializable(EXTRA_COMMAND_ID);
		mCommand = Dashboard.get(getActivity()).getCommand(commandId);
		mTitles = Dashboard.get(getActivity()).getCommands()
				.stream()
				.map(Command::getTitle)
				.collect(Collectors.toSet());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_command, parent, false);
		Toolbar mToolbar = v.findViewById(R.id.tool_bar);
		((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
		//noinspection ConstantConditions
		((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		EditText mCommandTitleEditText = v.findViewById(R.id.command_title_edit_text);
		mCommandTitleEditText.setText(mCommand.getTitle());
		mCommandTitleEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count)  {
			}
			@Override
			public void afterTextChanged(Editable s) {
				String titleCandidate=s.toString().toLowerCase();
				if(titleCandidate.startsWith(" ")) {
					s.clear();
					return;
				}
				if(titleCandidate.trim().isEmpty()) {
					s.clear();
					mCommand.setTitle(null);
					return;
				}
				if(!mTitles.contains(titleCandidate))
					mCommand.setTitle(titleCandidate);
				else {
					Toast.makeText(getActivity(), toCamelCase(titleCandidate) + " "
							+ getString(R.string.command_title_duplicate), Toast.LENGTH_LONG).show();
					mCommand.setTitle(null);
				}
			}
		});

		mWhoSpinner = v.findViewById(R.id.command_who_spinner);
		ArrayAdapter<CharSequence> mWhoAdapter = ArrayAdapter
				.createFromResource(getActivity(), R.array.who_array, android.R.layout.simple_spinner_item);
		mWhoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mWhoSpinner.setAdapter(mWhoAdapter);
		mWhoSpinner.setSelection(mCommand.getWho());
		mWhoSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {

				sWhoSelected = mWhoSpinner.getItemAtPosition(position).toString();
				switch (sWhoSelected) {
					//check mCommand settings!  Check OK
					case "Scenarios":
						mCommand.setWho(Command.WhoChoice.SCENARIOS.getValue());
						break;
					case "Lighting":
						mCommand.setWho(Command.WhoChoice.LIGHTING.getValue());
						break;
					case "Automatism":
						mCommand.setWho(Command.WhoChoice.AUTOMATISM.getValue());
						break;
					case "MH200N scenarios":
						mCommand.setWho(Command.WhoChoice.MH200N_SCENARIOS.getValue());
						break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		mWhereSpinner = v.findViewById(R.id.command_where_spinner);
		ArrayAdapter<Integer> mWhereAdapter = new ArrayAdapter<>
				(getActivity(), android.R.layout.simple_spinner_item, Command.sWhereChoices);
		mWhereSpinner.setAdapter(mWhereAdapter);
		mWhereSpinner.setSelection(mCommand.getWhere()-1);
		mWhereSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				sWhereSelected = (int) mWhereSpinner.getItemAtPosition(position);
				mCommand.setWhere(Command.sWhereChoices[(sWhereSelected) - 1]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		mTimeOutSpinner=v.findViewById(R.id.command_timeout_spinner);
		ArrayAdapter<Integer> mTimeoutAdapter = new ArrayAdapter<>
				(getActivity(), android.R.layout.simple_spinner_item, Command.sTimeOutChoices);
		mTimeOutSpinner.setAdapter(mTimeoutAdapter);
		mTimeOutSpinner.setSelection(mCommand.getTimeout());
		mTimeOutSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				sTimeOutSelected=(int)mTimeOutSpinner.getItemAtPosition(position);
				mCommand.setTimeout(Command.sTimeOutChoices[sTimeOutSelected]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		return v;
	}

	public static CommandFragment newInstance(UUID commandId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_COMMAND_ID, commandId);
		CommandFragment fragment = new CommandFragment();
		fragment.setArguments(args);
		return fragment;
	}

	private  String toCamelCase(String input){
		char[] array=input.toCharArray();
		StringBuilder output=new StringBuilder();
		char camelCase=Character.toUpperCase(array[0]);
		output.append(camelCase);
		for(int i=1;i<array.length;i++)
			output.append(array[i]);
		return output.toString();
	}

	@Override
	public void onStop(){
		super.onStop();
		if(mCommand.getTitle()!=null)
			Dashboard.get(getActivity()).saveCommands();
	}
}
