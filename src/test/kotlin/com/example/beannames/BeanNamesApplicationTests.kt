package com.example.beannames

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(classes = [TestConfig::class])
class BeanNamesApplicationTests {

    @Autowired
    private lateinit var task1: TestConfig.TaskImplFirst

    @Autowired
    private lateinit var task2: TestConfig.TaskImplSecond

    @Autowired
    private lateinit var map: Map<String, TestConfig.Task>

    @Test
    fun contextLoads() {
    }

    @Test
    fun beanDefinitionRegistry_registredCorrectBeans() {
        assert(task1 == map[task1.type()])
        assert(task2 == map[task2.type()])
    }

    @Test
    fun whenCustomFilterIsUsed_thenShouldFindOneBean() {
//        val applicationContext: ApplicationContext =
//            AnnotationConfigApplicationContext(BeanNamesApplication::class.java)
//
//        applicationContext.getBeansWithAnnotation(ComponentScan::class.java).forEach { (name, instance) ->
//            val scans: Set<ComponentScan> =
//                AnnotatedElementUtils.getMergedRepeatableAnnotations(instance.javaClass, ComponentScan::class.java)
//            //println(scans.size)
//            scans.forEach { el -> println(el.basePackageClasses.size) }
    }

}
