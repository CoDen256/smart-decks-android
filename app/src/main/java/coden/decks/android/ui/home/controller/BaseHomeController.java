package coden.decks.android.ui.home.controller;


public interface BaseHomeController {
    void addNewCard(String frontSide, String backSide);

    void deleteCurrentCard();

    void setDontKnow();

    void setKnow();

    void revealBackSide();

    void refresh();
}
