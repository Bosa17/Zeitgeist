package in.zeitgeist.testapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import in.zeitgeist.testapp.utils.BottomNavigationUtils;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    static NavigationController navigationController;
    private final int[] PAGE_IDS = {
            R.id.navigationComponentPageAFragment,
            R.id.Profile_fragment,
            R.id.Search_fragment,
            R.id.Settings_fragment
    };

    private NavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        PageNavigationView mNavigation = findViewById(R.id.navigation);

        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);

        initBottomNavigation(mNavigation);
        setMessage();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser== null){
            loginOrRegisterUser();
        }
    }

    private void loginOrRegisterUser(){
        Intent intent = new Intent(this,User_login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void initBottomNavigation(PageNavigationView pageNavigationView) {
        navigationController = pageNavigationView.material()
                .addItem(R.drawable.ic_home_black_24dp, "Home")
                .addItem(R.drawable.ic_account_circle_black_24dp, "Profile")
                .addItem(R.drawable.ic_search_black_24dp, "Explore")
                .addItem(R.drawable.ic_settings_black_24dp, "Settings")
                .build();
        BottomNavigationUtils.setupWithNavController(PAGE_IDS, navigationController, mNavController);
    }
    private void setMessage(){
        navigationController.setHasMessage(0,true);
    }

}
