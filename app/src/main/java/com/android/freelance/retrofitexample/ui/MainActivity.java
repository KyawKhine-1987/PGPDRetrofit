package com.android.freelance.retrofitexample.ui;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.freelance.retrofitexample.R;
import com.android.freelance.retrofitexample.data.models.Comments;
import com.android.freelance.retrofitexample.data.models.Posts;
import com.android.freelance.retrofitexample.data.network.MyApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();
    private static String url = "http://jsonplaceholder.typicode.com/";
    TextView result;
    MyApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "TEST : onCreate() is called...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = findViewById(R.id.tvResult);

        //this below code is accepted null value data from network in the real time.
        //Gson gson = new GsonBuilder().serializeNulls().create();

        //troubleshooting on network HTTP status
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                        Log.i(LOG_TAG, "TEST : okhttp3.Response intercept() is called...");

                        Request originalRequest = chain.request();
                        Request newRequest = originalRequest.newBuilder()
                                .header("Interceptor-Header", "xyz")
                                .build();
                        return chain.proceed(newRequest);
                    }
                })
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit r = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                /*.addConverterFactory(GsonConverterFactory.create(gson))*/
                .client(okHttpClient)
                .build();
        api = r.create(MyApi.class);

        /*GET*/
        callWebServiceForPosts();
//        callWebServiceForComments();

        /*POST*/
//        callWebServiceForCreateUser();

        /*PUT*/
//        callWebServiceForUpdatePost();

        /*DELETE*/
//        callWebServiceForDeletePost();
    }

    private void callWebServiceForDeletePost() {
        Log.i(LOG_TAG, "TEST : callWebServiceForDeletePost() is called...");

        Call<Void> call = api.deletePost(5);
        call.enqueue(new Callback<Void>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i(LOG_TAG, "TEST : onResponse() is called...");

                result.setText("Code : " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i(LOG_TAG, "TEST : onFailure() is called...");
                result.setText(t.getMessage());
            }
        });
    }

    private void callWebServiceForUpdatePost() {
        Log.i(LOG_TAG, "TEST : callWebServiceForUpdatePost() is called...");

        Posts posts = new Posts(12, null, "New Text");
        /*Call<Posts> call = api.putPost(5, posts);
        Call<Posts> call = api.putPost( "abc", 5, posts);
        Call<Posts> call = api.patchPost(5, posts);*/
        Map<String, String> headers = new HashMap<>();
        headers.put("Map-Header1", "def");
        headers.put("Map-Header2", "ghi");

        Call<Posts> call = api.patchPost(headers, 5, posts);
        call.enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                Log.i(LOG_TAG, "TEST : onResponse() is called...");

                if (!response.isSuccessful()) {
                    result.setText("Code : " + response.code());
                    return;
                }
                Posts postResponse = response.body();

                //assert posts != null;
                String content = "";
                content += "Code : " + response.code() + "\n";
                content += "------" + "\n";
                content += "ID : " + postResponse.getId() + "\n";
                content += "User ID : " + postResponse.getUserId() + "\n";
                content += "Title : " + postResponse.getTitle() + "\n";
                content += "Body : " + postResponse.getBody() + "\n";
                content += "------" + "\n\n";
                result.setText(content);
            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
                Log.i(LOG_TAG, "TEST : onFailure() is called...");
                result.setText(t.getMessage());
            }
        });
    }

    private void callWebServiceForCreateUser() {
        Log.i(LOG_TAG, "TEST : callWebServiceForCreateUser() is called...");

        Posts posts = new Posts(23, "New Title", "New Text");
        /*Call<Posts> call = api.createPost(posts);*/
        /*Call<Posts> call = api.createPost(23, "New Title", "New Text");*/
        Map<String, String> fields = new HashMap<>();
        fields.put("userId", "25");
        fields.put("title", "New Title");
        fields.put("body", "New Text");
        Call<Posts> call = api.createPost(fields);
        call.enqueue(new Callback<Posts>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                Log.i(LOG_TAG, "TEST : onResponse() is called...");

                if (!response.isSuccessful()) {
                    result.setText("Code : " + response.code());
                    return;
                }
                Posts postResponse = response.body();

                //assert posts != null;
                String content = "";
                content += "Code : " + response.code() + "\n";
                content += "ID : " + postResponse.getId() + "\n";
                content += "User ID : " + postResponse.getUserId() + "\n";
                content += "Title : " + postResponse.getTitle() + "\n";
                content += "Body : " + postResponse.getBody() + "\n\n";

                result.setText(content);
            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
                Log.i(LOG_TAG, "TEST : onFailure() is called...");
                result.setText(t.getMessage());
            }
        });
    }

    private void callWebServiceForComments() {
        Log.i(LOG_TAG, "TEST : callWebServiceForComments() is called...");
        /*Call<List<Comments>> call = api.fetchAllComments(3);*/
        Call<List<Comments>> call = api.fetchAllComments("posts/3/comments");
        call.enqueue(new Callback<List<Comments>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<Comments>> call, Response<List<Comments>> response) {
                Log.i(LOG_TAG, "TEST : onResponse() is called...");

                if (!response.isSuccessful()) {
                    result.setText("Code : " + response.code());
                    return;
                }
                List<Comments> comments = response.body();

                //assert posts != null;
                for (Comments c : comments) {
                    String content = "";
                    content += "ID : " + c.getId() + "\n";
                    content += "Post ID : " + c.getPostId() + "\n";
                    content += "Name : " + c.getName() + "\n";
                    content += "Email: " + c.getEmail() + "\n";
                    content += "Body : " + c.getBody() + "\n\n";

                    result.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Comments>> call, Throwable t) {
                Log.i(LOG_TAG, "TEST : onFailure() is called...");

                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callWebServiceForPosts() {
        Log.i(LOG_TAG, "TEST : callWebServiceForPosts() is called...");

        Map<String, String> parameters = new HashMap<>();
        parameters.put("userId", "1");
        parameters.put("_sort", "id");
        parameters.put("_order", "asc");
        /*Call<List<Posts>> call = api.fetchAllPosts(4, "id", "desc");
        Call<List<Posts>> call = api.fetchAllPosts(new Integer[]{2, 3, 6}, "id", "desc");*/
        Call<List<Posts>> call = api.fetchAllPosts(parameters);
        call.enqueue(new Callback<List<Posts>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<Posts>> call, Response<List<Posts>> response) {
                Log.i(LOG_TAG, "TEST : onResponse() is called...");

                if (!response.isSuccessful()) {
                    result.setText("Code : " + response.code());
                    return;
                }
                List<Posts> posts = response.body();

                //assert posts != null;
                for (Posts p : posts) {
                    String content = "";
                    content += "ID : " + p.getId() + "\n";
                    content += "User ID : " + p.getUserId() + "\n";
                    content += "Title : " + p.getTitle() + "\n";
                    content += "Body : " + p.getBody() + "\n\n";

                    result.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Posts>> call, Throwable t) {
                Log.i(LOG_TAG, "TEST : onFailure() is called...");

                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
