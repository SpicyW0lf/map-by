package com.example.beannames

import org.springframework.stereotype.Component

@Component
class TaskImplFirst : Task {
    override fun process(): String {
        return "First process task"
    }

    override fun type(): String {
        return "First task"
    }
}