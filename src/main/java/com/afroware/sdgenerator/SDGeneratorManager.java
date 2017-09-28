package com.afroware.sdgenerator;

import java.util.Collection;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import com.afroware.sdgenerator.annotation.SDGenerator;
import com.afroware.sdgenerator.support.ManagerImplTemplateSupport;
import com.afroware.sdgenerator.support.ManagerTemplateSupport;
import com.afroware.sdgenerator.support.RepositoryTemplateSupport;
import com.afroware.sdgenerator.support.ScanningConfigurationSupport;
import com.afroware.sdgenerator.util.GeneratorUtils;
import com.afroware.sdgenerator.util.SDLogger;

/**
 * Created by lamallam on 28/09/17.
 */
public class SDGeneratorManager implements ImportBeanDefinitionRegistrar, EnvironmentAware, ResourceLoaderAware {

    private Environment environment;
    private ResourceLoader resourceLoader;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        Assert.notNull(annotationMetadata, "AnnotationMetadata must not be null!");
        Assert.notNull(beanDefinitionRegistry, "BeanDefinitionRegistry must not be null!");

        if(annotationMetadata.getAnnotationAttributes(SDGenerator.class.getName()) != null) {

            AnnotationAttributes attributes = new AnnotationAttributes(annotationMetadata.getAnnotationAttributes(SDGenerator.class.getName()));

            String repositoryPackage = attributes.getString("repositoryPackage");
            String managerPackage = attributes.getString("managerPackage");
      
            if (!managerPackage.isEmpty() && repositoryPackage.isEmpty()) {
                SDLogger.error("Repositories must be generated before generating managers");
                return;
            }

            if (!repositoryPackage.isEmpty() || !managerPackage.isEmpty()){
                ScanningConfigurationSupport configurationSource = new ScanningConfigurationSupport(annotationMetadata, attributes, this.environment);

                Collection<BeanDefinition> candidates = configurationSource.getCandidates(resourceLoader);

                String absolutePath = GeneratorUtils.getAbsolutePath();
                if (absolutePath == null) {
                    SDLogger.addError("Could not define the absolute path!");
                    return;
                }

                if (!repositoryPackage.isEmpty()){

                    String repositoriesPath = absolutePath + repositoryPackage.replace(".", "/");

                    RepositoryTemplateSupport repositoryTemplateSupport = new RepositoryTemplateSupport(attributes);
                    repositoryTemplateSupport.initializeCreation(repositoriesPath, repositoryPackage, candidates , false);
                }

                if (!repositoryPackage.isEmpty() && !managerPackage.isEmpty()) {

                    String managerPath = absolutePath + managerPackage.replace(".", "/");

                    String repositoryPostfix = attributes.getString("repositoryPostfix");

                    ManagerTemplateSupport managerTemplateSupport = new ManagerTemplateSupport(attributes, repositoryPackage, repositoryPostfix);
                    managerTemplateSupport.initializeCreation(managerPath, managerPackage, candidates ,false);
              

                    ManagerImplTemplateSupport managerImplTemplateSupport = new ManagerImplTemplateSupport(attributes, repositoryPackage, repositoryPostfix);
                    managerImplTemplateSupport.initializeCreation(managerPath, managerPackage, candidates ,true);
                }

                SDLogger.printGeneratedTables(attributes.getBoolean("debug"));
            }

        }
    }

}
