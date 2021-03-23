package coden.decks.android.core;

import javax.inject.Singleton;

import coden.decks.android.core.module.CoreModule;
import coden.decks.android.core.settings.Settings;
import coden.decks.android.ui.home.view.HomeFragment;
import coden.decks.android.ui.pending.PendingCardFragment;
import coden.decks.core.model.DecksModel;
import coden.decks.core.revision.RevisionManager;
import dagger.Component;

@Singleton
@Component(modules = CoreModule.class)
public interface CoreApplicationComponent {
    DecksModel decksModel();
    Settings settings();
    RevisionManager revisor();

    void inject(HomeFragment homeFragment);
    void inject(PendingCardFragment pendingCardFragment);
}
