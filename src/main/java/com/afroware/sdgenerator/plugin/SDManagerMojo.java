package com.afroware.sdgenerator.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import com.afroware.sdgenerator.support.ManagerImplTemplateSupport;
import com.afroware.sdgenerator.support.ManagerTemplateSupport;
import com.afroware.sdgenerator.support.ScanningConfigurationSupport;
import com.afroware.sdgenerator.util.Constants;
import com.afroware.sdgenerator.util.CustomResourceLoader;
import com.afroware.sdgenerator.util.GeneratorUtils;
import com.afroware.sdgenerator.util.SDLogger;
import com.afroware.sdgenerator.util.SDMojoException;

/**
 *  Created by lamallam on 28/09/17.
 */
@Mojo(name = "managers")
@Execute(phase = LifecyclePhase.COMPILE)
public class SDManagerMojo extends CommonsMojo{

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        SDLogger.configure(getLog());

        this.validateField(Constants.ENTITY_PACKAGE);
        this.validateField(Constants.MAGANER_PACKAGE);
        this.validateField(Constants.REPOSITORY_PACKAGE);

        try {
            CustomResourceLoader resourceLoader = new CustomResourceLoader(project);
            resourceLoader = new CustomResourceLoader(project);
            resourceLoader.setPostfix(managerPostfix);
            resourceLoader.setRepositoryPackage(repositoryPackage);
            resourceLoader.setRepositoryPostfix(repositoryPostfix);
            resourceLoader.setOverwrite(overwrite);
            
            String absolutePath = GeneratorUtils.getAbsolutePath(managerPackage);
            if (absolutePath == null){
                SDLogger.addError("Could not define the absolute path of the managers");
                throw new SDMojoException();
            }

            ScanningConfigurationSupport scanningConfigurationSupport = new ScanningConfigurationSupport(entityPackage, onlyAnnotations);

            ManagerTemplateSupport managerTemplateSupport = new ManagerTemplateSupport(resourceLoader);
            managerTemplateSupport.initializeCreation(absolutePath, managerPackage, scanningConfigurationSupport.getCandidates(resourceLoader),false);
            
            ManagerImplTemplateSupport managerImplTemplateSupport = new ManagerImplTemplateSupport(resourceLoader);
            managerImplTemplateSupport.initializeCreation(absolutePath, managerPackage, scanningConfigurationSupport.getCandidates(resourceLoader),true); 

            SDLogger.printGeneratedTables(true);

        } catch (Exception e) {
            SDLogger.addError(e.getMessage());
            throw new SDMojoException(e.getMessage(), e);
        }
    }
    
}
