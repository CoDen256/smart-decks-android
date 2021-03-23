package coden.decks.android.core;

import javax.inject.Singleton;

import coden.decks.android.core.module.CoreModule;
import coden.decks.android.core.settings.Settings;
import coden.decks.core.model.DecksModel;
import coden.decks.core.revision.RevisionManager;
import dagger.Component;

@Singleton
@Component(modules = CoreModule.class)
public interface CoreApplicationComponent {
    DecksModel decksModel();
    Settings settings();
    RevisionManager revisor();
}