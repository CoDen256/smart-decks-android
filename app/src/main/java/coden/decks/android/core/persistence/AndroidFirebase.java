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

import coden.decks.core.data.Card;
import coden.decks.core.data.CardDeserializer;
import coden.decks.core.persistence.Database;
import coden.decks.core.firebase.config.FirebaseConfig;
import coden.decks.core.user.User;
import coden.decks.core.user.UserDeserializer;
import coden.decks.core.user.UserNotProvidedException;

import static java.util.Objects.requireNonNull;

/**
 * Represents a firebase used in android implementing {@link Database}
 */
public class AndroidFirebase implements Database {

    private final FirebaseConfig config;
    private final FirebaseFirestore firestore;
    private final CardDeserializer<DocumentSnapshot> cardDeserializer;
    private final UserDeserializer<DocumentSnapshot> userDeserializer;

    private User user;
    private CollectionReference deck;

    public AndroidFirebase(CardDeserializer<DocumentSnapshot> cardDeserializer, UserDeserializer<DocumentSnapshot> userDeserializer,
            FirebaseConfig config) {
        this.cardDeserializer = requireNonNull(cardDeserializer);
        this.userDeserializer = requireNonNull(userDeserializer);
        this.config = requireNonNull(config);
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
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        if (!Objects.equals(requireNonNull(user), this.user)){
            this.user = user;
            this.deck = null;
        }
    }

    @Override
    public CompletableFuture<Stream<User>> getAllUsers() {
        Task<QuerySnapshot> getAllUsersFuture = firestore.collection(config.userCollection).get();
        return createCompletableFuture(getAllUsersFuture)
                .thenApply(this::asUsers);
    }

    @Override
    public CompletableFuture<Stream<Card>> getAllEntries() throws UserNotProvidedException {
        final Task<QuerySnapshot> getAllEntriesFuture = getDeck().get();
        return createCompletableFuture(getAllEntriesFuture).
                thenApply(this::asCards);
    }


    @Override
    public CompletableFuture<Stream<Card>> getGreaterOrEqualLevel(int level) throws UserNotProvidedException {
        final Task<QuerySnapshot> getGreaterOrEqualLevelFuture = getDeck()
                .whereGreaterThanOrEqualTo("level", level)
                .get();
        return createCompletableFuture(getGreaterOrEqualLevelFuture)
                .thenApply(this::asCards);
    }

    @Override
    public CompletableFuture<Stream<Card>> getLessOrEqualLevel(int level) {
        final Task<QuerySnapshot> getLessOrEqualLevelFuture = getDeck()
                .whereLessThanOrEqualTo("level", level)
                .get();
        return createCompletableFuture(getLessOrEqualLevelFuture)
                .thenApply(this::asCards);
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

    private <T> CompletableFuture<T> createCompletableFuture(Task<T> task) {
        final CompletableFuture<T> completableFuture = new CompletableFuture<>();
        task.addOnSuccessListener(completableFuture::complete);
        task.addOnFailureListener(completableFuture::completeExceptionally);
        task.addOnCanceledListener(() -> completableFuture.cancel(true));
        return completableFuture;
    }

    private CollectionReference getDeck() throws UserNotProvidedException {
        if (deck == null) {
            deck = createCollection();
        }
        return deck;
    }

    private Stream<Card> asCards(QuerySnapshot snapshot) {
        return snapshot.getDocuments()
                .stream()
                .map(cardDeserializer::deserialize);
    }


    private Stream<User> asUsers(QuerySnapshot snapshot) {
        return snapshot.getDocuments()
                .stream()
                .map(userDeserializer::deserialize);
    }

    @Override
    public void close() throws IOException {
        // no-op
    }
}
