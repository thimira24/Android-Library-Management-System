package android.project.booksapp.Activities;

import android.content.Intent;
import android.project.booksapp.R;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class AdminSplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_splash);

        Thread myThread = new Thread(){

            @Override
            public void run() {
                try {
                    sleep(3000); startActivity(new Intent(AdminSplash.this, Main2Activity.class)); finish();
                }catch (Exception e){

                }
            }
        }; myThread.start();
    }
}
