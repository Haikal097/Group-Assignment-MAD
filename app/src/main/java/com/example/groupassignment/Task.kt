package com.example.groupassignment

data class Task(
    val id: Long,
    val title: String,
    var subtitle: String,
    var isCompleted: Boolean
)
