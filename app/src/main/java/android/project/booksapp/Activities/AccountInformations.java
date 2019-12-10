package android.project.booksapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.project.booksapp.R;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountInformations extends AppCompatActivity {

    private TextView membername, hometown, email, birthday, phone, username, password;
    private ImageView imageview, popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentToolbar();
        setContentView(R.layout.activity_account_informations);

        initViews();
        closeaction();


    }

    public void initViews(){

        membername = findViewById(R.id.membername_id);
        hometown = findViewById(R.id.hometown_id);
        email = findViewById(R.id.email_id);
        birthday = findViewById(R.id.birthday_id);
        phone = findViewById(R.id.phone_id);
        username = findViewById(R.id.username_id);
        password = findViewById(R.id.password_id);
        popup = findViewById(R.id.closepop);

        membername.setText(getIntent().getStringExtra("membername"));
        hometown.setText(getIntent().getStringExtra("hometown"));
        email.setText(getIntent().getStringExtra("email"));
        birthday.setText(getIntent().getStringExtra("birthday"));
        phone.setText(getIntent().getStringExtra("phone"));
        username.setText(getIntent().getStringExtra("username"));
        password.setText(getIntent().getStringExtra("password"));

    }

    public void closeaction(){

        popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( AccountInformations.this, userView.class)); finish();
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
