package com.afroware.sdgenerator.plugin;

import com.afroware.sdgenerator.support.RepositoryTemplateSupport;
import com.afroware.sdgenerator.support.ScanningConfigurationSupport;
import com.afroware.sdgenerator.util.*;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.*;

/**
 *  Created by lamallam on 28/09/17.
 */
@Mojo(name = "repositories")
@Execute(phase = LifecyclePhase.COMPILE)
public class SDRepositoryMojo extends CommonsMojo{

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        SDLogger.configure(getLog());

        this.validateField(Constants.ENTITY_PACKAGE);
        this.validateField(Constants.REPOSITORY_PACKAGE);

        try {
            CustomResourceLoader resourceLoader = new CustomResourceLoader(project);
            resourceLoader = new CustomResourceLoader(project);
            resourceLoader.setPostfix(repositoryPostfix);
            resourceLoader.setRepositorySuperClassName(repositorySuperClass);
            resourceLoader.setOverwrite(overwrite);

            String absolutePath = GeneratorUtils.getAbsolutePath(repositoryPackage);
            if (absolutePath == null){
                SDLogger.addError("Could not define the absolute path of the repositories");
                throw new SDMojoException();
            }

            ScanningConfigurationSupport scanningConfigurationSupport = new ScanningConfigurationSupport(entityPackage, onlyAnnotations);

            RepositoryTemplateSupport repositoryTemplateSupport = new RepositoryTemplateSupport(resourceLoader);
            repositoryTemplateSupport.initializeCreation(absolutePath, repositoryPackage, scanningConfigurationSupport.getCandidates(resourceLoader),false);

            SDLogger.printGeneratedTables(true);

        } catch (Exception e) {
            SDLogger.addError(e.getMessage());
            throw new SDMojoException(e.getMessage(), e);
        }
    }
    
}
