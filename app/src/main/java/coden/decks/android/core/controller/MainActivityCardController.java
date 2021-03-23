package coden.decks.android.core.controller;


import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.CompletableFuture;

import coden.decks.android.R;
import coden.decks.android.core.CoreApplicationComponent;
import coden.decks.android.core.settings.Settings;
import coden.decks.core.data.Card;
import coden.decks.core.model.DecksModel;
import coden.decks.core.user.User;

import static coden.decks.android.core.utils.FunctionalInterfaces.acceptOrCall;
import static coden.decks.android.core.utils.FunctionalInterfaces.applyOrSkip;


public class MainActivityCardController implements MainActivityController {

    private final View mView;
    private final Activity mActivity;

    private final DecksModel mDecksModel;
    private final Settings mSettings;

    private final View mdeleteButton;
    private final View mAddButton;

    private CompletableFuture<Card> currentCardFuture;
    private final TextView mFirstSide;
    private final TextView mSecondSide;
    private String deferredSecondSide;

    private final View mDontKnowButton;
    private final View mKnowButton;


    public MainActivityCardController(DecksModel model, Settings settings, Activity activity, View view) {
        mActivity = activity;
        mView = view;

        mDecksModel = model;
        mSettings = settings;

        mFirstSide = mView.findViewById(R.id.firstSide);
        mSecondSide = mView.findViewById(R.id.secondSide);
        mAddButton = mView.findViewById(R.id.add_button);
        mdeleteButton = mView.findViewById(R.id.delete_button);
        mDontKnowButton = mView.findViewById(R.id.button_dont_know);
        mKnowButton = mView.findViewById(R.id.button_know);
        displayNextCard();
    }



    private void setUser(User user){
        notifyOnDefaultUser();
    }

    @Override
    public void addNewCard(Intent intent) {
        String firstSide = intent.getStringExtra("firstSide");
        String secondSide = intent.getStringExtra("secondSide");
        Card newCard = mDecksModel.createCard(firstSide, secondSide);
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
    public void showSecondSide() {
        mSecondSide.setShadowLayer(0, 0, 0, 0);
        displayOnTextView(mSecondSide, deferredSecondSide);
    }

    private void hideSecondSide(){
        mSecondSide.setShadowLayer(5, 5, 5, R.color.second_side_shadow);
        mSecondSide.setText(R.string.tap_to_see);
    }

    @Override
    public void refresh() {
        displayNextCard();
    }

    private void displayNextCard(){
        currentCardFuture = mDecksModel.getNextCard();
        currentCardFuture.thenAccept(acceptOrCall(this::displaySides, this::displayEmpty));
    }

    private void displaySides(Card card){
        enableAllButtons();
        hideSecondSide();
        displayOnTextView(mFirstSide, card.getFrontSide());
        deferredSecondSide = card.getBackSide();
    }

    private void displayOnTextView(TextView view, String text){
        view.setText(toHtml(text));
    }

    private Spanned toHtml(String text) {
        return Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
    }

    private void displayEmpty() {
        notifyOnNoCards();
        notifyOnDefaultUser();
        mFirstSide.setText(R.string.no_cards);
        setInvisible(mSecondSide);
        hideSecondSide();
        disableAllButtons();
    }

    private void notifyOnDefaultUser() {
        if (mSettings.isDefaultUser())
            Toast.makeText(mActivity, R.string.using_default_user, Toast.LENGTH_LONG).show();
    }

    private void notifyOnNoCards() {
        if (!mSettings.isDefaultUser())
            Toast.makeText(mActivity, R.string.no_cards, Toast.LENGTH_LONG).show();
    }

    private void disableAllButtons(){
        setInvisible(mdeleteButton);
        setInvisible(mKnowButton);
        setInvisible(mDontKnowButton);
    }

    private void enableAllButtons(){
        setVisible(mdeleteButton);
        setVisible(mKnowButton);
        setVisible(mDontKnowButton);
    }

    private void setInvisible(View view){
        view.setVisibility(View.INVISIBLE);
    }

    private void setVisible(View view){
        view.setVisibility(View.VISIBLE);
    }

}
