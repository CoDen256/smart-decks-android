package coden.android.card.mvc.controller;


import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.CompletableFuture;

import coden.android.card.R;
import coden.core.decks.data.Card;
import coden.core.decks.model.DecksModel;
import coden.core.decks.user.User;

import static coden.android.card.mvc.model.ModelUtils.getModel;
import static coden.android.card.mvc.model.ModelUtils.getUser;
import static coden.android.card.mvc.utils.FunctionalInterfaces.acceptOrCall;
import static coden.android.card.mvc.utils.FunctionalInterfaces.applyOrSkip;

public class MainActivityCardController implements MainActivityController {

    private final View mView;
    private final Activity mActivity;

    private final DecksModel mCachedCardModel;

    private final View mdeleteButton;
    private final View mAddButton;

    private CompletableFuture<Card> currentCardFuture;
    private final TextView mFirstSide;
    private final TextView mSecondSide;
    private String deferredSecondSide;

    private final View mDontKnowButton;
    private final View mKnowButton;

    private User mUser;


    public MainActivityCardController(Activity activity, View view) {
        mActivity = activity;
        mView = view;

        setUser(getUser(view));
        mCachedCardModel = getModel(view);

        mFirstSide = mView.findViewById(R.id.firstSide);
        mSecondSide = mView.findViewById(R.id.secondSide);
        mAddButton = mView.findViewById(R.id.add_button);
        mdeleteButton = mView.findViewById(R.id.delete_button);
        mDontKnowButton = mView.findViewById(R.id.button_dont_know);
        mKnowButton = mView.findViewById(R.id.button_know);
        displayNextCard();
    }



    private void setUser(User user){
        mUser = user;
        notifyOnDefaultUser();
    }

    @Override
    public void addNewCard(Intent intent) {
        String firstSide = intent.getStringExtra("firstSide");
        String secondSide = intent.getStringExtra("secondSide");
        Card newCard = mCachedCardModel.createCard(firstSide, secondSide);
        mCachedCardModel.addCard(newCard);
    }

    @Override
    public void deleteCurrentCard() {
        if (currentCardFuture == null) return;
        currentCardFuture
                .thenApply(applyOrSkip(mCachedCardModel::deleteCard))
                .thenRun(this::displayNextCard);
    }

    @Override
    public void setDontKnow() {
        if (currentCardFuture == null) return;
        currentCardFuture
                .thenApply(applyOrSkip(mCachedCardModel::setDontKnow))
                .thenRun(this::displayNextCard);
    }


    @Override
    public void setKnow() {
        if (currentCardFuture == null) return;
        currentCardFuture
                .thenApply(applyOrSkip(mCachedCardModel::setKnow))
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
        currentCardFuture = mCachedCardModel.getNextCard();
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
        if (isDefaultUser())
            Toast.makeText(mActivity, R.string.using_default_user, Toast.LENGTH_LONG).show();
    }

    private void notifyOnNoCards() {
        if (!isDefaultUser())
            Toast.makeText(mActivity, R.string.no_cards, Toast.LENGTH_LONG).show();
    }

    private boolean isDefaultUser() {
        return "default".equals(mUser.getName());
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
