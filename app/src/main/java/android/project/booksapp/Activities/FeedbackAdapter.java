package android.project.booksapp.Activities;

import android.content.Context;
import android.project.booksapp.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {

    private Context mContext;
    private List<FeedbackModel> mFeedback;


    public FeedbackAdapter(Context mContext, List<FeedbackModel> mFeedback){

        this.mContext = mContext;
        this.mFeedback = mFeedback;
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.feedback_list, viewGroup, false);
        return new FeedbackAdapter.FeedbackViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder feedbackViewHolder, int i) {

        FeedbackModel feedbackcurrnt = mFeedback.get(i);
        feedbackViewHolder.message_title.setText(feedbackcurrnt.getTitle());
        feedbackViewHolder.message.setText(feedbackcurrnt.getMessage());
    }

    @Override
    public int getItemCount() {
        return mFeedback.size();
    }

    public class FeedbackViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{


        public TextView message_title, message;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);

            message_title = itemView.findViewById(R.id.feedback_title_id);
            message = itemView.findViewById(R.id.message_id);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }
    }



}
