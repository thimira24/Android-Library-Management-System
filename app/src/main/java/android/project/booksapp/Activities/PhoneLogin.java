package android.project.booksapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.project.booksapp.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneLogin extends AppCompatActivity {

    private EditText mPhoneText;
    private EditText mCodeText;
    private TextView templink;
    private Button mSendBtn;
    private FirebaseAuth mAuth;
    private TextView errorView;
    public String mVerificationId;
    public PhoneAuthProvider.ForceResendingToken mResendToken;
    public int btntype = 0;
    public PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallsback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentToolbar();
        setContentView(R.layout.phone_login);

        initViews();
        activate();
        callbacks();

        templink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PhoneLogin.this, Main2Activity.class)); finish();
            }
        });



    }

    public void initViews(){

        mPhoneText = (EditText) findViewById(R.id.phoneEditText);
        mCodeText = (EditText) findViewById(R.id.codeEditText);
        templink = (TextView) findViewById(R.id.heys);
        mSendBtn = (Button) findViewById(R.id.sendbtn);
        mAuth = FirebaseAuth.getInstance();
        errorView = (TextView)findViewById(R.id.textTF);

    }

    public void activate(){

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sendbtns = mSendBtn.getText().toString().trim();

                if (TextUtils.isEmpty(mPhoneText.getText().toString())){
                    Toast.makeText(PhoneLogin.this, "Enter Code.", Toast.LENGTH_LONG).show();
                }
                else
                    {
                    if (btntype == 0 )
                    {
                        mSendBtn.setEnabled(true);
                        mCodeText.setVisibility(View.VISIBLE);
                        String phoneNumber = mPhoneText.getText().toString();
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, PhoneLogin.this, mcallsback);
                    }else {
                        mSendBtn.setEnabled(true);
                        String verificationcode = mCodeText.getText().toString();
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,verificationcode);
                        signInWithPhoneAuthCredential(credential);
                    }
                }

            }
        });
    }

    public void callbacks(){

        mcallsback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                errorView.setText("There was some error with Verification Code. please click 'administrator' to access library, I fix this soon");
                errorView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {

                mVerificationId = verificationId;
                btntype =1;
                mResendToken = token;
                errorView.setText("Code sent!");
                mCodeText.setVisibility(View.VISIBLE);

                mSendBtn.setText("Verify Code");
                mSendBtn.setEnabled(true);

            }

        };

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            startActivity(new Intent(PhoneLogin.this, AdminSplash.class));
                            finish();
                        } else {
                            errorView.setText("There was some error with login");
                            errorView.setVisibility(View.VISIBLE);
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            }
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
