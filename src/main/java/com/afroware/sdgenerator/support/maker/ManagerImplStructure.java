package com.afroware.sdgenerator.support.maker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.afroware.sdgenerator.support.maker.builder.ObjectBuilder;
import com.afroware.sdgenerator.support.maker.builder.ObjectStructure;
import com.afroware.sdgenerator.support.maker.builder.ObjectStructure.ObjectConstructor;
import com.afroware.sdgenerator.support.maker.builder.ObjectStructure.ObjectFunction;
import com.afroware.sdgenerator.support.maker.builder.ObjectStructure.ObjectMethod;
import com.afroware.sdgenerator.support.maker.values.ExpressionValues;
import com.afroware.sdgenerator.support.maker.values.ObjectTypeValues;
import com.afroware.sdgenerator.support.maker.values.ObjectValues;
import com.afroware.sdgenerator.support.maker.values.ScopeValues;
import com.afroware.sdgenerator.util.CustomResourceLoader;
import com.afroware.sdgenerator.util.GeneratorUtils;
import com.afroware.sdgenerator.util.Tuple;

/**
 * Created by lamallam on 28/09/17.
 */
public class ManagerImplStructure extends Structure {

	public ManagerImplStructure(String managerPackage, String entityName, String className, String postfix,
			String repositoryPackage, String repositoryPostfix ,CustomResourceLoader loader) {
		this.loader = loader;
		String managerName = entityName + postfix;
		String managerImplName = managerName + "Impl";
		String managerImplPackage = managerPackage + ".impl";
		String repositoryName = entityName + repositoryPostfix;
		String repositoryNameAttribute = GeneratorUtils.decapitalize(repositoryName);
		Tuple<String, Boolean> entityId = getEntityId(className);
		
		this.objectBuilder = new ObjectBuilder(
				new ObjectStructure(managerImplPackage, ScopeValues.PUBLIC, ObjectTypeValues.CLASS, managerImplName)
						.addImplement(managerName).addImport(repositoryPackage + "." + repositoryName)
						.addImport(managerPackage + "." + managerName).addImport(Autowired.class)
						.addImport(Component.class).addAnnotation(Component.class)
						.addAttribute(repositoryName, repositoryNameAttribute)
						.addConstructor(constructor(managerImplName, repositoryName, repositoryNameAttribute))
						.addImport(className).addFunction(count(repositoryNameAttribute))
						.addMethod(deleteEntity(repositoryNameAttribute, entityName))
						.addMethod(deleteInteger(repositoryNameAttribute,entityId.left()))
						.addMethod(deleteIterable(repositoryNameAttribute, entityName))
						.addMethod(deleteAll(repositoryNameAttribute)).addFunction(exists(repositoryNameAttribute,entityId.left()))
						.addFunction(findAll(repositoryNameAttribute, entityName))
						.addFunction(findAllIterable(repositoryNameAttribute, entityName ,entityId.left()))
						.addFunction(findOne(repositoryNameAttribute, entityName,entityId.left()))
						.addFunction(saveIterable(repositoryNameAttribute, entityName))
						.addFunction(save(repositoryNameAttribute, entityName))).setAttributeBottom(true);

	}

	/**
	 * @param managerImplName
	 * @param repositoryName
	 * @param repositoryNameAttribute
	 * @return
	 */
	private ObjectConstructor constructor(String managerImplName, String repositoryName,
			String repositoryNameAttribute) {
		return new ObjectStructure.ObjectConstructor(ScopeValues.PUBLIC, managerImplName)
				.addAnnotation(Autowired.class).addArgument(repositoryName, repositoryNameAttribute)
				.addBodyLine(ObjectValues.THIS.getValue() + repositoryNameAttribute
						+ ExpressionValues.EQUAL.getValue() + repositoryNameAttribute);
	}

