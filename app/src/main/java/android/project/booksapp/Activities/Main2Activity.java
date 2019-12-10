package android.project.booksapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.project.booksapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
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

public class Main2Activity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener, BookAdapter.OnItemClickListner {

    private RecyclerView mRecyclerView;
    private BookAdapter mAdapter;
    private DatabaseReference databaseReference;
    private FirebaseStorage mStorage;
    private List<Book> mBooks;
    private ValueEventListener mDBListner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        System.out.println("-------------------------------Admin");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main2Activity.this, BookRegister.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mRecyclerView = findViewById(R.id.recycler_view_id);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // use edit code here


        mBooks = new ArrayList<>();
        // add here rm code
        databaseReference = FirebaseDatabase.getInstance().getReference("Book_ID");

        mDBListner = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mBooks.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Book books = postSnapshot.getValue(Book.class);
                    books.setKey(postSnapshot.getKey());
                    mBooks.add(books);
                }

                mAdapter = new BookAdapter(Main2Activity.this, mBooks);
                mRecyclerView.setAdapter(mAdapter);

                mAdapter.setOnItemClickListner(Main2Activity.this);
                mStorage = FirebaseStorage.getInstance();

                //mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Main2Activity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }




    @Override
    public void onItemClick(int position) {

        Book viewbook = mBooks.get(position);
        Intent intent = new Intent(Main2Activity.this, BookInformaions.class);
        intent.putExtra("bname1", viewbook.getBookname());
        intent.putExtra("bauthor1", viewbook.getBookauthor());
        intent.putExtra("bookisbn1", viewbook.getBookisbn());
        intent.putExtra("date1", viewbook.getBookdate());
        intent.putExtra("copies1", viewbook.getBookcopies());
        intent.putExtra("category1", viewbook.getBookcategory());
        intent.putExtra("location1", viewbook.getBooklocation());
        intent.putExtra("edition1", viewbook.getBookedition());
        intent.putExtra("image", viewbook.getmImageUrl());
        startActivity(intent);
    }

    @Override
    public void onEditClick(int position) {

        Book editbook = mBooks.get(position);
        Intent intent = new Intent(Main2Activity.this, EditBookActivity.class);
        intent.putExtra("bname", editbook.getBookname());
        intent.putExtra("bauthor", editbook.getBookauthor());
        intent.putExtra("bookisbn", editbook.getBookisbn());
        intent.putExtra("date", editbook.getBookdate());
        intent.putExtra("copies", editbook.getBookcopies());
        intent.putExtra("category", editbook.getBookcategory());
        intent.putExtra("location", editbook.getBooklocation());
        intent.putExtra("edition", editbook.getBookedition());
        intent.putExtra("image", editbook.getmImageUrl());
        startActivity(intent);




    }

    @Override
    public void onDeleteClick(int position) {
        Book selectedIem = mBooks.get(position);
        final String selectedKey = selectedIem.getKey();

        StorageReference imageref = mStorage.getReferenceFromUrl(selectedIem.getmImageUrl());
        imageref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.child(selectedKey).removeValue();
                Toast.makeText(Main2Activity.this, "Deleted Successfully", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(mDBListner);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);

        MenuItem SearchItem = menu.findItem(R.id.Action_search);
        SearchView searchView = (SearchView) SearchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);
                return false;
            }
        });

        // search author
        MenuItem SearchAuthor = menu.findItem(R.id.Action_Author_search);

        SearchView searchAuthor = (SearchView)  SearchAuthor.getActionView();
        searchAuthor.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchAuthor.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getAuthorFilter().filter(s);
                return false;
            }
        });

        // search location
        MenuItem SeachLocation = menu.findItem(R.id.Action_ISBN_search);
        SearchView seachfloor = (SearchView) SeachLocation.getActionView();
        seachfloor.setImeOptions(EditorInfo.IME_ACTION_DONE);
        seachfloor.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getLocationFilter().filter(s);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_scanner) {  startActivity(new Intent(Main2Activity.this, Scanner.class));

        } else if (id == R.id.nav_about) { startActivity(new Intent(Main2Activity.this, About.class));

        }  else if (id == R.id.nav_postz) {  startActivity(new Intent(Main2Activity.this, userView.class));

        } else if (id == R.id.nav_feedback) { startActivity(new Intent(Main2Activity.this, SeeFeedback.class));

        } else if (id == R.id.nav_logout) { startActivity(new Intent(Main2Activity.this, LogOutAdmin.class)); finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
