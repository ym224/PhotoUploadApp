package com.yuniemao.photouploadapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "Main";

    private String searchString;
    private MenuItem login;
    private MenuItem logout;
    private SearchView searchBar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentPagerAdapter viewPagerAdapter;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = FirebaseAuth.getInstance().getCurrentUser();

        final TabsFragment photoFragment = new TabsFragment();
        final TabsFragment processedPhotoFragment = new TabsFragment();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            searchString = bundle.getString("searchString");
            Log.d(TAG, "search string is " + searchString);
            photoFragment.setQuery(searchString);
            processedPhotoFragment.setQuery(searchString);
        }

        viewPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()){
            private final Fragment[] mFragments = new Fragment[] {photoFragment, processedPhotoFragment};
            private final String[] mFragmentNames = {"PHOTOS", "ASCII PHOTOS"};
            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }
            @Override
            public int getCount() {
                return mFragments.length;
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };

        viewPager = findViewById(R.id.container);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        findViewById(R.id.new_photo_button).setOnClickListener(this);
        searchBar = findViewById(R.id.search_bar);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                showSearchImages(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    public void showSearchImages(String query){
        Log.d(TAG, "in show search with query " + query);
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("searchString", query); // pass search query to intent
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        login = menu.getItem(0);
        logout = menu.getItem(1);
        if (user != null) {
            login.setVisible(false);
            logout.setVisible(true);
        }
        else {
            login.setVisible(true);
            logout.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }
        else if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            login.setVisible(true);
            logout.setVisible(false);
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.new_photo_button) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                startActivity(new Intent(this, ImageUploadActivity.class));
                finish();
            }
            else {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        }
    }
}
