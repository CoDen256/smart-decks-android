package coden.decks.android.ui.home.controller;


import java.util.concurrent.CompletableFuture;

import coden.decks.android.R;
import coden.decks.android.core.settings.Settings;
import coden.decks.android.ui.home.view.BaseHomeView;
import coden.decks.core.data.Card;
import coden.decks.core.model.DecksModel;

import static coden.decks.android.core.utils.FunctionalInterfaces.acceptOrCall;
import static coden.decks.android.core.utils.FunctionalInterfaces.applyOrSkip;


public class HomeController implements BaseHomeController {

    private final DecksModel mDecksModel;
    private final Settings mSettings;
    private final BaseHomeView mView;

    private CompletableFuture<Card> currentCardFuture;

    private String deferredSecondSide;

    public HomeController(DecksModel model, Settings settings, BaseHomeView view) {
        mDecksModel = model;
        mSettings = settings;
        mView = view;
    }

    @Override
    public void addNewCard(String frontSide, String backSide) {
        Card newCard = mDecksModel.createCard(frontSide, backSide);
        mDecksModel.addCard(newCard);
    }

    @Override
    public void deleteCurrentCard() {
        if (currentCardFuture == null) return;
        currentCardFuture
                .thenApply(applyOrSkip(mDecksModel::deleteCard))
                .thenRun(this::displayNextCard);
    }

    @Override
    public void setDontKnow() {
        if (currentCardFuture == null) return;
        currentCardFuture
                .thenApply(applyOrSkip(mDecksModel::setDontKnow))
                .thenRun(this::displayNextCard);
    }

    @Override
    public void setKnow() {
        if (currentCardFuture == null) return;
        currentCardFuture
                .thenApply(applyOrSkip(mDecksModel::setKnow))
                .thenRun(this::displayNextCard);
    }

    @Override
    public void revealBackSide() {
        mView.displayOnBackSide(deferredSecondSide);
        mView.revealBackSide();
    }


    @Override
    public void refresh() {
        if (mSettings.isDefaultUser()) mView.notify(R.string.using_default_user);
        displayNextCard();
    }

    private void displayNextCard(){
        currentCardFuture = mDecksModel.getNextCard();
        currentCardFuture.thenAccept(acceptOrCall(this::displayCard, this::displayEmpty));
    }

    private void displayCard(Card card){
        setEnabledAllButtons(true);
        mView.displayOnBackSide(R.string.tap_to_see);
        mView.hideBackSide();
        mView.displayOnFrontSide(card.getFrontSide());
        deferredSecondSide = card.getBackSide();
    }

    private void displayEmpty() {
        if (!mSettings.isDefaultUser()) mView.notify(R.string.emptyDeck);
        mView.displayOnFrontSide(R.string.emptyDeck);
        mView.setVisibleBackSide(false);
        setEnabledAllButtons(false);
    }

    private void setEnabledAllButtons(boolean enabled){
        mView.setEnabledDontKnowButton(enabled);
        mView.setEnabledKnowButton(enabled);
        mView.setEnabledDeleteButton(enabled);
    }

}
