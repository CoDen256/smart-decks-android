package coden.android.card;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import coden.cards.model.Model;

public class CreateCardActivity extends AppCompatActivity {

    private TextInputEditText mFirstSide;
    private TextInputEditText mSecondSide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFirstSide = findViewById(R.id.first_side_input);
        mSecondSide = findViewById(R.id.second_side_input);
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
        if (TextUtils.isEmpty(mFirstSide.getText()) || TextUtils.isEmpty(mSecondSide.getText())){
            toastIncomplete();
        }else {
            publish(mFirstSide.getText().toString(), mSecondSide.getText().toString());
        }
    }

    private void publish(String firstSide, String secondSide){
        Intent data = new Intent();
        data.putExtra("firstSide",firstSide);
        data.putExtra("secondSide",secondSide);
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