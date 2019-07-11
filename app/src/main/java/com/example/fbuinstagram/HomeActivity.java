package com.example.fbuinstagram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.fbuinstagram.fragments.ComposeFragment;
import com.example.fbuinstagram.fragments.HomeFragment;
import com.example.fbuinstagram.fragments.ProfileFragment;
import com.example.fbuinstagram.interfaces.ReturnHomeInterface;

public class HomeActivity extends AppCompatActivity implements ReturnHomeInterface {

    private static final String APP_TAG = "HomeActivity";

    private BottomNavigationView bottomNavigationView;

    private FragmentManager fragmentManager;
    private Fragment currFragment;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fragmentManager = getSupportFragmentManager();


        getSupportActionBar().setTitle("");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.nav_logo_whiteout);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId()){
                    case R.id.action_home:
                        currFragment = new HomeFragment();
                        Toast.makeText(HomeActivity.this, "Home!", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.action_compose:
                        currFragment = new ComposeFragment();
                        Toast.makeText(HomeActivity.this, "Compose!", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.action_profile:
                        currFragment = new ProfileFragment();
                        Toast.makeText(HomeActivity.this, "Profile!", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        currFragment = new HomeFragment();
                        Toast.makeText(HomeActivity.this, "SOMETHING ELSE!", Toast.LENGTH_SHORT).show();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, currFragment).commit();
                return true;
            }
        });

        //Setting default selected item
        bottomNavigationView.setSelectedItemId(R.id.action_home);


    }// end onCreate


    @Override
    public void buttonClicked() {
        Log.d(APP_TAG, "Hey so I actually ended up in my Activity like I should have! :))");
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }
}