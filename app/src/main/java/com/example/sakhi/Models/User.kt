package com.example.sakhi.Models

data class User(
    var uid: String= "",
    val name: String= "",
    val uniqueId: String= "",
    val superGuardian: String= "",
    val phone: String= "",
    val guardians: List<String> = listOf()
)