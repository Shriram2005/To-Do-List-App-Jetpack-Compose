package com.example.compose1.dataClasses

data class TaskData(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    var completed: Boolean = false,
    val createdAt: String = ""
)