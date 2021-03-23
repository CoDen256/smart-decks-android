package coden.decks.android.core.module;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.IOException;

import javax.inject.Singleton;

import coden.decks.android.R;
import coden.decks.android.app.App;
import coden.decks.android.core.persistence.AndroidFirebase;
import coden.decks.android.core.persistence.AndroidFirebaseCardMapper;
import coden.decks.android.core.settings.AndroidSettings;
import coden.decks.android.core.settings.Settings;
import coden.decks.core.firebase.FirebaseConfig;
import coden.decks.core.model.Decks;
import coden.decks.core.model.DecksModel;
import coden.decks.core.persistence.CardMapper;
import coden.decks.core.persistence.Database;
import coden.decks.core.revision.RevisionManager;
import coden.decks.core.revision.RevisionManagerImpl;
import dagger.Module;
import dagger.Provides;

@Module
public class CoreModule {

    @Singleton
    @Provides
    public DecksModel provideDecksModel(Settings settings, Database database, RevisionManager manager){
        return new Decks(settings.getUser(), manager, database);
    }

    @Singleton
    @Provides
    public Database providesDatabase(CardMapper<DocumentSnapshot> mapper, FirebaseConfig firebaseConfig){
        return new AndroidFirebase(mapper, firebaseConfig);
    }

    @Singleton
    @Provides
    public FirebaseConfig providesFirebaseConfig(){
        return new FirebaseConfig(App.getRes().openRawResource(R.raw.firebase));
    }

    @Singleton
    @Provides
    public CardMapper<DocumentSnapshot> providesCardMapper(){
        return new AndroidFirebaseCardMapper();
    }

    @Singleton
    @Provides
    public RevisionManager providesRevisionManager() {
        try {
            return new RevisionManagerImpl(App.getRes().openRawResource(R.raw.revision));
        } catch (IOException e) {
            throw new IllegalStateException("Syntax error in revision configuration file");
        }
    }

    @Singleton
    @Provides
    public Settings providesSettings(){
        return new AndroidSettings();
    }
}
