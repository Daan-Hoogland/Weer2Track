package io.hoogland.weer2track.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.hoogland.weer2track.R;
import io.hoogland.weer2track.databinding.ActivityMainBinding;

/**
 * Activity for the main screen of the application, showing current & forecast weather data.
 *
 * @author dan
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Variable used in the app to keep track of whether or not the data could be loaded
     * successfully.
     */
    public static boolean isOldData = false;

    /**
     * Method called when the {@link AppCompatActivity} is loaded. Initiates parts of the UI that are
     * shared between the fragments, like the navbar.
     *
     * @param savedInstanceState Previously saved state, if applicable (e.g. orientation change)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_forecast, R.id.navigation_currentweather)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
}