package android.project.booksapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.project.booksapp.R;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class userView extends AppCompatActivity implements AccountAdapter.OnItemClickListner {

    private RecyclerView mRecyclerView;
    private FloatingActionButton addlink;
    private AccountAdapter mAdapter;
    private DatabaseReference databaseReference;
    private List<CreateAccountModel> mAccount;

    private ValueEventListener mDBListner;
    private FirebaseStorage mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentToolbar();
        setContentView(R.layout.activity_user_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        fabaction();

        mRecyclerView = findViewById(R.id.recycler_view_idz);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAccount = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("account");

        mDBListner = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mAccount.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    CreateAccountModel accountModel = postSnapshot.getValue(CreateAccountModel.class);
                    accountModel.setmKey(postSnapshot.getKey());
                    mAccount.add(accountModel);
                }

                mAdapter = new AccountAdapter(userView.this, mAccount);
                mRecyclerView.setAdapter(mAdapter);

                //
                mAdapter.setOnItemClickListner(userView.this);
                mStorage = FirebaseStorage.getInstance();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(userView.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void fabaction(){


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(userView.this, UserRegistration.class));
            }
        });

    }




    @Override
    public void onItemClick(int position) {

        CreateAccountModel viewAccount = mAccount.get(position);
        Intent intent = new Intent(userView.this, AccountInformations.class);

        intent.putExtra("membername", viewAccount.getName());
        intent.putExtra("hometown", viewAccount.getAddress());
        intent.putExtra("email", viewAccount.getEmail());
        intent.putExtra("birthday", viewAccount.getBirthday());
        intent.putExtra("phone", viewAccount.getPhone());
        intent.putExtra("username", viewAccount.getUsername());
        intent.putExtra("password", viewAccount.getPassword());
        startActivity(intent);

    }

    @Override
    public void onDeleteClick(int position) {

        CreateAccountModel selectedItem = mAccount.get(position);
        final String selectedKey = selectedItem.getmKey();
        StorageReference imageref = mStorage.getReferenceFromUrl(selectedItem.getProfile());
        imageref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println(selectedKey+"------------------------------------------------------");
                databaseReference.child(selectedKey).removeValue();
                Toast.makeText(userView.this, "User account deleted successfully", Toast.LENGTH_LONG).show();
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

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(userView.this, Main2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
