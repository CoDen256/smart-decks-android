package coden.android.card.ui.pending;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import coden.android.card.R;
import coden.cards.data.Card;
import coden.cards.model.Model;
import coden.cards.reminder.BaseReminder;

import static coden.android.card.mvc.model.ModelUtils.getModel;
import static coden.android.card.mvc.model.ModelUtils.getReminder;

/**
 * A fragment representing a list of Items.
 */
public class PendingCardFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PendingCardFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PendingCardFragment newInstance(int columnCount) {
        PendingCardFragment fragment = new PendingCardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending_card_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
//            getModel(view)
//                    .getPendingCards()
//                    .thenAccept(cards -> setAdapter(recyclerView, cards));
            Model model = getModel(view);
            BaseReminder reminder = getReminder(view);
            model.getPendingCards().thenCombine(model.getReadyCards(), (pending, ready) -> {
                ready.addAll(pending);
                return ready;
            }).thenAccept(cards -> setAdapter(recyclerView, cards, reminder));

        }
        return view;
    }

    private void setAdapter(RecyclerView recyclerView, List<Card> cards, BaseReminder reminder) {
        recyclerView.setAdapter(new CardRecyclerViewAdapter(cards, reminder));
    }
}