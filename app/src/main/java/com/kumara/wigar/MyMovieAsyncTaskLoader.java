package com.kumara.wigar;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MyMovieAsyncTaskLoader extends AsyncTaskLoader <ArrayList<MovieList>> {

    private ArrayList <MovieList> mData;
    private boolean mHasResult = false;
    private static final  String API_KEY = "275c653343120c79bde7cb54f7e453c4";


    private String mMovieTitle;

    public MyMovieAsyncTaskLoader(final Context context, String MovieTitle) {
        super(context);

        onContentChanged();
        this.mMovieTitle = MovieTitle;
    }

    @Override
    protected void onStartLoading (){
        if (takeContentChanged())forceLoad();
        else if (mHasResult)deliverResult(mData);
    }

    public void deliverResult (ArrayList<MovieList>data){
        mData = data;
        mHasResult = true;
        super.deliverResult(data);
    }

    @Override
    protected void onReset (){
        super.onReset();
        onStopLoading();
        if (mHasResult){
            onReleaseResources (mData);
            mData = null;
            mHasResult = false;
        }
    }

    private void onReleaseResources(ArrayList<MovieList> mData) {
    }

    @Override
    public ArrayList<MovieList> loadInBackground() {
        SyncHttpClient client = new SyncHttpClient();

        final ArrayList <MovieList> movieLists = new ArrayList<>();
        String url0 = "";
        String url1 = "https://api.themoviedb.org/3/movie/now_playing?api_key="+API_KEY;
        String url2 = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&language=en-US&query=" + mMovieTitle;
        if (TextUtils.isEmpty(mMovieTitle)){  //jika value searchfield kosong, maka url0 bernilai url1 (Now_Playing)
            url0 = url1;
        }else {
            url0 = url2;
        }

        client.get(url0, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("MEMBACA URL", "onSuccess: ALHAMDULILLAH");
                try {

                    JSONObject object = new JSONObject(new String(responseBody));
                    JSONArray result = object.getJSONArray("results");
                    // movieLists.clear();

                    for (int i = 0; i < result.length(); i++){
                        JSONObject movieitem = result.getJSONObject(i);
                        String id = movieitem.getString("id");
                        String vote_average = movieitem.getString("vote_average");
                        String title = movieitem.getString("title");
                        String poster_path = movieitem.getString("poster_path");
                        JSONArray genre = movieitem.getJSONArray("genre_ids");
                        String genre_ids = genre.toString();
                        String backdrop_path = movieitem.getString("backdrop_path");
                        String original_language = movieitem.getString("original_language");
                        String overview = movieitem.getString("overview");
                        String release_date = movieitem.getString("release_date");

                        MovieList newMovie = new MovieList(id,vote_average,title,poster_path,original_language,genre_ids,overview,release_date, backdrop_path);
                        movieLists.add(newMovie);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        return movieLists;
    }


}