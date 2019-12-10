package android.project.booksapp.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.project.booksapp.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class BookRegister extends AppCompatActivity {

    private static final String TAG = "BookRegister";
    private ImageView close,correctbtn, mImageView;
    private Button mChoosebtn, registerButton;
    public Spinner spinner1, spinner2;
    private EditText book_ID, book_Author, book_ISBN, book_Date, book_Copies, Edition;
    private ProgressBar mPrograssbar;
    private DatePickerDialog.OnDateSetListener mDateSetListner;
    private DatabaseReference databaseReference;
    private StorageReference mStorageRef;
    private FirebaseAuth firebaseAuth;
    private Uri mImageUri;
    private static final String REQUIRED = "Required";
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentToolbar();
        setContentView(R.layout.book_register);


        initviews();
        popupButton();
        chooseImageButton();
        Spinners();
        Spinners1();
        regbutton();
        DatePicker();

    }

    public void initviews(){

        book_ID = (EditText) findViewById(R.id.Book_title_id);
        book_Author = (EditText) findViewById(R.id.Book_Author_id);
        book_ISBN = (EditText) findViewById(R.id.Book_isbn_id);
        book_Date = (EditText) findViewById(R.id.Book_date_id);
        book_Copies = (EditText) findViewById(R.id.Book_copies_id);
        Edition = (EditText) findViewById(R.id.Edition_id);


        close = (ImageView) findViewById(R.id.closepop);
        mImageView = (ImageView) findViewById(R.id.image_View);
        registerButton = (Button) findViewById(R.id.button_register);
        mChoosebtn = (Button) findViewById(R.id.choose_image_button);
        correctbtn = (ImageView) findViewById(R.id.correct);

        spinner1 = (Spinner)findViewById(R.id.spinner1);
        spinner2 = (Spinner)findViewById(R.id.spinner2);
        mPrograssbar = (ProgressBar) findViewById(R.id.mProgressbar);

        mStorageRef = FirebaseStorage.getInstance().getReference("Book_ID");
        databaseReference = FirebaseDatabase.getInstance().getReference("Book_ID");
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void  regbutton(){

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();

            }
        });
    }

    public void DatePicker(){

        book_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(BookRegister.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListner, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyyy" + month + "/" + dayOfMonth + "/" + year );
                String date =  month + "/" + dayOfMonth + "/" + year ;
                book_Date.setText(date);
            }
        };
    }

    public String getFileExtension(Uri uri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    public void uploadFile(){

         if (mImageUri != null){

             StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
             fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                 @Override
                 public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                     Handler handler = new Handler();
                     handler.postDelayed(new Runnable() {
                         @Override
                         public void run() {
                             mPrograssbar.setProgress(0);
                         }
                     }, 5000);

                              String bookname = book_ID.getText().toString().trim();
                              String bookauthor = book_Author.getText().toString().trim();
                              String bookisbn = book_ISBN.getText().toString().trim();
                              String bookdate = book_Date.getText().toString().trim();
                              String bookcopies = book_Copies.getText().toString().trim();
                              String bookcategory = spinner1.getSelectedItem().toString().trim();
                              String booklocation = spinner2.getSelectedItem().toString().trim();
                              String bookedition = Edition.getText().toString().trim();

                     if (bookname.isEmpty() || bookauthor.isEmpty() || bookisbn.isEmpty() || bookdate.isEmpty() || bookcopies.isEmpty() || bookcategory.isEmpty() || booklocation.isEmpty() || bookedition.isEmpty()){

                         Toast.makeText(BookRegister.this, "Fill all text boxes.", Toast.LENGTH_LONG).show();
                     }
                     else {
                              Book books = new Book(bookname, bookauthor, bookisbn, bookdate, bookcopies, bookcategory, booklocation, bookedition,taskSnapshot.getDownloadUrl().toString() );
                              String uploadID = databaseReference.push().getKey();
                                 final HashMap<String, String> bookMap = new HashMap<>();
                                 bookMap.put("bookauthor", bookauthor);
                                 bookMap.put("bookcategory", bookcategory);
                                 bookMap.put("bookcopies", bookcopies);
                                 bookMap.put("bookdate", bookdate);
                                 bookMap.put("bookedition", bookedition);
                                 bookMap.put("bookisbn", bookisbn);
                                 bookMap.put("booklocation", booklocation);
                                 bookMap.put("bookname", bookname);
                                 bookMap.put("mImageUrl", taskSnapshot.getDownloadUrl().toString());
                              databaseReference.child(bookisbn).setValue(bookMap);
                              Toast.makeText(BookRegister.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                              correctbtn.setVisibility(View.VISIBLE);
                              resetBTN();

                     }
                 }
             }).addOnFailureListener(new OnFailureListener() {
                 @Override
                 public void onFailure(@NonNull Exception e) {

                     Toast.makeText(BookRegister.this, e.getMessage(), Toast.LENGTH_LONG).show();

                 }
             }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                 @Override
                 public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        mPrograssbar.setProgress((int) progress);

                 }
             });

        }else {
             Toast.makeText(this, "Please fill all text boxes and Book Cover", Toast.LENGTH_LONG).show();
        }

    }

    private void resetBTN(){

        book_ID.setText("");
        book_Author.setText("");
        book_ISBN.setText("");
        book_Date.setText("");
        book_Copies.setText("");
        Edition.setText("");

        book_ID.setHint("Book Title");
        book_Author.setHint("Author");
        book_ISBN.setHint("ISBN");
        book_Date.setHint("P.Date");
        book_Copies.setHint("Copies");
        Edition.setHint("Edition");
        correctbtn.setVisibility(View.INVISIBLE);
    }

    public void Spinners(){

        final List<String> list = new ArrayList<String>();
        list.add("Computer science");
        list.add("information and general");
        list.add("Philosophy and psychology");
        list.add("Religion");
        list.add("Social Sciences");
        list.add("Language");
        list.add("Science");
        list.add("Maths");
        list.add("Arts");
        list.add("History");
        list.add("Military");
        list.add("Geography");
        list.add("Home Science");
        list.add("Literature");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,list);
        spinner1.setAdapter(arrayAdapter);

    }

    public void Spinners1(){

        final List<String> list1 = new ArrayList<String>();
        list1.add("1st Floor");
        list1.add("2nd Floor");
        list1.add("3rd Floor");
        list1.add("4th Floor");
        final ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,list1);
        spinner2.setAdapter(arrayAdapter1);

    }

    public void chooseImageButton(){

        mChoosebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check runtime permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        // is not granted
                        String [] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        //show pop up for rumtime permission
                        requestPermissions(permissions, PERMISSION_CODE);

                    }else{
                        // permission already granted
                        pickImageFromGallery();

                    }
                } else {
                        /// system os is less than android 6.0
                        pickImageFromGallery();
                }
            }
        });
    }

    private void  pickImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length > 0 && grantResults [0] == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery();
                }else{
                    Toast.makeText(this,"Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE && data != null && data.getData() != null){

            mImageUri = data.getData();
            Picasso.with(this).load(mImageUri).into(mImageView);

        }
    }

    public void popupButton(){

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookRegister.this, Main2Activity.class));
                finish();
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
