package com.afroware.sdgenerator.support.maker;

import com.afroware.sdgenerator.support.maker.builder.ObjectBuilder;
import com.afroware.sdgenerator.support.maker.builder.ObjectStructure;
import com.afroware.sdgenerator.support.maker.builder.ObjectStructure.ObjectFunction;
import com.afroware.sdgenerator.support.maker.builder.ObjectStructure.ObjectMethod;
import com.afroware.sdgenerator.support.maker.values.ObjectTypeValues;
import com.afroware.sdgenerator.support.maker.values.ScopeValues;
import com.afroware.sdgenerator.util.CustomResourceLoader;
import com.afroware.sdgenerator.util.Tuple;

/**
 * Created by lamallam on 28/09/17.
 */
public class ManagerStructure extends Structure {


    public ManagerStructure(String managerPackage, String entityName, String className ,String postfix, String repositoryPackage, String repositoryPostfix ,CustomResourceLoader loader) {
		this.loader = loader;

        String managerName = entityName + postfix;
        Tuple<String, Boolean> entityId = getEntityId(className);
        this.objectBuilder = new ObjectBuilder(new ObjectStructure(managerPackage, ScopeValues.PUBLIC, ObjectTypeValues.INTERFACE, managerName)
        	   .addImport(className)	
               .addFunction(count())
               .addMethod(deleteEntity(entityName))
               .addMethod(deleteInteger(entityId.left()))
               .addMethod(deleteIterable(entityName))
               .addMethod(deleteAll())
               .addFunction(exists(entityId.left()))
               .addFunction(findAll(entityName))
               .addFunction(findAllIterable(entityName,entityId.left()))
               .addFunction(findOne(entityName,entityId.left()))
               .addFunction(saveIterable(entityName))
               .addFunction(save(entityName))
        ).setAttributeBottom(true);

    }

	/**
	 * @param entityName
	 * @return
	 */
	private ObjectFunction save(String entityName) {
		return new ObjectStructure.ObjectFunction(ScopeValues.PUBLIC, "save" , "<S extends "+entityName+"> S" ,true).addArgument("S", "arg");
	}

	/**
	 * @param entityName
	 * @return
	 */
	private ObjectFunction saveIterable(String entityName) {
		return new ObjectStructure.ObjectFunction(ScopeValues.PUBLIC, "save" , "<S extends "+entityName+"> Iterable<S>" ,true).addArgument("Iterable<S>", "arg");
	}

	/**
	 * @param entityName
	 * @return
	 */
	private ObjectFunction findOne(String entityName,String primaryKey) {
		return new ObjectStructure.ObjectFunction(ScopeValues.PUBLIC, "findOne" , entityName ,true).addArgument(primaryKey, "arg");
	}

	/**
	 * @param entityName
	 * @return
	 */
	private ObjectFunction findAllIterable(String entityName,String primaryKey) {
		return new ObjectStructure.ObjectFunction(ScopeValues.PUBLIC, "findAll" , " Iterable<"+entityName+">" ,true).addArgument("Iterable<"+primaryKey+">", "arg");
	}

	/**
	 * @param entityName
	 * @return
	 */
	private ObjectFunction findAll(String entityName) {
		return new ObjectStructure.ObjectFunction(ScopeValues.PUBLIC, "findAll" , "Iterable<"+entityName+">" ,true);
	}

	/**
	 * @return
	 */
	private ObjectFunction exists(String primaryKey) {
		return new ObjectStructure.ObjectFunction(ScopeValues.PUBLIC, "exists" , "Boolean" ,true).addArgument(primaryKey, "arg");
	}

	/**
	 * @return
	 */
	private ObjectMethod deleteAll() {
		return new ObjectStructure.ObjectMethod(ScopeValues.PUBLIC, "deleteAll" ,true);
	}

	/**
	 * @param entityName
	 * @return
	 */
	private ObjectMethod deleteIterable(String entityName) {
		return new ObjectStructure.ObjectMethod(ScopeValues.PUBLIC, "delete" ,true).addArgument("Iterable<? extends "+entityName+">", entityName);
	}

	/**
	 * @return
	 */
	private ObjectFunction count() {
		return new ObjectStructure.ObjectFunction(ScopeValues.PUBLIC, "count" , "Long" ,true);
	}

	/**
	 * @return
	 */
	private ObjectMethod deleteInteger(String primaryKey) {
		return new ObjectStructure.ObjectMethod(ScopeValues.PUBLIC, "delete" ,true).addArgument(primaryKey, "arg");
	}

	/**
	 * @param entityName
	 * @return
	 */
	private ObjectMethod deleteEntity(String entityName) {
		return new ObjectStructure.ObjectMethod(ScopeValues.PUBLIC, "delete" ,true).addArgument(entityName, entityName);
	}

}
