package android.project.booksapp.Activities;

import android.content.Intent;
import android.project.booksapp.R;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Thread myThread = new Thread(){

            @Override
            public void run() {
                    try {
                        sleep(5000); startActivity(new Intent(welcome.this, MainActivity.class)); finish();
                    }catch (Exception e){

                    }
            }
        };

        myThread.start();
    }
}
