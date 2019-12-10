package android.project.booksapp.Activities;

import android.content.Intent;
import android.project.booksapp.R;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class LogOutAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_out_admin);

        Thread myThread = new Thread(){

            @Override
            public void run() {
                try {
                    sleep(3000); startActivity(new Intent(LogOutAdmin.this, UserLogin.class)); finish();
                }catch (Exception e){

                }
            }
        };

        myThread.start();
    }
}
