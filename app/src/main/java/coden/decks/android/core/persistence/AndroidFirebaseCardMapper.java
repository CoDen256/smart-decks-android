package coden.decks.android.core.persistence;

import com.google.firebase.firestore.DocumentSnapshot;

import javax.inject.Inject;

import coden.decks.core.data.Card;
import coden.decks.core.firebase.FirebaseCard;
import coden.decks.core.persistence.CardMapper;

/**
 * The {@code AndroidFirebaseCardMapper} is a mapper to map {@link DocumentSnapshot} to {@link Card}
 */

public class AndroidFirebaseCardMapper implements CardMapper<DocumentSnapshot> {
    @Inject
    public AndroidFirebaseCardMapper() {
    }

    @Override
    public Card toCard(DocumentSnapshot snapshot) {
        return snapshot.toObject(FirebaseCard.class);
    }
}
