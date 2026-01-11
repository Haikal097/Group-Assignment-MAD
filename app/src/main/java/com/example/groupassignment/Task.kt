package com.example.groupassignment

import java.util.UUID

// Keep it simple for now.
// Role B (You) will add JSON annotations later in your branch.
data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val dueDate: String,
    val isCompleted: Boolean = false
)