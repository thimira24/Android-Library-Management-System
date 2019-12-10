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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountLogin extends AppCompatActivity {

    private EditText email, password;
    private Button signin_Button;
    private FirebaseAuth mAuth;
    private TextView link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentToolbar();
        setContentView(R.layout.activity_account_login);

        mAuth = FirebaseAuth.getInstance();

        initViews();

        signin_Button.setOnClickListener(new View.OnClickListener() {




            @Override
            public void onClick(View v) {
                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(AccountLogin.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                String useremail = email.getText().toString().trim();
                                String userpass = password.getText().toString().trim();

                                if (useremail.isEmpty() || userpass.isEmpty()) {
                                    Toast.makeText(AccountLogin.this, "Check Email and Password", Toast.LENGTH_SHORT).show();
                                }
                                else {

                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        startActivity(new Intent(AccountLogin.this, welcome.class)); finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(AccountLogin.this, "Check email, password & internet connection", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                finish();
                                // ...
                            }
                        });
            }
        });
    }


    public void initViews() {

        email = findViewById(R.id.email_id);
        password = findViewById(R.id.password_id);
        signin_Button = findViewById(R.id.signin);
        link  = (TextView) findViewById(R.id.heys);

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountLogin.this, AdminLogin.class)); finish();
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
