package com.afroware.sdgenerator.support;

import java.io.File;
import java.util.Arrays;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.annotation.AnnotationAttributes;

import com.afroware.sdgenerator.provider.AbstractTemplateProvider;
import com.afroware.sdgenerator.support.maker.ManagerImplStructure;
import com.afroware.sdgenerator.util.CustomResourceLoader;
import com.afroware.sdgenerator.util.GeneratorUtils;
import com.afroware.sdgenerator.util.Tuple;

/**
 * Created by lamallam on 28/09/17.
 */
public class ManagerImplTemplateSupport extends AbstractTemplateProvider {

    private String repositoryPackage;
    private String repositoryPostfix;

    public ManagerImplTemplateSupport(AnnotationAttributes attributes, String repositoryPackage, String repositoryPostfix) {
        super(attributes);
        this.repositoryPackage = repositoryPackage;
        this.repositoryPostfix = repositoryPostfix;
        this.findFilterRepositories();
    }

    public ManagerImplTemplateSupport(CustomResourceLoader customResourceLoader) {
        super(customResourceLoader);
        this.repositoryPackage = customResourceLoader.getRepositoryPackage();
        this.repositoryPostfix = customResourceLoader.getRepositoryPostfix();
        this.findFilterRepositories();
    }

    private void findFilterRepositories() {
        String repositoryPath = GeneratorUtils.getAbsolutePath() + repositoryPackage.replace(".", "/");
        File[] repositoryFiles = GeneratorUtils.getFileList(repositoryPath, repositoryPostfix);
        this.setIncludeFilter(Arrays.asList(repositoryFiles));
        this.setIncludeFilterPostfix(repositoryPostfix);
    }
    
    

    @Override
    protected Tuple<String, Integer> getContentFromTemplate(String mPackage, String simpleClassName,String repositorySuperClassName ,String postfix, BeanDefinition beanDefinition) {
        return new ManagerImplStructure(mPackage, simpleClassName,beanDefinition.getBeanClassName(), postfix, repositoryPackage, repositoryPostfix ,loader).build();
    }

    @Override
    protected String getExcludeClasses() {
        return "excludeManagerClasses";
    }

    @Override
    protected String getPostfix() {
        return "managerPostfix";
    }

	@Override
	protected String getRepositorySuperClass() {
		
		return "repositorySuperClass";
	}

}