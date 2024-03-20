package com.example.postersmaker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.postersmaker.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    HomeFragment homeFragment;
    SettingFragment settingFragment;
    HelpFragment helpFragment;
    ProjectsFragment projectsFragment;
    NewFragment newFragment;
    MeowBottomNavigation bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        homeFragment = new HomeFragment();
        helpFragment = new HelpFragment();
        projectsFragment = new ProjectsFragment();
        settingFragment = new SettingFragment();
        newFragment = new NewFragment();

        setFragment(homeFragment);

        bottomNavigation.show(1, true);
        setFragment(homeFragment);


        // add your bottom navigation icon here
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.home_icon));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.baseline_settings_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.baseline_add_circle_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.baseline_help_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(5, R.drawable.project_icon));

        bottomNavigation.setOnClickMenuListener(model -> {
            Fragment selectedFragment = null;

            switch (model.getId()) {
                case 1:
                    selectedFragment = homeFragment;
                    break;
                case 2:
                    selectedFragment = settingFragment;
                    break;
                case 3:
                    selectedFragment = newFragment;
                    break;
                case 4:
                    selectedFragment = helpFragment;
                    break;
                case 5:
                    selectedFragment = projectsFragment;

            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
            }
            return null;
        });
    }
    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }
}
