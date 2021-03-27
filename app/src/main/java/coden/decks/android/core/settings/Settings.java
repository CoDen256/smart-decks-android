package coden.decks.android.core.settings;

import android.content.SharedPreferences;

import coden.decks.core.user.User;

public interface Settings {

    User getUser();

    boolean isDefaultUser();

    void registerListener(SharedPreferences.OnSharedPreferenceChangeListener listener);
}
