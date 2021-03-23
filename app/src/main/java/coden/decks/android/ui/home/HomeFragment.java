package coden.decks.android.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


import coden.decks.android.CreateCardActivity;
import coden.decks.android.R;
import coden.decks.android.SettingsActivity;
import coden.decks.android.core.CoreApplicationComponent;
import coden.decks.android.core.DaggerCoreApplicationComponent;
import coden.decks.android.core.controller.MainActivityCardController;
import coden.decks.android.core.controller.MainActivityController;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    private View mRoot;
    private MainActivityController mController;

    private CoreApplicationComponent mComponent = DaggerCoreApplicationComponent.create();

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mRoot = inflater.inflate(R.layout.fragment_home, container, false);
        mController = new MainActivityCardController(mComponent, getActivity(), mRoot);
        setListenerOnAddButton();
        setListenerOnDeleteButton();
        setListenerOnKnow();
        setListenerOnDontKnow();
        setListenerOnShowSecondSide();

        return mRoot;
    }

    private void setListenerOnDeleteButton() {
        FloatingActionButton deleteButton = mRoot.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(view -> mController.deleteCurrentCard());
    }

    private void setListenerOnAddButton() {
        FloatingActionButton addButton = mRoot.findViewById(R.id.add_button);
        addButton.setOnClickListener(view -> {
            Intent launchNewIntent = new Intent(getContext(), CreateCardActivity.class);
            startActivityForResult(launchNewIntent, 0);
        });
    }

    private void setListenerOnKnow() {
        Button knowButton = mRoot.findViewById(R.id.button_know);
        knowButton.setOnClickListener(view -> mController.setKnow());
    }

    private void setListenerOnDontKnow() {
        Button dontKnowButton = mRoot.findViewById(R.id.button_dont_know);
        dontKnowButton.setOnClickListener(view -> mController.setDontKnow());
    }

    private void setListenerOnShowSecondSide(){
        TextView showSecondSideButton = mRoot.findViewById(R.id.secondSide);
        showSecondSideButton.setOnClickListener(view -> mController.showSecondSide());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                mController.addNewCard(data);
            }
            else {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent launchNewIntent = new Intent(getActivity(), SettingsActivity.class);
            startActivityForResult(launchNewIntent, 1);
        }else if (item.getItemId() == R.id.action_refresh){
            mController.refresh();
        }
        return super.onOptionsItemSelected(item);
    }



}