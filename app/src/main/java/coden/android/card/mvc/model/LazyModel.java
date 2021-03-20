package coden.android.card.mvc.model;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import coden.cards.data.Card;
import coden.cards.model.SimpleModel;
import coden.cards.persistence.Database;
import coden.cards.reminder.BaseReminder;
import coden.cards.user.User;

public class LazyModel extends SimpleModel {
    public LazyModel(User user, BaseReminder reminder, Database database) {
        super(user, reminder, database);
    }

    @Override
    public CompletableFuture<Card> getNextCard() {
        return getReadyCards().thenApply(this::getNextOrNull);
    }

    private Card getNextOrNull(List<Card> cards){
        return cards.isEmpty() ? null : cards.get(0);
    }



}
