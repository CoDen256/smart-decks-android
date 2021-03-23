package coden.decks.android;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import coden.decks.android.core.intents.CreateCardIntent;


public class CreateCardActivity extends AppCompatActivity {

    private TextInputEditText mFrontSide;
    private TextInputEditText mBackSide;
    private View mCancelButton;
    private View mCreateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);
        setSupportActionBar(findViewById(R.id.toolbar));
        findViews();
        setListenerOnCreateButton();
        setListenerOnCancelButton();
    }

    private void findViews() {
        mCreateButton = findViewById(R.id.submit_button);
        mCancelButton = findViewById(R.id.cancel_button);
        mFrontSide = findViewById(R.id.frontSide);
        mBackSide = findViewById(R.id.backside);
    }


    private void setListenerOnCreateButton() {
        mCreateButton.setOnClickListener( v -> publishOnCompleted());
    }

    private void setListenerOnCancelButton() {
        mCancelButton.setOnClickListener( v -> discard());
    }

    private void publishOnCompleted() {
        if (TextUtils.isEmpty(mFrontSide.getText()) || TextUtils.isEmpty(mBackSide.getText())){
            toastIncomplete();
        }else {
            publish(mFrontSide.getText().toString(), mBackSide.getText().toString());
        }
    }

    private void publish(String frontSide, String backSide){
        setResult(RESULT_OK, new CreateCardIntent(frontSide, backSide));
        finish();
    }

    private void discard(){
        setResult(RESULT_CANCELED, new Intent());
        finish();
    }

    private void toastIncomplete(){
        Toast.makeText(this, R.string.entries_incomplete, Toast.LENGTH_SHORT).show();
    }
}