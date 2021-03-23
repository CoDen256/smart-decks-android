package coden.decks.android.ui.home.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
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

import javax.inject.Inject;

import coden.decks.android.CreateCardActivity;
import coden.decks.android.R;
import coden.decks.android.SettingsActivity;
import coden.decks.android.app.App;
import coden.decks.android.core.intents.CreateCardIntent;
import coden.decks.android.ui.home.controller.HomeController;
import coden.decks.android.ui.home.controller.BaseHomeController;
import coden.decks.android.core.settings.Settings;
import coden.decks.core.model.DecksModel;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment implements BaseHomeView{

    @Inject
    DecksModel mDecksModel;

    @Inject
    Settings mSettings;

    private View mRoot;

    private BaseHomeController mController;

    private TextView mFrontSide;
    private TextView mBackSide;
    private FloatingActionButton mAddButton;
    private FloatingActionButton mDeleteButton;
    private Button mDontKnowButton;
    private Button mKnowButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        App.appComponent.inject(this);

        setHasOptionsMenu(true);
        mRoot = inflater.inflate(R.layout.fragment_home, container, false);

        mController = new HomeController(mDecksModel, mSettings, this);

        findViews();
        setListeners();
        mController.refresh();

        return mRoot;
    }

    private void findViews() {
        mFrontSide = mRoot.findViewById(R.id.firstSide);
        mBackSide = mRoot.findViewById(R.id.secondSide);
        mAddButton = mRoot.findViewById(R.id.add_button);
        mDeleteButton = mRoot.findViewById(R.id.delete_button);
        mDontKnowButton = mRoot.findViewById(R.id.button_dont_know);
        mKnowButton = mRoot.findViewById(R.id.button_know);
    }

    private void setListeners() {
        setListenerOnAddButton();
        setListenerOnDeleteButton();
        setListenerOnKnow();
        setListenerOnDontKnow();
        setListenerOnShowSecondSide();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK && data != null) {
                CreateCardIntent createCardIntent = new CreateCardIntent(data);
                mController.addNewCard(createCardIntent.getFrontSide(), createCardIntent.getBackSide());
            }
            else if (data == null) notify("Something went wrong...", Toast.LENGTH_SHORT);
            else notify("Cancelled.", Toast.LENGTH_SHORT);
        }
    }

    private void setListenerOnAddButton() {
        mAddButton.setOnClickListener(view -> {
            Intent launchNewIntent = new Intent(getContext(), CreateCardActivity.class);
            startActivityForResult(launchNewIntent, 0);
        });
    }

    private void setListenerOnDeleteButton() {
        mDeleteButton.setOnClickListener(view -> mController.deleteCurrentCard());
    }

    private void setListenerOnKnow() {
        mKnowButton.setOnClickListener(view -> mController.setKnow());
    }

    private void setListenerOnDontKnow() {
        mDontKnowButton.setOnClickListener(view -> mController.setDontKnow());
    }

    private void setListenerOnShowSecondSide(){
        TextView showSecondSideButton = mRoot.findViewById(R.id.secondSide);
        showSecondSideButton.setOnClickListener(view -> {
            mController.revealBackSide();
        });
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


    @Override
    public void displayOnFrontSide(String text) {
        displayOnTextView(mFrontSide, text);
    }

    @Override
    public void displayOnFrontSide(int resid) {
        displayOnFrontSide(getString(resid));
    }

    @Override
    public void displayOnBackSide(String text) {
        displayOnTextView(mBackSide, text);
    }

    @Override
    public void displayOnBackSide(int resid) {
        displayOnBackSide(getString(resid));
    }

    @Override
    public void revealBackSide() {
        mBackSide.setShadowLayer(0, 0, 0, 0);
    }

    @Override
    public void hideBackSide() {
        mBackSide.setShadowLayer(5, 5, 5, R.color.second_side_shadow);
    }

    private void displayOnTextView(TextView view, String text){
        view.setText(toHtml(text));
    }

    private Spanned toHtml(String text) {
        return Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
    }

    @Override
    public void setVisibleFrontSide(boolean visible) {
        setVisibility(mFrontSide, visible);
    }

    @Override
    public void setVisibleBackSide(boolean visible) {
        setVisibility(mBackSide, visible);
    }

    @Override
    public void setEnabledDeleteButton(boolean enabled) {
        setVisibility(mDeleteButton, enabled);
    }

    @Override
    public void setEnabledAddButton(boolean enabled) {
        setVisibility(mAddButton, enabled);
    }

    @Override
    public void setEnabledKnowButton(boolean enabled) {
        setVisibility(mKnowButton, enabled);
    }

    @Override
    public void setEnabledDontKnowButton(boolean enabled) {
        setVisibility(mDontKnowButton, enabled);
    }

    private void setVisibility(View view, boolean visible){
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void notify(int resid) {
        notify(getString(resid));
    }

    @Override
    public void notify(String text) {
        notify(text, Toast.LENGTH_LONG);
    }

    private void notify(String text, int duration){
        Toast.makeText(getActivity(), text, duration).show();
    }
}