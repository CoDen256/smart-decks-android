package coden.decks.android.core.settings;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import coden.decks.android.R;
import coden.decks.android.app.App;
import coden.decks.core.user.User;
import coden.decks.core.user.UserEntry;

public class AndroidSettings implements Settings {


    private final Context applicationContext;
    private final String defaultUsername;

    private User currentUser;

    public AndroidSettings() {
        applicationContext = App.getInstance().getApplicationContext();
        defaultUsername = App.getRes().getString(R.string.default_user_name);
        currentUser = new UserEntry(getSettingsUsername());
    }

    @Override
    public User getUser() {
        String username = getSettingsUsername();
        if (currentUser == null || !username.equals(currentUser.getName())){
            currentUser = new UserEntry(username);
        }
        return currentUser;
    }

    private String getSettingsUsername() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        return sharedPreferences.getString("username", defaultUsername);
    }

    @Override
    public boolean isDefaultUser() {
        return currentUser != null && defaultUsername.equals(getSettingsUsername());
    }
}
