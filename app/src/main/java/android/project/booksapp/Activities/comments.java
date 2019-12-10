package android.project.booksapp.Activities;

import android.project.booksapp.R;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class comments extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FeedbackAdapter mAdapter;
    private DatabaseReference databaseReference;
    private List<FeedbackModel> mFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        mRecyclerView = findViewById(R.id.recycler_view_idx);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFeedback = new ArrayList<FeedbackModel>();
        databaseReference = FirebaseDatabase.getInstance().getReference("User_Feedback");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    FeedbackModel feedbacks = postSnapshot.getValue(FeedbackModel.class);
                    mFeedback.add(feedbacks);
                }

                mAdapter = new FeedbackAdapter(comments.this, mFeedback);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(comments.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}
