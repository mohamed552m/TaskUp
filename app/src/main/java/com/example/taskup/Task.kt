package com.example.taskup

data class Task(
    val title: String,
    val description: String,
    val day: String,
    var isCompleted: Boolean = false
)