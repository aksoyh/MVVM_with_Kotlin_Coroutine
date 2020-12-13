package com.aksoyh.mvvmwithkotlincoroutine.data

data class Users(
    val `data`: List<UserList>,
    val limit: Int,
    val offset: Int,
    val page: Int,
    val total: Int
)

data class UserList(
    val email: String,
    val firstName: String,
    val id: String,
    val lastName: String,
    val picture: String,
    val title: String
)