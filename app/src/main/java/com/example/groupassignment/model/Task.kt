package com.example.groupassignment.model

import java.util.UUID

data class Task(
    val id: String = UUID.randomUUID().toString(), // Default value prevents "No value passed" errors
    val title: String,
    val subtitle: String = "",
    var isCompleted: Boolean = false
)