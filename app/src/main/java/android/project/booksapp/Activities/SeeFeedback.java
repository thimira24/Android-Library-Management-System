package android.project.booksapp.Activities;

import android.content.Intent;
import android.project.booksapp.R;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SeeFeedback extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FeedbackAdapter mAdapter;
    private DatabaseReference databaseReference;
    private List<FeedbackModel> mFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_feedback);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

                mAdapter = new FeedbackAdapter(SeeFeedback.this, mFeedback);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(SeeFeedback.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(SeeFeedback.this, Main2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
