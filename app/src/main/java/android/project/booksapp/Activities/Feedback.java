package android.project.booksapp.Activities;

import android.content.Intent;
import android.project.booksapp.R;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Feedback extends AppCompatActivity {

    private EditText title1, message1;
    private Button sendfeedback;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        initViews();
        buttonAction();
    }

    public void initViews(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title1 = (EditText) findViewById(R.id.title_id);
        message1 = (EditText) findViewById(R.id.message_id);
        sendfeedback = (Button) findViewById(R.id.btn_id);
        databaseReference = FirebaseDatabase.getInstance().getReference("User_Feedback");
        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void getValues(){

        String title = title1.getText().toString().trim();
        String message = message1.getText().toString().trim();

        if (title.isEmpty() || message.isEmpty()){

            Toast.makeText(this, "Please fill All Fields correctly.", Toast.LENGTH_LONG).show();

        }else {

            String id = databaseReference.push().getKey();
            FeedbackModel feedbacks = new FeedbackModel(title, message);
            databaseReference.child(id).setValue(feedbacks);
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
            resetBTN();

        }
    }

    private void resetBTN(){

        title1.setText("");
        message1.setText("");

        title1.setHint("Title");
        message1.setHint("Write here");

    }

    public void buttonAction(){

        sendfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    getValues();
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(Feedback.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
