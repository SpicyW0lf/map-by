package com.example.beannames

interface MappedInter {
    @MapBy(name = "duda")
    fun check(): String {
        return "cola"
    }
}