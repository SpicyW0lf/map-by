package com.example.beannames

interface Task {
    fun process(): String
    @MapBy("task")
    fun type(): String
}
