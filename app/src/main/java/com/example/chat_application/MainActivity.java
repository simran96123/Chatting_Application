package com.example.chat_application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.chat_application.Fragments.ChatsFragment;
import com.example.chat_application.Fragments.UsersFragment;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    FirebaseUser firebaseUser;
//    DatabaseReference mRef;

    private  static int AUTH_REQUEST_CODE = 792;

    private  FirebaseAuth firebaseAuth;
   private FirebaseAuth.AuthStateListener  mAuthStateListener;
   private List<AuthUI.IdpConfig> providers;


    @Override
    protected void onStart() {
        super.onStart();
       firebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        if (mAuthStateListener != null)firebaseAuth.removeAuthStateListener(mAuthStateListener);
        super.onStop();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        mRef = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());


//        mRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Users users = dataSnapshot.getValue(Users.class);
//               // Toast.makeText(MainActivity.this, "User Login"+users.getUsername(), Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        //tab layout and view pager
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new ChatsFragment() , "Chats");
        viewPagerAdapter.addFragment(new UsersFragment() , "Users");

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

    }

    //Adding logout functionality


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this , MainActivity.class));

                finish();

                return  true;

        }

        return false;
    }

    private void init()
    {
        providers = Arrays.asList(
                //Apple

                new AuthUI.IdpConfig.EmailBuilder().build()

        );

        firebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //get user
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(MainActivity.this, "You already login with uid"+user.getUid(), Toast.LENGTH_SHORT).show();

                }

                else {
                    //login
                    startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)

                    .build() , AUTH_REQUEST_CODE);
                }
            }
        };
    }

    // class viewpagerAdapter
    class ViewPagerAdapter extends FragmentPagerAdapter{
        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();

        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }



        @Override
        public int getCount() {
            return fragments.size();
        }


        public  void addFragment(Fragment fragment , String title){
            fragments.add(fragment);
            titles.add(title);
        }


        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return  titles.get(position);
        }
    }



}