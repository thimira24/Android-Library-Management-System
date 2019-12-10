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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Scanner extends AppCompatActivity {

    private Button scan_button;
    private TextView resultsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentToolbar();
        setContentView(R.layout.scanner);

        initViews();
        buttonAction();
    }

    public void initViews(){
        scan_button = (Button) findViewById(R.id.btn_scan);
        resultsView =(TextView) findViewById(R.id.qrresult);
    }

    public void buttonAction(){

        final Activity activity = this;
        scan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, result.getContents(),Toast.LENGTH_LONG).show();
                resultsView.setVisibility(View.VISIBLE);
                resultsView.setText(result.getContents());

            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
