package coden.decks.android;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;


public class CreateCardActivity extends AppCompatActivity {

    private TextInputEditText mFrontSide;
    private TextInputEditText mBackSide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFrontSide = findViewById(R.id.frontSide);
        mBackSide = findViewById(R.id.backside);
        setListenerOnCreateButton();
        setListenerOnCancelButton();
    }


    private void setListenerOnCreateButton() {
        Button createButton = findViewById(R.id.submit_button);
        createButton.setOnClickListener( v -> publishOnCompleted());
    }

    private void setListenerOnCancelButton() {
        Button cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener( v -> discard());
    }

    private void publishOnCompleted() {
        if (TextUtils.isEmpty(mFrontSide.getText()) || TextUtils.isEmpty(mBackSide.getText())){
            toastIncomplete();
        }else {
            publish(mFrontSide.getText().toString(), mBackSide.getText().toString());
        }
    }

    private void publish(String frontSide, String backSide){
        Intent data = new Intent();
        data.putExtra("frontSide", frontSide);
        data.putExtra("backSide", backSide);
        setResult(RESULT_OK,data);
        finish();
    }

    private void discard(){
        Intent data = new Intent();
        setResult(RESULT_CANCELED,data);
        finish();
    }

    private void toastIncomplete(){
        Toast.makeText(this, "Entries are incomplete", Toast.LENGTH_SHORT).show();
    }
}