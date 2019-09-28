package quiz.logo.com.logoquiz.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import androidx.lifecycle.ViewModelProviders;
import quiz.logo.com.logoquiz.R;
import quiz.logo.com.logoquiz.viewmodel.ViewModel;

public class LoginActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private ViewModel mViewModel;
    private final String LOGO_FILE_NAME = "logos.txt";
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewModel = ViewModelProviders.of(this).get(ViewModel.class);
        try {
            InputStream stream = getAssets().open(LOGO_FILE_NAME);
            mViewModel.readFile(stream);
        } catch (IOException e) {
            // Handle exceptions here
        }

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container,GameFragment.class,"GameFragment");
        transaction.addToBackStack(null);
        transaction.commit();

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public ArrayList<String[]> getQuizList() {
        return mViewModel.getQuizList();
    }

    public int getCurrLevel() {
        return mViewModel.getCurrLevel();
    }
}
