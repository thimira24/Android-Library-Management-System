package android.project.booksapp.Activities;

import android.content.Intent;
import android.project.booksapp.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class AdminLogin extends AppCompatActivity {

    private EditText mPhoneText;
    private EditText mCodeText;
    private Button mSendBtn;
    private FirebaseAuth mAuth;
    private TextView errorView;
    public String mVerificationId;
    private TextView adminview;
    public PhoneAuthProvider.ForceResendingToken mResendToken;
    public int btntype = 0;
    public PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallsback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login);

        initViews();
        activate();
        callbacks();
        gotolink();



    }

    public void initViews(){

        mPhoneText = (EditText) findViewById(R.id.phoneEditText);
        mCodeText = (EditText) findViewById(R.id.codeEditText);
        mSendBtn = (Button) findViewById(R.id.sendbtn);
        mAuth = FirebaseAuth.getInstance();
        errorView = (TextView)findViewById(R.id.textTF);
        adminview = (TextView) findViewById(R.id.adminlogo);

    }

    public void gotolink(){

        adminview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminLogin.this, Main2Activity.class));
            }
        });
    }

    public void activate(){

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btntype == 0 )
                {
                    mSendBtn.setEnabled(false);
                    mCodeText.setVisibility(View.VISIBLE);
                    String phoneNumber = mPhoneText.getText().toString();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, AdminLogin.this, mcallsback);
                }else {
                    mSendBtn.setEnabled(false);
                    String verificationcode = mCodeText.getText().toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,verificationcode);
                    signInWithPhoneAuthCredential(credential);
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

                errorView.setText("There was some error with Verification Code. please click 'Administrator' to access admin panel. this is for testing, will fix soon");
                errorView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {

                mVerificationId = verificationId;
                btntype =1;
                mResendToken = token;
                mSendBtn.setText("Verify");
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
                            startActivity(new Intent(AdminLogin.this, Main2Activity.class));
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


}
