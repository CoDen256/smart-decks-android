package coden.decks.android.ui.home.view;

public interface BaseHomeView {

    void displayOnFrontSide(String text);
    void displayOnFrontSide(int resid);
    void displayOnBackSide(String text);
    void displayOnBackSide(int resid);
    void revealBackSide();
    void hideBackSide();

    void setVisibleFrontSide(boolean visible);
    void setVisibleBackSide(boolean visible);

    void setEnabledDeleteButton(boolean enabled);
    void setEnabledAddButton(boolean enabled);
    void setEnabledKnowButton(boolean enabled);
    void setEnabledDontKnowButton(boolean enabled);

    void notify(int resid);
    void notify(String text);
}
