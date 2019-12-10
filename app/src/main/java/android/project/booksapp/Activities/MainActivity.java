package android.project.booksapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.project.booksapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener, BookAdapter.OnItemClickListner {


    private RecyclerView mRecyclerView;
    private BookAdapter mAdapter;
    private DatabaseReference databaseReference;
    private List<Book> mBooks;
    FloatingActionButton fabs1, fabs2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("-------------------------------Student");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        mBooks = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Book_ID");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Book books = postSnapshot.getValue(Book.class);
                    mBooks.add(books);
                }

                mAdapter = new BookAdapter(MainActivity.this, mBooks);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setOnItemClickListner(MainActivity.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });



    }



    @Override
    public void onItemClick(int position) {

        Book viewbook = mBooks.get(position);
        Intent intent = new Intent(MainActivity.this, BookInformaions.class);
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
        Toast.makeText(this, "Users do not have permission to edit Book ID " + position, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDeleteClick(int position) {
        Toast.makeText(this, "Users do not have permission to delete Book ID " + position, Toast.LENGTH_LONG).show();
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
        getMenuInflater().inflate(R.menu.main, menu);

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

        if (id == R.id.privacy) {     startActivity(new Intent(MainActivity.this, Privacy.class));

        }  else if (id == R.id.privacy) {     startActivity(new Intent(MainActivity.this, Privacy.class));

        } else if (id == R.id.about) {  startActivity(new Intent(MainActivity.this, About.class));

        } else if (id == R.id.nav_send) { startActivity(new Intent(MainActivity.this, Contact.class));

        } else if (id == R.id.nav_sendssa) { startActivity(new Intent(MainActivity.this, Feedback.class));

        } else if (id == R.id.nav_sendss) { startActivity(new Intent(MainActivity.this, LogOutMember.class)); finish();

        } else if (id == R.id.nav_Articles) { startActivity(new Intent(MainActivity.this, Articles.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
