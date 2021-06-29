package org.openhds.mobile.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ConfigUtils {
	
	public static final String FORM_ID = "form_id";

	public static String getResourceString(Context context, int id) {
		return (context.getString(id));
	}

	public static String getPreferenceString(Context context, int key,
			String defaultValue) {
		String keyString = getResourceString(context, key);
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getString(keyString, defaultValue);
	}

	public static String getPreferenceString(Context context, String key,
			String defaultValue) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getString(key, defaultValue);
	}
}
