package com.example.beannames

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ComponentScan.Filter
import org.springframework.context.annotation.FilterType

@SpringBootApplication
@ComponentScan(includeFilters = [ Filter(type = FilterType.CUSTOM, classes = [MapByAnnotationFilter::class]) ])
class BeanNamesApplication()

    fun main(args: Array<String>) {
        runApplication<BeanNamesApplication>(*args)
    }

