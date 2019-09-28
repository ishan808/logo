package quiz.logo.com.logoquiz.viewmodel;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import androidx.lifecycle.AndroidViewModel;
import quiz.logo.com.logoquiz.model.Quiz;

/**
 * Created by ishanvaid on 28/09/19.
 */

public class GameViewModel extends AndroidViewModel {
    private static final int REQ_SIZE = 16;
    private HashMap<String, Bitmap> cache;
    private int nextLevel;
    private final static int CACHE_SIZE = 10;
    private final String ALLCHARS = "abcdefghinjklmnopqrstuvwxyz";
    public GameViewModel(Application application) {
        super(application);
    }


    public Quiz getLevelDetails(String ans){

        if(cache.containsKey(ans)) {
            ArrayList<Character>  cList = new ArrayList<>();
            for(int i = 0;i<ans.length();i++) {
                cList.add(ans.charAt(i));
            }
            while(cList.size() < REQ_SIZE) {
                int index = new Random().nextInt(ALLCHARS.length());
                cList.add(ALLCHARS.charAt(index));
            }
            Collections.shuffle(cList);
            StringBuilder sb = new StringBuilder();

            for (Character ch : cList) {
                sb.append(ch);
            }
            return new Quiz(cache.get(ans),ans,sb.toString());

        }
        return null;
    }
    public void onLevelComplete(String next, String imgUrl){
        ArrayList<String[]> lst = new ArrayList<>();
        lst.add(new String []{next,imgUrl});

        new DownloadFilesTask(lst).execute();


    }

    public void setData(ArrayList<String[]> quizList, int currLevel) {

    }

    public void loadData(ArrayList<String[]> quizList, int currLevel) {
        new DownloadFilesTask(quizList).execute();
    }

    private class DownloadFilesTask extends AsyncTask<Void, Bundle, Void> {
        private ArrayList<String> mAnsList;
        private ArrayList<URL> mUrlList;
        public DownloadFilesTask(ArrayList<String[]> quizList){

            for(String[] str: quizList) {
                mAnsList.add(str[0]);
                try {
                    mUrlList.add(new URL(str[1]));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onProgressUpdate(Bundle... bdl) {
            int ind = bdl[0].getInt("index");
            byte[] byteArray = bdl[0].getByteArray("image");
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            if(cache.size() == CACHE_SIZE){
                //remove from cache oldest entry
                //TODO use linked hash map.
            }
            cache.put(mAnsList.get(ind), bmp);
        }

        @Override
        protected Void doInBackground(Void... urls) {
            int count = urls.length;
            long totalSize = 0;
            for (int i = 0; i < count; i++) {
                try {

                    HttpURLConnection connection = (HttpURLConnection) mUrlList.get(i).openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap bmp = BitmapFactory.decodeStream(input);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    Bundle b = new Bundle();
                    b.putInt("index",i);
                    b.putByteArray("image",byteArray);
                    publishProgress(b);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            return null;
        }


    }
}
