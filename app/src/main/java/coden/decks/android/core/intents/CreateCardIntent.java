package coden.decks.android.core.intents;

import android.content.Intent;

public class CreateCardIntent extends Intent {
    public static final String FRONT_SIDE_FIELD = "frontSide";
    public static final String BACK_SIDE_FIELD = "backSide";

    private String frontSide;
    private String backSide;

    public CreateCardIntent(String frontSide, String backSide) {
        setFrontSide(frontSide);
        setBackSide(backSide);
    }

    public CreateCardIntent(Intent intent){
        this.frontSide = intent.getStringExtra(FRONT_SIDE_FIELD);
        this.backSide = intent.getStringExtra(BACK_SIDE_FIELD);
    }

    public void setFrontSide(String frontSide) {
        this.frontSide = frontSide;
        putExtra(FRONT_SIDE_FIELD, frontSide);
    }

    public void setBackSide(String backSide) {
        this.backSide = backSide;
        putExtra(BACK_SIDE_FIELD, backSide);
    }

    public String getFrontSide() {
        return frontSide;
    }

    public String getBackSide() {
        return backSide;
    }
}
