package com.example.beannames

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.getBeansWithAnnotation
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.context.annotation.ComponentScan
import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.core.type.filter.AnnotationTypeFilter
import org.springframework.stereotype.Component
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

//@Component
class MapByBeanScanner : BeanFactoryPostProcessor, ApplicationContextAware {

    private lateinit var applicationContext: ApplicationContext

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }

    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
        val beanDefinitionScanner = ClassPathScanningCandidateComponentProvider(false)//ClassPathBeanDefinitionScanner()
        beanDefinitionScanner.addIncludeFilter(MapByAnnotationFilter())
        val scannerSet = beanDefinitionScanner.findCandidateComponents("com.example.beannames")

        val candidateInterfaces = scannerSet//beanDefinitionScanner.registry.beanDefinitionNames
        //  Class.forName(it).kotlin as? KClass<*>
            .mapNotNull { Class.forName(it.beanClassName)}
            //.filter { it.isInterface }
        //println(candidateInterfaces)

        candidateInterfaces.forEach { candidateInterface ->
            val beanNames = applicationContext.getBeanNamesForType(candidateInterface::class.java)
            for (beanName in beanNames) {
                val bean = applicationContext.getBean(beanName)
                val keyNameMethod = candidateInterface.methods
                    .filterIsInstance<KFunction<*>>()
                    .firstOrNull { it.annotations.any { it is MapBy } }

                if (keyNameMethod != null) {
                    val keyName = keyNameMethod.call(bean) as String
                    beanFactory.registerSingleton(keyName, bean)
                }
            }
        }
    }
}