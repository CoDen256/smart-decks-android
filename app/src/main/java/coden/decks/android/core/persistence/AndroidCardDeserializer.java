package coden.decks.android.core.persistence;

import com.google.firebase.firestore.DocumentSnapshot;

import coden.decks.core.data.Card;
import coden.decks.core.data.CardDeserializer;
import coden.decks.core.firebase.card.FirebaseCard;

/**
 * The {@code AndroidFirebaseCardMapper} is a mapper to map {@link DocumentSnapshot} to {@link Card}
 */

public class AndroidCardDeserializer implements CardDeserializer<DocumentSnapshot>{

    @Override
    public Card deserialize(DocumentSnapshot snapshot) {
        return snapshot.toObject(FirebaseCard.class);
    }

}
