package coden.android.card.mvc.persistence;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import coden.core.decks.data.Card;
import coden.core.decks.persistence.Database;
import coden.core.decks.persistence.firebase.FirebaseCard;
import coden.core.decks.persistence.firebase.FirebaseConfig;
import coden.core.decks.user.User;
import coden.core.decks.user.UserNotProvidedException;

public class AndroidFirebase implements Database {

        private final FirebaseConfig config;
        private final FirebaseFirestore firestore;

        private User user;
        private CollectionReference cards;

        public AndroidFirebase(InputStream config){
            Objects.requireNonNull(config);
            this.config = new FirebaseConfig(config);
            this.firestore = createFirestore();
        }

        private FirebaseFirestore createFirestore() {
            return FirebaseFirestore.getInstance();
        }

        private CollectionReference createCollection() {
            return firestore.collection(this.config.userCollection)
                    .document(this.user.getName())
                    .collection(this.config.mainCollection);
        }

        @Override
        public void setUser(User user) {
            this.user = Objects.requireNonNull(user);
            this.cards = createCollection();
        }

        @Override
        public CompletableFuture<Stream<Card>> getAllEntries() throws UserNotProvidedException {
            final Task<QuerySnapshot> getAllEntriesFuture = getCards().get();
            return createCompletableFuture(getAllEntriesFuture).
                    thenApply(this::fetchDocumentsAsFirebaseCardEntries);
        }


        @Override
        public CompletableFuture<Stream<Card>> getGreaterOrEqualLevel(int level) throws UserNotProvidedException {
            final Task<QuerySnapshot> getGreaterOrEqualLevelFuture = getCards()
                    .whereGreaterThanOrEqualTo("level", level)
                    .get();
            return createCompletableFuture(getGreaterOrEqualLevelFuture)
                    .thenApply(this::fetchDocumentsAsFirebaseCardEntries);
        }

        @Override
        public CompletableFuture<Stream<Card>> getLessOrEqualLevel(int level) {
            final Task<QuerySnapshot> getLessOrEqualLevelFuture = getCards()
                    .whereLessThanOrEqualTo("level", level)
                    .get();
            return createCompletableFuture(getLessOrEqualLevelFuture)
                    .thenApply(this::fetchDocumentsAsFirebaseCardEntries);
        }

        @Override
        public CompletableFuture<Void> deleteEntry(Card entry){
            final Task<Void> deleteFuture = getCards()
                    .document(entry.getFrontSide())
                    .delete();
            return createCompletableFuture(deleteFuture)
                    .thenApply(writeResult -> null);
        }

        @Override
        public CompletableFuture<Void> addOrUpdateEntry(Card entry) {
            final Task<Void> addOrUpdateFuture = getCards()
                    .document(entry.getFrontSide())
                    .set(entry);
            return createCompletableFuture(addOrUpdateFuture)
                    .thenApply(writeResult -> null);
        }

        private CollectionReference getCards() throws UserNotProvidedException {
            if (user == null) {
                throw new UserNotProvidedException();
            }
            return cards;
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
                    .map(this::toFirebaseCardEntry);
        }

        private FirebaseCard toFirebaseCardEntry(DocumentSnapshot c) {
            return c.toObject(FirebaseCard.class);
        }
}
