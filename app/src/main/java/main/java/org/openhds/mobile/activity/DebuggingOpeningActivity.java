package org.openhds.mobile.activity;

import java.net.MalformedURLException;
import java.net.URL;

import org.openhds.mobile.R;
import org.openhds.mobile.database.DatabaseAdapter;
import org.openhds.mobile.task.HttpTask;
import org.openhds.mobile.task.HttpTask.RequestContext;
import org.openhds.mobile.task.SupervisorLoginTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class DebuggingOpeningActivity extends Activity {

	private static final String OPENHDS_URL = "http://130.111.132.88:8080/openhds";
	private static final String INTEROP_URL = "http://130.111.132.88:8080/openhds";
	private static final String SUPERVISOR_USER = "admin";
	private static final String SUPERVISOR_PASSWORD = "test";
	

	private SharedPreferences settings;
	private DatabaseAdapter databaseAdapter;
	private ProgressDialog progressDialog;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getString(R.string.app_name) + " > " + "Debug Mode Startup");

		settings = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());

		databaseAdapter = new DatabaseAdapter(this);

		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);

		// chain of async method calls results in starting UpdateActivity
		setServerURLS();
	}

	// set server URLs in preferences
	// authenticateSupervisor on success
	private void setServerURLS() {
		// should update preference management to
		// "modern fragment-based preferenceActivity"
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(getString(R.string.openhds_server_url_key),
				OPENHDS_URL);
		editor.putString(getString(R.string.interop_server_url_key),
				INTEROP_URL);
		editor.putString(OpeningActivity.USERNAME_KEY,
				SUPERVISOR_USER);
		editor.putString(OpeningActivity.PASSWORD_KEY,
				SUPERVISOR_PASSWORD);
		if (editor.commit()) {
			Toast.makeText(getApplicationContext(), "Set server URLs",
					Toast.LENGTH_LONG).show();
			authenticateSupervisor();
		} else {
			Toast.makeText(getApplicationContext(),
					"Error setting server URLs", Toast.LENGTH_LONG).show();
		}
	}

	// authenticate supervisor with server
	// logInSupervisor on success
	private void authenticateSupervisor() {
		RequestContext requestCtx = new RequestContext();
		URL serverURL;
		try {
			serverURL = new URL(OPENHDS_URL + "/api/user/authenticate");
		} catch (MalformedURLException e) {
			Toast.makeText(getApplicationContext(), "MalformedURLException.",
					Toast.LENGTH_LONG).show();
			return;
		}
		requestCtx.url(serverURL).user(SUPERVISOR_USER)
				.password(SUPERVISOR_PASSWORD);
		HttpTask<Void, Void> authenticateTask = new HttpTask<Void, Void>(
				getApplicationContext(), requestCtx, new AuthenticateListener());
		authenticateTask.execute();
	}

	// log in as supervisor, locally on device
	// sync data on success
	private void logInSupervisor() {
		SupervisorLoginTask loginTask = new SupervisorLoginTask(
				databaseAdapter, SUPERVISOR_USER, SUPERVISOR_PASSWORD,
				new LoginListener());
		loginTask.execute();
	}

	// sync data with server
	// authenticate field worker on success
	private void syncData() {
		progressDialog.setTitle("Syncing data with server.");
		progressDialog.setMessage("Do not interrupt.");
		progressDialog.show();


	}

	// async handler for authenticateSupervisor()
	private class AuthenticateListener implements HttpTask.TaskListener {
		public void onFailedAuthentication() {
			Toast.makeText(getApplicationContext(),
					"Supervisor credentials not authenticated.",
					Toast.LENGTH_LONG).show();
		}

		public void onConnectionError() {
			Toast.makeText(getApplicationContext(),
					"Connection error authenticating supervisor.",
					Toast.LENGTH_LONG).show();
		}

		public void onConnectionTimeout() {
			Toast.makeText(getApplicationContext(),
					"Connection timeout authenticating supervisor.",
					Toast.LENGTH_LONG).show();
		}

		public void onSuccess() {
			Toast.makeText(getApplicationContext(),
					"Authenticated supervisor.", Toast.LENGTH_LONG).show();
			logInSupervisor();
		}

		public void onFailure() {
			Toast.makeText(getApplicationContext(),
					"Unknown failure authenticating supervisor.",
					Toast.LENGTH_LONG).show();
		}

		public void onNoContent() {
			Toast.makeText(getApplicationContext(),
					"No content authenticating supervisor.", Toast.LENGTH_LONG)
					.show();
		}
	}

	// async handler for logInSupervisor()
	private class LoginListener implements SupervisorLoginTask.Listener {
		public void onAuthenticated() {
			Toast.makeText(getApplicationContext(), "Logged in as supervisor.",
					Toast.LENGTH_LONG).show();
			syncData();
		}

		public void onBadAuthentication() {
			Toast.makeText(getApplicationContext(),
					"Supervisor not logged in with credentials.",
					Toast.LENGTH_LONG).show();
		}
	}

	// async handler for logInFieldWorker()
//	private class LogInFieldWorkerListener implements
//			AuthenticateFieldWorker.Listener {
//		public void onAuthenticated(FieldWorker fieldWorker) {
//			Toast.makeText(getApplicationContext(),
//					"Logged in as field worker.", Toast.LENGTH_LONG).show();
//			startUpdateActivity(fieldWorker);
//		}
//	}

}
