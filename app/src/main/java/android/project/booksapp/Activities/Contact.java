package android.project.booksapp.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.project.booksapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Contact extends AppCompatActivity {

    private EditText mEditTextTo;
    private  EditText mEditTextSubject;
    private EditText mEditTextMessage;
    private Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: 0717701797"));
                startActivity(intent);
            }
        });

        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.setData(Uri.parse("http://mybooksapps.blogspot.com"));
                startActivity(intent);
            }
        });

        initViews();
        initobjects();

    }

    public void initViews(){

        mEditTextTo = findViewById(R.id.edit_text_to);
        mEditTextSubject = findViewById(R.id.edit_text_subject);
        mEditTextMessage = findViewById(R.id.edit_text_message);
        buttonSend = findViewById(R.id.button_send);

    }

    public void initobjects(){

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });

    }

    public void sendMail(){

        String recipientList = mEditTextTo.getText().toString();
        String[] recipients = recipientList.split(",");

        String subject = mEditTextSubject.getText().toString();
        String message = mEditTextMessage.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(Contact.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
