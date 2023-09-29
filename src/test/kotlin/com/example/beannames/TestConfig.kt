package com.example.beannames

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order

@Configuration
class TestConfig {
    interface Task {
        fun process(): String
        @MapBy("task")
        fun type(): String
    }

    class TaskImplFirst : Task {
        override fun process(): String {
            return "First process task"
        }

        override fun type(): String {
            return "First task"
        }
    }

    class TaskImplSecond : Task {
        override fun process(): String {
            return "Second process task"
        }

        override fun type(): String {
            return "Second task"
        }
    }

    @Bean
    fun taskImplFirst() = TaskImplFirst()

    @Bean
    fun taskImplSecond() = TaskImplSecond()

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun beanDefenitionRegistryForNaming(): BeanDefenitionRegistryForNaming {
        return BeanDefenitionRegistryForNaming()
    }


}