package org.openhds.mobile.task;

import org.openhds.mobile.database.DatabaseAdapter;
import org.openhds.mobile.model.Supervisor;
import android.os.AsyncTask;

/**
 * The Login task will verify a local user exists in the database If no user
 * exist, the user must initially make a request to download forms with the
 * typed in username/password. If successfully
 */
public class SupervisorLoginTask extends AsyncTask<Boolean, Void, SupervisorLoginTask.Result> {
	private DatabaseAdapter storage;
	private String username;
	private String password;
	private Listener listener;

	public enum Result {
		AUTHENTICATED, BAD_AUTHENTICATION
	}

	public interface Listener {
		void onAuthenticated();

		void onBadAuthentication();
	}

	public SupervisorLoginTask(DatabaseAdapter storage, String user, String password,
			Listener listener) {
		this.storage = storage;
		this.username = user;
		this.password = password;
		this.listener = listener;
	}

	@Override
	protected Result doInBackground(Boolean... params) {
		Supervisor user = storage.findSupervisorByUsername(username);
		if (user != null && user.getPassword().equals(password)) {
			return Result.AUTHENTICATED;
		} else {
			return Result.BAD_AUTHENTICATION;
		}
	}

	@Override
	protected void onPostExecute(Result result) {
		switch (result) {
		case BAD_AUTHENTICATION:
			listener.onBadAuthentication();
			break;
		case AUTHENTICATED:
			listener.onAuthenticated();
			break;
		}
	}

}
