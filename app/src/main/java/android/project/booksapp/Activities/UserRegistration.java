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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class UserRegistration extends AppCompatActivity {

    private EditText cname, caddrress, cemail, cbirthday, cphone, cusername, cpassword;
    private ImageView profile, closepopup, imagepickup;
    private Button registerButton;
    private TextView signin;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private static final String TAG = "UserRegistration";
    private ProgressBar mPrograssbar;
    private DatePickerDialog.OnDateSetListener mDateSetListner;
    private DatabaseReference databaseReference;
    private StorageReference mStorageRef;
    private FirebaseAuth firebaseAuth;
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentToolbar();
        setContentView(R.layout.activity_user_registration);

        initviews();
        closeView();
        DatePicker();
        chooseImageButton();
        regButton();


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserRegistration.this, AccountLogin.class)); finish();
            }
        });
    }


    public void initviews(){

        // edit texxt ids
        cname = findViewById(R.id.cname_id);
        caddrress = findViewById(R.id.caddress_id);
        cemail = findViewById(R.id.cemail_id);
        cbirthday = findViewById(R.id.cbirthday_id);
        cphone = findViewById(R.id.cphone_id);
        cusername = findViewById(R.id.cusername_id);
        cpassword = findViewById(R.id.cpassword_id);

        // image view
        profile = findViewById(R.id.profile_view);
        closepopup = findViewById(R.id.closepop);
        imagepickup = findViewById(R.id.addpic);

        // button ids
        registerButton = findViewById(R.id.button_register);

        // text view ids
        signin = findViewById(R.id.signinid);

        mPrograssbar = findViewById(R.id.mprogressBar);

        // other references
        mStorageRef = FirebaseStorage.getInstance().getReference("account");
        databaseReference = FirebaseDatabase.getInstance().getReference("account");
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void chooseImageButton(){

        imagepickup.setOnClickListener(new View.OnClickListener() {
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

    public void regButton(){

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });
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
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPrograssbar.setProgress(0);
                        }
                    }, 5000);

                    final String name = cname.getText().toString().trim();
                    final String address = caddrress.getText().toString().trim();
                    final String email = cemail.getText().toString().trim();
                    final String birthday = cbirthday.getText().toString().trim();
                    final String phone = cphone.getText().toString().trim();
                    final String username = cusername.getText().toString().trim();
                    final String password = cpassword.getText().toString().trim();


                    if (name.isEmpty() || address.isEmpty() || email.isEmpty() || birthday.isEmpty() || phone.isEmpty() || username.isEmpty() || password.isEmpty() ){

                        Toast.makeText(UserRegistration.this, "Please fill all text boxes.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(UserRegistration.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "createUserWithEmail:success");
                                            FirebaseUser user = firebaseAuth.getCurrentUser();
                                            CreateAccountModel accountModel = new CreateAccountModel(name, address, email, birthday, phone, username, password,taskSnapshot.getDownloadUrl().toString() );
                                            String uploadID = databaseReference.push().getKey();
                                            databaseReference.child(uploadID).setValue(accountModel);
                                            Toast.makeText(UserRegistration.this, "Created account sccessfully", Toast.LENGTH_LONG).show();
                                            resetfields();

                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(UserRegistration.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                        }

                                        // ...
                                    }
                                });

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(UserRegistration.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mPrograssbar.setProgress((int) progress);

                }
            });

        }else {
            Toast.makeText(this, "Please fill all text boxes and profile picture", Toast.LENGTH_LONG).show();
        }

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
            Picasso.with(this).load(mImageUri).into(profile);

        }
    }

    public void DatePicker(){

        cbirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(UserRegistration.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListner, year, month, day);
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
                cbirthday.setText(date);
            }
        };
    }

    private void resetfields(){

        cname.setText("");
        caddrress.setText("");
        cemail.setText("");
        cbirthday.setText("");
        cphone.setText("");
        cusername.setText("");
        cpassword.setText("");
        profile.setImageResource(R.drawable.userfeedback);

        cname.setHint("Name");
        caddrress.setHint("Address");
        cemail.setHint("Email");
        cbirthday.setHint("Birthday");
        cphone.setHint("Phone");
        cusername.setHint("Username");
        cpassword.setHint("Password");
    }

    public void closeView(){

        closepopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserRegistration.this, UserLogin.class)); finish();
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
