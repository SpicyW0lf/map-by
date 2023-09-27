package com.example.beannames

import org.springframework.core.type.AnnotationMetadata
import org.springframework.core.type.classreading.MetadataReader
import org.springframework.core.type.classreading.MetadataReaderFactory
import org.springframework.core.type.filter.AnnotationTypeFilter

class MapByAnnotationFilter: AnnotationTypeFilter(MapBy::class.java) {
    override fun matchSelf(metadataReader: MetadataReader): Boolean {
        if (metadataReader.classMetadata.isInterface) return false

        val metadata: AnnotationMetadata = metadataReader.annotationMetadata
        if (metadata.hasAnnotatedMethods(annotationType.name)) return true

        for (intName in metadataReader.classMetadata.interfaceNames) {
            val clazz = Class.forName(intName)
            if (isInterfacesHaveAnnotation(clazz)) return true
        }

        return false
    }

    private fun isInterfacesHaveAnnotation(clazz: Class<*> ): Boolean {
        for (method in clazz.methods) {
            if (method.isAnnotationPresent(annotationType)) {
                return true
            }
        }

        for (interfac in clazz.interfaces) {
            if (isInterfacesHaveAnnotation(interfac)) {
                return true
            }
        }

        return false
    }
}