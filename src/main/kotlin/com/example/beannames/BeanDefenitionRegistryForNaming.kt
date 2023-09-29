package com.example.beannames

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.getBean
import org.springframework.beans.factory.getBeansWithAnnotation
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.boot.autoconfigure.AutoConfigurationPackage
import org.springframework.boot.autoconfigure.AutoConfigurationPackages
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.stereotype.Component
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import kotlin.contracts.contract

@Component
class BeanDefenitionRegistryForNaming : BeanDefinitionRegistryPostProcessor {

    private val names = HashMap<String, Method>()

    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
        beanFactory.beanNamesIterator.forEach {
            val bean = beanFactory.getBean(it)
            if (names.contains(bean.javaClass.name)) {
                beanFactory.registerSingleton(names[bean.javaClass.name]?.invoke(bean).toString(), bean)
                (beanFactory as BeanDefinitionRegistry).removeBeanDefinition(it)
            }
        }
    }

    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {
        for (beanDefName in registry.beanDefinitionNames) {
            val beanDefinition = registry.getBeanDefinition(beanDefName).beanClassName ?: continue
            val beanClass = Class.forName(beanDefinition)
            val methodInClass = isInterfacesHaveAnnotation(beanClass)
            if (methodInClass != null) {
                names[beanDefName] = methodInClass
            }

            for (innerClass in beanClass.classes) {
                val method = isInterfacesHaveAnnotation(innerClass)
                if (method != null) {
                    names[innerClass.name] = method
                }
            }

        }
    }

    private fun isInterfacesHaveAnnotation(clazz: Class<*>): Method? {
        for (method in clazz.methods) {
            if (method.isAnnotationPresent(MapBy::class.java)) {
                return method
            }
        }

        for (interfac in clazz.interfaces) {
            val method = isInterfacesHaveAnnotation(interfac)
            if (method != null) {
                return method
            }
        }

        return null
    }
}