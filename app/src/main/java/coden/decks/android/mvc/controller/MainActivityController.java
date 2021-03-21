package coden.decks.android.mvc.controller;


import android.content.Intent;

public interface MainActivityController {
    void addNewCard(Intent intent);

    void deleteCurrentCard();

    void setDontKnow();

    void setKnow();

    void showSecondSide();

    void refresh();
}
