package coden.decks.android.core.persistence;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import javax.inject.Inject;

import coden.decks.core.data.Card;
import coden.decks.core.persistence.CardMapper;
import coden.decks.core.persistence.Database;
import coden.decks.core.firebase.FirebaseConfig;
import coden.decks.core.user.User;
import coden.decks.core.user.UserNotProvidedException;

/**
 * Represents a firebase used in android implementing {@link Database}
 */
public class AndroidFirebase implements Database {

    private final FirebaseConfig config;
    private final FirebaseFirestore firestore;
    private final CardMapper<DocumentSnapshot> mapper;

    private User user;
    private CollectionReference deck;

    public AndroidFirebase(CardMapper<DocumentSnapshot> mapper, FirebaseConfig config) {
        this.mapper = Objects.requireNonNull(mapper);
        this.config = Objects.requireNonNull(config);
        this.firestore = createFirestore();
    }

    private FirebaseFirestore createFirestore() {
        return FirebaseFirestore.getInstance();
    }

    private CollectionReference createCollection() {
        if (user == null) throw new UserNotProvidedException();
        return firestore.collection(this.config.userCollection)
                .document(this.user.getName())
                .collection(this.config.deckCollection);
    }

    @Override
    public void setUser(User user) {
        this.user = Objects.requireNonNull(user);
        this.deck = null;
    }

    @Override
    public CompletableFuture<Stream<Card>> getAllEntries() throws UserNotProvidedException {
        final Task<QuerySnapshot> getAllEntriesFuture = getDeck().get();
        return createCompletableFuture(getAllEntriesFuture).
                thenApply(this::fetchDocumentsAsFirebaseCardEntries);
    }


    @Override
    public CompletableFuture<Stream<Card>> getGreaterOrEqualLevel(int level) throws UserNotProvidedException {
        final Task<QuerySnapshot> getGreaterOrEqualLevelFuture = getDeck()
                .whereGreaterThanOrEqualTo("level", level)
                .get();
        return createCompletableFuture(getGreaterOrEqualLevelFuture)
                .thenApply(this::fetchDocumentsAsFirebaseCardEntries);
    }

    @Override
    public CompletableFuture<Stream<Card>> getLessOrEqualLevel(int level) {
        final Task<QuerySnapshot> getLessOrEqualLevelFuture = getDeck()
                .whereLessThanOrEqualTo("level", level)
                .get();
        return createCompletableFuture(getLessOrEqualLevelFuture)
                .thenApply(this::fetchDocumentsAsFirebaseCardEntries);
    }

    @Override
    public CompletableFuture<Void> deleteEntry(Card entry) {
        final Task<Void> deleteFuture = getDeck()
                .document(entry.getFrontSide())
                .delete();
        return createCompletableFuture(deleteFuture)
                .thenApply(writeResult -> null);
    }

    @Override
    public CompletableFuture<Void> addOrUpdateEntry(Card entry) {
        final Task<Void> addOrUpdateFuture = getDeck()
                .document(entry.getFrontSide())
                .set(entry);
        return createCompletableFuture(addOrUpdateFuture)
                .thenApply(writeResult -> null);
    }

    private CollectionReference getDeck() throws UserNotProvidedException {
        if (deck == null) {
            deck = createCollection();
        }
        return deck;
    }

    private <T> CompletableFuture<T> createCompletableFuture(Task<T> task) {
        final CompletableFuture<T> completableFuture = new CompletableFuture<>();
        task.addOnSuccessListener(completableFuture::complete);
        task.addOnFailureListener(completableFuture::completeExceptionally);
        task.addOnCanceledListener(() -> completableFuture.cancel(true));
        return completableFuture;
    }

    private Stream<Card> fetchDocumentsAsFirebaseCardEntries(QuerySnapshot snapshot) {
        return snapshot.getDocuments()
                .stream()
                .map(mapper::toCard);
    }

    @Override
    public void close() throws IOException {
        // no-op
    }
}
