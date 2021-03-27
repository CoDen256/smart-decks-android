package coden.decks.android.core.persistence;

import com.google.firebase.firestore.DocumentSnapshot;

import coden.decks.core.user.User;
import coden.decks.core.user.UserDeserializer;
import coden.decks.core.user.UserEntry;

/**
 * Android implementation of user deserializer to be used in firebase. It converts
 * the given {@link DocumentSnapshot} to the user
 *
 * @author Denys Chernyshov
 */
public class AndroidUserDeserializer implements UserDeserializer<DocumentSnapshot> {
    @Override
    public User deserialize(DocumentSnapshot documentSnapshot) {
        return new UserEntry(documentSnapshot.getId());
    }
}
