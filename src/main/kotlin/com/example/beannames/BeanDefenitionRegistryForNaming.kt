package com.example.beannames

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.getBeansWithAnnotation
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.stereotype.Component
import java.lang.reflect.Constructor

@Component
class BeanDefenitionRegistryForNaming : BeanDefinitionRegistryPostProcessor {
    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
        val provider = ClassPathScanningCandidateComponentProvider(false)

        provider.addIncludeFilter(MapByAnnotationFilter())
        println(provider.findCandidateComponents("com.example.beannames"))
    }

    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {

        for (beanDefName in registry.beanDefinitionNames) {
            val beanDefinition = registry.getBeanDefinition(beanDefName).beanClassName ?: continue
            val beanClass = Class.forName(beanDefinition)
            val methods = beanClass.methods

            for (method in methods) {
                val ann: MapBy = method.getAnnotation(MapBy::class.java) ?: continue
                val cons: Constructor<*> = beanClass.getConstructor()
                val bean = cons.newInstance()
                val name = method.invoke(bean).toString()
                registry.registerBeanDefinition(name, registry.getBeanDefinition(beanDefName))
                registry.removeBeanDefinition(beanDefName)
            }
        }
    }
}