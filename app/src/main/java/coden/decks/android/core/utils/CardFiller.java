package coden.decks.android.core.utils;

import java.util.HashMap;
import java.util.Map;

import coden.decks.core.model.DecksModel;
import coden.decks.core.user.User;

public class CardFiller {

    private static final Map<String, String> toFill = new HashMap<>();
    static {
        toFill.put("Resolution", "Auflösung");
        toFill.put("Brightness", "Helligkeit");
        toFill.put("Access Point", "Zugangspunkt");
        toFill.put("Application", "Anwendung");
        toFill.put("Directory", "Verzeichnis");
        toFill.put("Runtime", "Laufzeit");
        toFill.put("Pointer", "Zeiger");
    }

    private final DecksModel mDecksModel;

    public CardFiller(DecksModel model, User user) {
        mDecksModel = model;
        model.setUser(user);
    }

    public void removeAllCards(){
        mDecksModel.getAllCards().thenAccept(cards -> cards.forEach(mDecksModel::deleteCard));
    }

    public void addFillerCards(){
        toFill.forEach((f, b) -> mDecksModel.addCard(mDecksModel.createCard(f, b)));
    }
}
