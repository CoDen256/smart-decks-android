package coden.decks.android.core.module;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import javax.inject.Singleton;

import coden.decks.android.R;
import coden.decks.android.app.App;
import coden.decks.android.core.persistence.AndroidCardDeserializer;
import coden.decks.android.core.persistence.AndroidFirebase;
import coden.decks.android.core.persistence.AndroidUserDeserializer;
import coden.decks.android.core.settings.AndroidSettings;
import coden.decks.android.core.settings.Settings;
import coden.decks.core.data.CardDeserializer;
import coden.decks.core.firebase.config.FirebaseConfig;
import coden.decks.core.model.Decks;
import coden.decks.core.model.DecksModel;
import coden.decks.core.persistence.Database;
import coden.decks.core.revision.RevisionLevel;
import coden.decks.core.revision.RevisionManager;
import coden.decks.core.revision.RevisionManagerImpl;
import coden.decks.core.revision.config.RevisionLevels;
import coden.decks.core.user.UserDeserializer;
import dagger.Module;
import dagger.Provides;

@Module
public class CoreModule {

    @Singleton
    @Provides
    public DecksModel provideDecksModel(Settings settings, Database database, RevisionManager manager) {
        return new Decks(settings.getUser(), manager, database);
    }

    @Singleton
    @Provides
    public Database providesDatabase(CardDeserializer<DocumentSnapshot> cardDeserializer,
                                     UserDeserializer<DocumentSnapshot> userDeserializer,
                                     FirebaseConfig firebaseConfig) {
        return new AndroidFirebase(cardDeserializer, userDeserializer, firebaseConfig);
    }

    @Singleton
    @Provides
    public FirebaseConfig providesFirebaseConfig(){
        try {
            return new FirebaseConfig(App.getRes().openRawResource(R.raw.firebase));
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Firebase configuration file not found");
        }
    }


    @Singleton
    @Provides
    public UserDeserializer<DocumentSnapshot> providesUserDeserializer() {
        return new AndroidUserDeserializer();
    }

    @Singleton
    @Provides
    public CardDeserializer<DocumentSnapshot> providesCardDeserializer() {
        return new AndroidCardDeserializer();
    }

    @Singleton
    @Provides
    public RevisionManager providesRevisionManager(Set<RevisionLevel> levels) {
        return new RevisionManagerImpl(levels);
    }

    @Singleton
    @Provides
    public Set<RevisionLevel> providesLevels() {
        try {
            return new RevisionLevels(App.getRes().openRawResource(R.raw.revision));
        } catch (IOException e) {
            throw new IllegalStateException("Syntax error in revision configuration file");
        }
    }

    @Singleton
    @Provides
    public Settings providesSettings() {
        return new AndroidSettings();
    }
}
