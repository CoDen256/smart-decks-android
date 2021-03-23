package coden.decks.android.core.settings;

import coden.decks.core.user.User;

public interface Settings {
    User getUser();
    boolean isDefaultUser();
}
