package com.afroware.sdgenerator.annotation;

import com.afroware.sdgenerator.SDGeneratorManager;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * Created by lamallam on 28/09/17.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EntityScan
@Import(SDGeneratorManager.class)
public @interface SDGenerator {

    String repositoryPackage() default "";
    String repositoryPostfix() default "Repository";
    String repositorySuperClass() default "JpaRepository";
    Class<?>[] excludeRepositoriesClasses() default {};

    String managerPackage() default "";
    String managerPostfix() default "Manager";
    Class<?>[] excludeManagerClasses() default {};

    @AliasFor(annotation = EntityScan.class, attribute = "basePackages")
    String[] entityPackage() default {};

    boolean debug() default false;

    boolean onlyAnnotations() default false;

    boolean overwrite() default false;
}
