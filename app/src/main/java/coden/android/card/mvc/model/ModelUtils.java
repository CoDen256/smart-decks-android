package coden.android.card.mvc.model;

import android.content.SharedPreferences;
import android.view.View;

import androidx.preference.PreferenceManager;

import java.io.IOException;
import java.io.InputStream;

import coden.android.card.R;
import coden.android.card.mvc.persistence.AndroidFirebase;
import coden.cards.model.Model;
import coden.cards.persistence.Database;
import coden.cards.reminder.BaseReminder;
import coden.cards.reminder.Reminder;
import coden.cards.user.User;
import coden.cards.user.UserEntry;

public class ModelUtils {


    private static Model mModel;
    private static Database mDatabase;
    private static BaseReminder mBaseReminder;

    public static Model getModel(View view){
        if (mModel == null){
            mModel = createModel(getUser(view), getReminder(view), getDatabase(view));
        }
        return mModel;
    }

    public static Model getModel(User user, BaseReminder reminder, Database database){
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

    public static BaseReminder getReminder(View view){
        if (mBaseReminder == null){
            mBaseReminder = createReminder(view);
        }
        return mBaseReminder;
    }

    private static Model createModel(User user, BaseReminder reminder, Database database) {
        return new LazyModel(user, reminder, database);
    }

    private static Database createDatabase(View view) {
        InputStream firebaseConfig = view.getResources().openRawResource(R.raw.firebase);
        return new AndroidFirebase(firebaseConfig);
    }

    private static BaseReminder createReminder(View view) {
        InputStream reminderConfig = view.getResources().openRawResource(R.raw.reminder);
        try{
            return new Reminder(reminderConfig);
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
