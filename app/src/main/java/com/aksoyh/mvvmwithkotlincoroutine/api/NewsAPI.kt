package com.aksoyh.mvvmwithkotlincoroutine.api

import com.aksoyh.mvvmwithkotlincoroutine.data.UserDetail
import com.aksoyh.mvvmwithkotlincoroutine.data.Users
import com.aksoyh.mvvmwithkotlincoroutine.util.Constants
import retrofit2.Response
import retrofit2.http.*

interface NewsAPI {

    @GET(Constants.getAllUser)
    suspend fun getAllUsers(): Response<Users>

    @GET(Constants.getUserDetail)
    suspend fun getUserDetail(@Path("userId") userId: String): Response<UserDetail>

}