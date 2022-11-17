package com.sharon.sample.mpesa;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

interface InterfaceMethods {
    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseFormServer> loginMethod(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("addUser.php")
    Call<ResponseFormServer> addUsers(@Field("firstname") String firstname, @Field("lastname") String lastname, @Field("email") String email, @Field("department") String department, @Field("role") String role);

    @FormUrlEncoded
    @POST("Results.php")
    Call<ResponseFormServer> addResults(@Field("management") String management,
                                        @Field("politics") String politics,
                                        @Field("project") String project,
                                        @Field("field") String field,
                                        @Field("commskills") String commskills,
                                        @Field("semister") String semister,
                                        @Field("regNo") String regNo);


    @FormUrlEncoded
    @POST("updatePassword.php")
    Call<ResponseFormServer> updatePasswrd(@Field("oldpassword") String oldpassword, @Field("newpassword") String newpassword, @Field("email") String email);


}