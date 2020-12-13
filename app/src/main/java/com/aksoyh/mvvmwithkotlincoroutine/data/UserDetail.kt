package com.aksoyh.mvvmwithkotlincoroutine.data

data class UserDetail(
    val dateOfBirth: String,
    val email: String,
    val firstName: String,
    val gender: String,
    val id: String,
    val lastName: String,
    val location: Location,
    val phone: String,
    val picture: String,
    val registerDate: String,
    val title: String
)

data class Location(
    val city: String,
    val country: String,
    val state: String,
    val street: String,
    val timezone: String
)