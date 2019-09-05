package com.android.freelance.retrofitexample.data.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

import com.android.freelance.retrofitexample.data.models.Comments;
import com.android.freelance.retrofitexample.data.models.Posts;

import java.util.List;
import java.util.Map;

public interface MyApi {
    /*@GET("posts")
    Call<List<Posts>> fetchAllPosts();*/

    //TODO //List<Integer> and Integer[]...can pass in here. Integer can be null but int can't be.
    /*@GET("posts")
    Call<List<Posts>> fetchAllPosts(@Query("userId") int userId,
                                    @Query("_sort") String sort,
                                    @Query("_order") String order);*/

    @GET("posts")
    Call<List<Posts>> fetchAllPosts(@Query("userId") Integer[] userId,
                                    @Query("_sort") String sort,
                                    @Query("_order") String order);

    @GET("posts")
    Call<List<Posts>> fetchAllPosts(@QueryMap Map<String, String> parameters);

    @GET("posts/{id}/comments")
    Call<List<Comments>> fetchAllComments(@Path("id") int postId);

    @GET
    Call<List<Comments>> fetchAllComments(@Url String url);

    @POST("posts")
    Call<Posts> createPost(@Body Posts posts);

    @FormUrlEncoded
    @POST("posts")
    Call<Posts> createPost(@Field("userId") int userId,
                           @Field("title") String title,
                           @Field("body") String body);

    @FormUrlEncoded
    @POST("posts")
    Call<Posts> createPost(@FieldMap Map<String, String> fields);

    @PUT("posts/{id}")
    Call<Posts> putPost(@Path("id") int id, @Body Posts posts);

    @Headers({"Static-Header1:123", "Static-Header2:456"})
    @PUT("posts/{id}")
    Call<Posts> putPost(
            @Header("Dynamic-Header") String header,
            @Path("id") int id,
            @Body Posts posts);

    @PATCH("posts/{id}")
    Call<Posts> patchPost(@Path("id") int id, @Body Posts posts);

    @PATCH("posts/{id}")
    Call<Posts> patchPost(@HeaderMap Map<String, String> headers,
                          @Path("id") int id,
                          @Body Posts posts);

    @DELETE("posts/{id}")
    Call<Void> deletePost(@Path("id") int id);//It will return an empty body for Delete method and that Void is completely ignore response body from network.
}
