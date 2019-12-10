package android.project.booksapp.Activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.project.booksapp.R;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class BookInformaions extends AppCompatActivity {

    private TextView bookname, bookauthor, bookisbn, booklocation, bookdate, bookcopies, bookcategory, bookdescription;
    private ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentToolbar();
        setContentView(R.layout.book_informaions);

        initViews();


    }

    public void initViews(){

        bookname = findViewById(R.id.Book_title_id_view);
        bookauthor = findViewById(R.id.Book_author_id_view);
        bookisbn = findViewById(R.id.Book_isbn_id_view);
        booklocation = findViewById(R.id.Book_location_id_view);

        bookdate = findViewById(R.id.Book_date_id_view);
        bookcopies= findViewById(R.id.Book_copies_id_view);
        bookcategory = findViewById(R.id.Book_category_id_view);
        bookdescription = findViewById(R.id.Book_description_id_view);
        imageview = findViewById(R.id.imageinfo);



        bookname.setText(getIntent().getStringExtra("bname1"));
        bookauthor.setText(getIntent().getStringExtra("bauthor1"));
        bookisbn.setText(getIntent().getStringExtra("bookisbn1"));
        booklocation.setText(getIntent().getStringExtra("location1"));
        bookdate.setText(getIntent().getStringExtra("date1"));
        bookcopies.setText(getIntent().getStringExtra("copies1"));
        bookcategory.setText(getIntent().getStringExtra("category1"));
        bookdescription.setText(getIntent().getStringExtra("edition1"));
        Picasso.with(this).load(getIntent().getExtras().getString("image"));


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
