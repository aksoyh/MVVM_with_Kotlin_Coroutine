package com.aksoyh.mvvmwithkotlincoroutine.repository

import com.aksoyh.mvvmwithkotlincoroutine.api.RetrofitInstance

class MainRepository {

    suspend fun getUsers() = RetrofitInstance.api.getAllUsers()

    suspend fun getUserDetail(userId: String) = RetrofitInstance.api.getUserDetail(userId)
}