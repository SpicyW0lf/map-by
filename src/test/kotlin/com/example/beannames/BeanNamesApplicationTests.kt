package com.example.beannames

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
class BeanNamesApplicationTests {

    @Autowired
    private lateinit var task1: TaskImplFirst
    @Autowired
    private lateinit var task2: TaskImplSecond
    @Autowired
    private lateinit var map: Map<String, Task>

    @Test
    fun contextLoads() {
    }

    @Test
    fun beanDefinitionRegistry_registredCorrectBeans() {
        assert(task1 == map[task1.type()])
        assert(task2 == map[task2.process()])
    }

    @Test
    fun whenCustomFilterIsUsed_thenShouldFindOneBean() {
        val applicationContext: ApplicationContext = AnnotationConfigApplicationContext(BeanNamesApplication::class.java)

        val beans: List<String> = applicationContext.beanDefinitionNames.filter {
            bean -> !bean.contains("org.springframework")
                && !bean.contains("beanNamesApplication")
        }.toList()

        assert(beans.contains(""))
    }
}
