package coden.decks.android.mvc.model;

import android.content.SharedPreferences;
import android.view.View;

import androidx.preference.PreferenceManager;

import java.io.IOException;
import java.io.InputStream;

import coden.decks.android.R;
import coden.decks.android.mvc.persistence.AndroidFirebase;
import coden.decks.core.firebase.FirebaseConfig;
import coden.decks.core.model.DecksModel;
import coden.decks.core.model.Decks;
import coden.decks.core.persistence.Database;
import coden.decks.core.revision.RevisionManagerImpl;
import coden.decks.core.revision.RevisionManager;
import coden.decks.core.user.User;
import coden.decks.core.user.UserEntry;

public class ModelUtils {


    private static DecksModel mModel;
    private static Database mDatabase;
    private static RevisionManager mBaseReminder;

    public static DecksModel getModel(View view){
        if (mModel == null){
            mModel = createModel(getUser(view), getReminder(view), getDatabase(view));
        }
        return mModel;
    }

    public static DecksModel getModel(User user, RevisionManager reminder, Database database){
        if (mModel == null){
            mModel = createModel(user, reminder, database);
        }
        return mModel;
    }


    public static Database getDatabase(View view){
        if (mDatabase == null){
            mDatabase = createDatabase(view);
        }
        return mDatabase;
    }

    public static RevisionManager getReminder(View view){
        if (mBaseReminder == null){
            mBaseReminder = createReminder(view);
        }
        return mBaseReminder;
    }

    private static DecksModel createModel(User user, RevisionManager reminder, Database database) {
        return new Decks(user, reminder, database);
    }

    private static Database createDatabase(View view) {
        InputStream firebaseConfig = view.getResources().openRawResource(R.raw.firebase);
        return new AndroidFirebase(new FirebaseConfig(firebaseConfig));
    }

    private static RevisionManager createReminder(View view) {
        InputStream reminderConfig = view.getResources().openRawResource(R.raw.revision);
        try{
            return new RevisionManagerImpl(reminderConfig);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot create a reminder. Check the config on syntax errors", e);
        }

    }
    public static User getUser(View view) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(view.getContext());
        String username = sharedPreferences.getString("username", view.getResources().getString(R.string.default_user_name));
        return new UserEntry(username);
    }
}
