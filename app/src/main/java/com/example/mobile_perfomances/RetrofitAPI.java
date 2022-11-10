package com.example.mobile_perfomances;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface RetrofitAPI {
    @POST("Performances")
    Call<Perfom> createPost(@Body Perfom dataModal);

    @PUT("Performances/")
    Call<Perfom> createPut(@Body Perfom dataModal, @Query("ID") int id);

    @DELETE("Performances/")
    Call<Perfom> createDelete(@Query("id") int id);
}
