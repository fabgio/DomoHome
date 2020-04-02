package com.fabiani.domohome;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

//singleton class

public class Dashboard {
	private static final String TAG = "Dashboard";
	private static Dashboard sDashboard;
	private static final String JSON_FILENAME = "commands.json";
	private JSONSerializer mJSONSerializer;
	private List<Command> mCommands;

	private Dashboard(Context appContext) {
		mJSONSerializer = new JSONSerializer(appContext, JSON_FILENAME);
		//commands loading....
		try {
			mCommands = mJSONSerializer.loadCommands();
			Log.i(TAG, "Commands loaded  ");;
		} catch (Exception e) {
			mCommands = new ArrayList<>();
			Log.i(TAG, "commands not  loaded  ");
		}
		//Preferences loading....
		SettingsFragment.sIp = PreferenceManager.getDefaultSharedPreferences(appContext)
				.getString(SettingsFragment.IP_KEY, SettingsFragment.sAddressInput);
		SettingsFragment.sPasswordOpen = PreferenceManager.getDefaultSharedPreferences(appContext)
				.getInt(SettingsFragment.PASSWORD_OPEN_KEY, 0);
		SettingsFragment.isIpValid = PreferenceManager.getDefaultSharedPreferences(appContext)
				.getBoolean(SettingsFragment.EXTRA_IP_IS_VALID, SettingsFragment.isIpValid);
		SettingsFragment.isPassordOpenValid = PreferenceManager.getDefaultSharedPreferences(appContext)
				.getBoolean(SettingsFragment.EXTRA_PASSWORD_OPEN_IS_VALID, SettingsFragment.isPassordOpenValid);
	}

	public static Dashboard get(Context c) {
		if (sDashboard == null)
			sDashboard = new Dashboard(c.getApplicationContext());
		return sDashboard;
	}

	public List<Command> getCommands() {
		return mCommands;
	}

	public Command getCommand(UUID id) {
		Optional<Command>found=mCommands.stream().filter(command->command.getId().equals(id)).findFirst();
		return found.orElse(null);
	}

	public void addCommand(Command c) {
			mCommands.add(c);
	}


	public void deleteCommand(Command c) {
		mCommands.remove(c);
	}

	public void saveCommands() {
		try {
			mJSONSerializer.saveCommands((ArrayList<Command>) mCommands);
			Log.i(TAG, "Commands saved");
		} catch (Exception e) {
			Log.i(TAG, "Error saving commands");
		}
	}
}

