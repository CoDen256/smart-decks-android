package coden.android.card.ui.pending;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import coden.android.card.R;
import coden.cards.data.Card;
import coden.cards.reminder.BaseReminder;

import java.time.Duration;
import java.util.List;

import static coden.android.card.mvc.model.ModelUtils.getReminder;


public class CardRecyclerViewAdapter extends RecyclerView.Adapter<CardRecyclerViewAdapter.ViewHolder> {

    private final List<Card> mCards;
    private final BaseReminder mReminder;

    public CardRecyclerViewAdapter(List<Card> items, BaseReminder reminder) {
        mCards = items;
        mReminder = reminder;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_pending_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Card card = mCards.get(position);
        holder.mItem = card;
//        holder.mIdView.setText(String.valueOf(position+1));
        holder.firstSide.setText(card.getFirstSide());
        holder.secondSide.setText(card.getSecondSide());
        holder.deadline.setText(formatDuration(mReminder.getOvertime(card)));
    }

    private static String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        String positive = String.format(
                "%dh %02dm",
                absSeconds / 3600,
                (absSeconds % 3600) / 60);
        return seconds < 0 ? "-" + positive : positive;
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
//        public final TextView mIdView;
        public final TextView firstSide;
        public final TextView secondSide;
        public final TextView deadline;
        public Card mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
//            mIdView = view.findViewById(R.id.item_number);
            firstSide = view.findViewById(R.id.first_side_content);
            secondSide = view.findViewById(R.id.second_side_content);
            deadline = view.findViewById(R.id.deadline);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + firstSide.getText() + "'";
        }
    }
}