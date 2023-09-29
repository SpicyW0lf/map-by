package com.example.beannames

import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.context.support.beans
import org.springframework.stereotype.Component
import java.lang.reflect.Method

@Component
class BeanDefinitionFactoryForNaming : BeanFactoryPostProcessor {

    private val mapOfInters = mutableMapOf<String, Pair<Method, MutableSet<String>>>()

    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
        beanFactory.beanNamesIterator.forEach {
            val beanClass = beanFactory.getBean(it).javaClass
            val interName = isInterfaceHasAnnotatedMethods(beanClass)
            if (interName != null) {
                mapOfInters[interName]?.second?.add(it)
            }
        }

        mapOfInters.forEach {(key, value) ->
            val map = mutableMapOf<String, Any>()
            value.second.forEach {
                val bean = beanFactory.getBean(it)
                map[value.first.invoke(bean).toString()] = bean
            }

            beanFactory.registerSingleton(key, map)
        }
    }

    private fun isInterfaceHasAnnotatedMethods(clazz: Class<*>): String? {
        var interName: String? = null

        if (!clazz.isInterface) {
            for (inter in clazz.interfaces) {
                interName = isInterfaceHasAnnotatedMethods(inter)
                if (interName != null){
                    break
                }
            }

            if (interName != null) {

                return interName
            }
        } else {
            for (method in clazz.methods) {
                if (method.isAnnotationPresent(MapBy::class.java)) {
                    var annName = method.getAnnotation(MapBy::class.java).name
                    if (annName == "") {
                        annName = method.name
                    }
                    val key = clazz.name.substring(clazz.name.indexOf('$') + 1).replaceFirstChar { it.lowercase() } + "By" + annName.replaceFirstChar { it.uppercase() }
                    if (mapOfInters.containsKey(key)) {
                        return key
                    }
                    mapOfInters[key] = Pair(method, mutableSetOf())

                    return key
                }
            }

            for (inter in clazz.interfaces) {
                interName = isInterfaceHasAnnotatedMethods(inter)
                if (interName != null) return interName
            }
        }

        return null
    }
}