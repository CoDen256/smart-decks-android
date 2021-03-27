package coden.decks.android.core.module;

import android.content.SharedPreferences;

import coden.decks.android.core.settings.Settings;
import coden.decks.core.model.Decks;
import coden.decks.core.persistence.Database;
import coden.decks.core.revision.RevisionManager;

import static coden.decks.android.core.settings.AndroidSettings.USERNAME;

public class AndroidDecks extends Decks implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final Settings mSettings;

    public AndroidDecks(Settings settings, RevisionManager revisor, Database database) {
        super(settings.getUser(), revisor, database);
        settings.registerListener(this);
        mSettings = settings;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(USERNAME)){
            setUser(mSettings.getUser());
        }
    }
}
