package quiz.logo.com.logoquiz.model;

import android.graphics.Bitmap;

/**
 * Created by ishanvaid on 28/09/19.
 */

public class Quiz {
    private Bitmap mImage;
    private String mName;
    private String mJumbledName;

    public Quiz(Bitmap image, String name, String jName){
        mImage = image;
        mName = name;
        mJumbledName = jName;
    }

    public Bitmap getImage(){
        return mImage;
    }
    public String getName(){
        return mName;
    }
    public String getjName(){
        return mJumbledName;
    }
}
