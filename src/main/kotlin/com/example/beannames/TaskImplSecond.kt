package com.example.beannames

import org.springframework.stereotype.Component

@Component
class TaskImplSecond : Task {
    override fun process(): String {
        return "Second process task"
    }

    override fun type(): String {
        return "Second task"
    }
}