	/**
	 * @param entityName
	 * @return
	 */
	private ObjectFunction save(String repositoryNameAttribute, String entityName) {
		return new ObjectStructure.ObjectFunction(ScopeValues.PUBLIC, "save", "<S extends " + entityName + "> S")
				.addArgument("S", "arg").addBodyLine("return " + repositoryNameAttribute + ".save(arg)");
	}

	/**
	 * @param entityName
	 * @return
	 */
	private ObjectFunction saveIterable(String repositoryNameAttribute, String entityName) {
		return new ObjectStructure.ObjectFunction(ScopeValues.PUBLIC, "save",
				"<S extends " + entityName + "> Iterable<S>").addArgument("Iterable<S>", "arg")
						.addBodyLine("return " + repositoryNameAttribute + ".save(arg)");
	}

	/**
	 * @param entityName
	 * @return
	 */
	private ObjectFunction findOne(String repositoryNameAttribute, String entityName , String primaryKey) {
		return new ObjectStructure.ObjectFunction(ScopeValues.PUBLIC, "findOne", entityName)
				.addArgument(primaryKey, "arg").addBodyLine("return " + repositoryNameAttribute + ".findOne(arg)");
	}

	/**
	 * @param entityName
	 * @return
	 */
	private ObjectFunction findAllIterable(String repositoryNameAttribute, String entityName , String primaryKey) {
		return new ObjectStructure.ObjectFunction(ScopeValues.PUBLIC, "findAll", " Iterable<" + entityName + ">")
				.addArgument("Iterable<"+primaryKey+">", "arg").addBodyLine("return " + repositoryNameAttribute + ".findAll(arg)");
	}

	/**
	 * @param entityName
	 * @return
	 */
	private ObjectFunction findAll(String repositoryNameAttribute, String entityName) {
		return new ObjectStructure.ObjectFunction(ScopeValues.PUBLIC, "findAll", "Iterable<" + entityName + ">")
				.addBodyLine("return " + repositoryNameAttribute + ".findAll()");
	}

	/**
	 * @return
	 */
	private ObjectFunction exists(String repositoryNameAttribute , String primaryKey) {
		return new ObjectStructure.ObjectFunction(ScopeValues.PUBLIC, "exists", "Boolean").addArgument(primaryKey, "arg")
				.addBodyLine("return " + repositoryNameAttribute + ".exists(arg)");
	}

	/**
	 * @return
	 */
	private ObjectMethod deleteAll(String repositoryNameAttribute) {
		return new ObjectStructure.ObjectMethod(ScopeValues.PUBLIC, "deleteAll")
				.addBodyLine( repositoryNameAttribute + ".deleteAll()");
	}

	/**
	 * @param entityName
	 * @return
	 */
	private ObjectMethod deleteIterable(String repositoryNameAttribute, String entityName) {
		return new ObjectStructure.ObjectMethod(ScopeValues.PUBLIC, "delete")
				.addArgument("Iterable<? extends " + entityName + ">", "arg")
				.addBodyLine( repositoryNameAttribute + ".delete(arg)");
	}

	/**
	 * @return
	 */
	private ObjectFunction count(String repositoryNameAttribute) {
		return new ObjectStructure.ObjectFunction(ScopeValues.PUBLIC, "count", "Long")
				.addBodyLine("return " + repositoryNameAttribute + ".count()");
	}

	/**
	 * @return
	 */
	private ObjectMethod deleteInteger(String repositoryNameAttribute , String primaryKey) {
		return new ObjectStructure.ObjectMethod(ScopeValues.PUBLIC, "delete").addArgument(primaryKey, "arg")
				.addBodyLine( repositoryNameAttribute + ".delete(arg)");
	}

	/**
	 * @param entityName
	 * @return
	 */
	private ObjectMethod deleteEntity(String repositoryNameAttribute, String entityName) {
		return new ObjectStructure.ObjectMethod(ScopeValues.PUBLIC, "delete").addArgument(entityName, "arg")
				.addBodyLine( repositoryNameAttribute + ".delete(arg)");
	}


}
