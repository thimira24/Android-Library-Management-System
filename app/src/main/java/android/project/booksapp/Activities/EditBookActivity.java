package android.project.booksapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.project.booksapp.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditBookActivity extends AppCompatActivity {

    private EditText book_ID, book_Author, book_ISBN, book_Date, book_Copies, Edition, spinner1, spinner2;
    private Button  registerButton;
    private ImageView popup;
    DatabaseReference mdatabasereference;
    String key,image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentToolbar();
        setContentView(R.layout.activity_edit_book);

        initViews();
        closepopup();

    }

    public void initViews(){

        book_ID = (EditText) findViewById(R.id.Book_title_id);
        book_Author = (EditText) findViewById(R.id.Book_Author_id);
        book_ISBN = (EditText) findViewById(R.id.Book_isbn_id);
        book_Date = (EditText) findViewById(R.id.Book_date_id);
        book_Copies = (EditText) findViewById(R.id.Book_copies_id);
        Edition = (EditText) findViewById(R.id.Edition_id);
        spinner1 = (EditText) findViewById(R.id.spinner1);
        spinner2 = (EditText) findViewById(R.id.spinner2);
        popup = (ImageView) findViewById(R.id.closepop);
        registerButton = (Button) findViewById(R.id.button_register);

        key = getIntent().getExtras().get("bookisbn").toString();
        mdatabasereference = FirebaseDatabase.getInstance().getReference().child("Book_ID");

        book_ID.setText(getIntent().getStringExtra("bname"));
        book_Author.setText(getIntent().getStringExtra("bauthor"));
        book_ISBN.setText(getIntent().getStringExtra("bookisbn"));
        book_Date.setText(getIntent().getStringExtra("date"));
        book_Copies.setText(getIntent().getStringExtra("copies"));
        Edition.setText(getIntent().getStringExtra("edition"));
        spinner1.setText(getIntent().getStringExtra("category"));
        spinner2.setText(getIntent().getStringExtra("location"));
        image = getIntent().getStringExtra("image");


    }

    public void closepopup(){

        popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent( EditBookActivity.this, Main2Activity.class)); finish();
            }
        });
    }

    public void buttonUpdate(View view) {
            // edit book info
        final HashMap<String, String> bookMap = new HashMap<>();
        bookMap.put("bookauthor", book_Author.getText().toString());
        bookMap.put("bookcategory", spinner1.getText().toString());
        bookMap.put("bookcopies", book_Copies.getText().toString());
        bookMap.put("bookdate", book_Date.getText().toString());
        bookMap.put("bookedition", Edition.getText().toString());
        bookMap.put("bookisbn", book_ISBN.getText().toString());
        bookMap.put("booklocation", spinner2.getText().toString());
        bookMap.put("bookname", book_ID.getText().toString());
        bookMap.put("mImageUrl", image);

           mdatabasereference.child(book_ISBN.getText().toString()).setValue(bookMap).addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   if (task.isSuccessful()){
                       Toast.makeText(EditBookActivity.this, "Sucessfull", Toast.LENGTH_LONG).show();
                   }else{
                       Toast.makeText(EditBookActivity.this, "Failed", Toast.LENGTH_LONG).show();
                   }
               }
           });

    }

    private void transparentToolbar() {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
