package quiz.logo.com.logoquiz.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.util.JsonReader;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.lifecycle.AndroidViewModel;

/**
 * Created by ishanvaid on 28/09/19.
 */

public class ViewModel extends AndroidViewModel {

    private ArrayList<String[]> mQuizList = new ArrayList<>();
    int mCurrLevel;
    public ViewModel(Application application) {
        super(application);

    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void readFile(InputStream in) {

        try (JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"))) {
            reader.beginArray();
            while (reader.hasNext()) {
                reader.beginObject();
                while (reader.hasNext()) {
                    String name = reader.nextName();
                    String key = "";
                    String imgUrl = "";
                    String time = "";
                    if (name.equals("name")) {
                        key = reader.nextString();
                    } else if (name.equals("imgUrl")) {
                        imgUrl = reader.nextString();
                    } else if (name.equals("time")) { //time for the timer to be run for this quiz
                        time = reader.nextString();
                    } else {
                        reader.skipValue();
                    }
                    mQuizList.add(new String[]{key,imgUrl});
                }
            }
            reader.endArray();

        } catch(IOException e){

        }
        getUserProgress();


    }

    public void getUserProgress(){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.getApplication());
        mCurrLevel = settings.getInt("currLevel", 0);


    }


    public ArrayList<String[]> getQuizList() {
        return mQuizList;
    }

    public int getCurrLevel() {
        return mCurrLevel;
    }
}
