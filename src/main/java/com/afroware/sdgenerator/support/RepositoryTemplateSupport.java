package com.afroware.sdgenerator.support;

import com.afroware.sdgenerator.provider.AbstractTemplateProvider;
import com.afroware.sdgenerator.support.maker.RepositoryStructure;
import com.afroware.sdgenerator.util.CustomResourceLoader;
import com.afroware.sdgenerator.util.Tuple;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.annotation.AnnotationAttributes;

/**
 * Created by lamallam on 28/09/17.
 */
public class RepositoryTemplateSupport extends AbstractTemplateProvider {

    public RepositoryTemplateSupport(AnnotationAttributes attributes) {
        super(attributes);
    }

    public RepositoryTemplateSupport(CustomResourceLoader loader) {
        super(loader);
    }

    @Override
    protected Tuple<String, Integer> getContentFromTemplate(String repositoryPackage, String simpleClassName,String repositorySuperClassName, String postfix, BeanDefinition beanDefinition) {
        return new RepositoryStructure(repositoryPackage, simpleClassName, beanDefinition.getBeanClassName(),repositorySuperClassName,  postfix, loader).build();
    }

    @Override
    protected String getExcludeClasses() {
        return "excludeRepositoriesClasses";
    }

    @Override
    protected String getPostfix() {
        return "repositoryPostfix";
    }

	@Override
	protected String getRepositorySuperClass() {
		
		return "repositorySuperClass";
	}

}
