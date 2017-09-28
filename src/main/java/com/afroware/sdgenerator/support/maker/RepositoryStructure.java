package com.afroware.sdgenerator.support.maker;

import com.afroware.sdgenerator.support.maker.builder.ObjectBuilder;
import com.afroware.sdgenerator.support.maker.builder.ObjectStructure;
import com.afroware.sdgenerator.support.maker.values.ObjectTypeValues;
import com.afroware.sdgenerator.support.maker.values.ScopeValues;
import com.afroware.sdgenerator.util.CustomResourceLoader;
import com.afroware.sdgenerator.util.GeneratorUtils;
import com.afroware.sdgenerator.util.Tuple;

/**
 * Created by lamallam on 28/09/17.
 */
public class RepositoryStructure extends Structure {

	public RepositoryStructure(String repositoryPackage, String entityName, String entityClass,
			String repositorySuperClassName, String postfix, CustomResourceLoader loader) {
		this.loader = loader;
		String repositoryName = entityName + postfix;
		String repositorySuperClassPackageName = "CrudRepository".equals(repositorySuperClassName)
				? "org.springframework.data.repository"
				: "org.springframework.data.jpa.repository";
		String repositorySuperClass = repositorySuperClassPackageName + "." + repositorySuperClassName;
		Tuple<String, Boolean> entityId = getEntityId(entityClass);
		if (entityId != null) {
			this.objectBuilder = new ObjectBuilder(new ObjectStructure(repositoryPackage, ScopeValues.PUBLIC,
					ObjectTypeValues.INTERFACE, repositoryName)
							.addImport(entityClass)
							.addImport(repositorySuperClass)
							.addImport("org.springframework.stereotype.Repository")
							.addImport(entityId.right() ? entityId.left() : "").addAnnotation("Repository")
							.setExtend(repositorySuperClassName, entityName,
									GeneratorUtils.getSimpleClassName(entityId.left())));
		}
	}

}
