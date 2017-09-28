package com.afroware.sdgenerator.support;

import com.afroware.sdgenerator.provider.AbstractTemplateProvider;
import com.afroware.sdgenerator.support.maker.ManagerStructure;
import com.afroware.sdgenerator.util.CustomResourceLoader;
import com.afroware.sdgenerator.util.GeneratorUtils;
import com.afroware.sdgenerator.util.Tuple;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.annotation.AnnotationAttributes;

import java.io.File;
import java.util.Arrays;

/**
 * Created by lamallam on 28/09/17.
 */
public class ManagerTemplateSupport extends AbstractTemplateProvider {

    private String repositoryPackage;
    private String repositoryPostfix;

    public ManagerTemplateSupport(AnnotationAttributes attributes, String repositoryPackage, String repositoryPostfix) {
        super(attributes);
        this.repositoryPackage = repositoryPackage;
        this.repositoryPostfix = repositoryPostfix;
        this.findFilterRepositories();
    }

    public ManagerTemplateSupport(CustomResourceLoader customResourceLoader) {
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
        return new ManagerStructure(mPackage, simpleClassName,beanDefinition.getBeanClassName(), postfix, repositoryPackage, repositoryPostfix,loader).build();
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