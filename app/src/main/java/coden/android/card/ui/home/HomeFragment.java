package coden.android.card.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import coden.android.card.CreateCardActivity;
import coden.android.card.MainActivity;
import coden.android.card.R;
import coden.android.card.SettingsActivity;
import coden.android.card.mvc.controller.MainActivityCardController;
import coden.android.card.mvc.controller.MainActivityController;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    private View mRoot;
    private MainActivityController mMainActivityCardController;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mRoot = inflater.inflate(R.layout.fragment_home, container, false);
        mMainActivityCardController = new MainActivityCardController(getActivity(), mRoot);

        setListenerOnAddButton();
        setListenerOnDeleteButton();
        setListenerOnKnow();
        setListenerOnDontKnow();
        setListenerOnShowSecondSide();

        return mRoot;
    }

    private void setListenerOnDeleteButton() {
        FloatingActionButton deleteButton = mRoot.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(view -> mMainActivityCardController.deleteCurrentCard());
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
        knowButton.setOnClickListener(view -> mMainActivityCardController.setKnow());
    }

    private void setListenerOnDontKnow() {
        Button dontKnowButton = mRoot.findViewById(R.id.button_dont_know);
        dontKnowButton.setOnClickListener(view -> mMainActivityCardController.setDontKnow());
    }

    private void setListenerOnShowSecondSide(){
        TextView showSecondSideButton = mRoot.findViewById(R.id.secondSide);
        showSecondSideButton.setOnClickListener(view -> mMainActivityCardController.showSecondSide());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                mMainActivityCardController.addNewCard(data);
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
            mMainActivityCardController.refresh();
        }
        return super.onOptionsItemSelected(item);
    }



}