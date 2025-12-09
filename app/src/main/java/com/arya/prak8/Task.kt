package com.arya.prak8

data class Task(
    var id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val date: String? = null,
    var isCompleted: Boolean = false
)