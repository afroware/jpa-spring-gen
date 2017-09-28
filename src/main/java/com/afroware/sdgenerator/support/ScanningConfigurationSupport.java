package com.afroware.sdgenerator.support;

import com.afroware.sdgenerator.annotation.SDGenerate;
import com.afroware.sdgenerator.annotation.SDNoGenerate;
import com.afroware.sdgenerator.provider.ClassPathScanningProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import javax.persistence.Entity;
import java.util.*;

/**
 * Created by lamallam on 28/09/17.
 */
public class ScanningConfigurationSupport {

    private final Environment environment;
    private final AnnotationAttributes attributes;
    private final AnnotationMetadata annotationMetadata;
    private final String[] entityPackage;
    private final boolean onlyAnnotations;

    public ScanningConfigurationSupport(AnnotationMetadata annotationMetadata, AnnotationAttributes attributes, Environment environment){
        Assert.notNull(environment, "Environment must not be null!");
        Assert.notNull(environment, "AnnotationMetadata must not be null!");
        this.environment = environment;
        this.attributes = attributes;
        this.annotationMetadata = annotationMetadata;
        this.entityPackage = this.attributes.getStringArray("entityPackage");
        this.onlyAnnotations = this.attributes.getBoolean("onlyAnnotations");
    }

    public ScanningConfigurationSupport(String[] entityPackage, boolean onlyAnnotations) {
        this.entityPackage = entityPackage;
        this.onlyAnnotations = onlyAnnotations;
        this.environment = null;
        this.annotationMetadata = null;
        this.attributes = null;
    }

    private Iterable<String> getBasePackages() {

        if (entityPackage.length == 0) {
            String className = this.annotationMetadata.getClassName();
            return Collections.singleton(ClassUtils.getPackageName(className));
        } else {
            HashSet<String> packages = new HashSet<String>();
            packages.addAll(Arrays.asList(entityPackage));

            return packages;
        }
    }

    public Collection<BeanDefinition> getCandidates(ResourceLoader resourceLoader) {
        if(this.getBasePackages() == null){
            return Collections.emptyList();
        }

        ClassPathScanningProvider scanner = new ClassPathScanningProvider();
        scanner.setResourceLoader(resourceLoader);
        if (environment != null) {
            scanner.setEnvironment(this.environment);
        }

        scanner.setIncludeAnnotation(SDGenerate.class);
        scanner.setExcludeAnnotation(SDNoGenerate.class);
        if (!onlyAnnotations) {
            scanner.setIncludeAnnotation(Entity.class);
        }

        Iterator<String> filterPackages = this.getBasePackages().iterator();

        HashSet<BeanDefinition> candidates = new HashSet<BeanDefinition>();

        while(filterPackages.hasNext()) {
            String basePackage = (String)filterPackages.next();
            Set<BeanDefinition> candidate = scanner.findCandidateComponents(basePackage);
            candidates.addAll(candidate);
        }

        return candidates;
    }
}

