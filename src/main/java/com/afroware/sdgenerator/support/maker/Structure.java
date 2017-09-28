package com.afroware.sdgenerator.support.maker;

import com.afroware.sdgenerator.support.maker.builder.ObjectBuilder;
import com.afroware.sdgenerator.support.maker.builder.ObjectStructure;
import com.afroware.sdgenerator.support.maker.values.ObjectTypeValues;
import com.afroware.sdgenerator.support.maker.values.ScopeValues;
import com.afroware.sdgenerator.util.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by lamallam on 28/09/17.
 */
public class Structure {

	protected CustomResourceLoader loader;
	protected ObjectBuilder objectBuilder;
	protected Integer error = 0;

	protected Tuple<String, Boolean> getEntityId(String entityClass) {
		try {
			Class<?> entity = null;
			if (loader == null) {
				entity = Class.forName(entityClass);
			} else {
				entity = loader.getUrlClassLoader().loadClass(entityClass);
			}
			
			for (Field field : entity.getDeclaredFields()) {

				if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(EmbeddedId.class)) {
					this.implementsSerializable(field.getType());
					return new Tuple<>(field.getType().getName(), this.isCustomType(field.getType()));
				}
			}

			for (Method method : entity.getDeclaredMethods()) {
				if (!method.getReturnType().equals(Void.TYPE)
						&& (method.isAnnotationPresent(Id.class) || method.isAnnotationPresent(EmbeddedId.class))) {
					this.implementsSerializable(method.getReturnType());
					return new Tuple<>(method.getReturnType().getName(), this.isCustomType(method.getReturnType()));
				}
			}

			error = SDLogger.addError("Repository Error: Primary key not found in "
					+ GeneratorUtils.getSimpleClassName(entityClass) + ".java");
			return null;
		} catch (GeneratorException ex) {
			error = SDLogger.addError(ex.getMessage());
			return null;
		} catch (Exception e) {
			error = SDLogger.addError("Repository Error: Failed to access entity "
					+ GeneratorUtils.getSimpleClassName(entityClass) + ".java");
			return null;
		}
	}

	public Tuple<String, Integer> build() {
		return new Tuple<>(objectBuilder == null ? null : objectBuilder.build(), error);
	}

	protected boolean isCustomType(Class<?> clazz) {
		if (clazz.isAssignableFrom(Boolean.class) || clazz.isAssignableFrom(Byte.class)
				|| clazz.isAssignableFrom(String.class) || clazz.isAssignableFrom(Integer.class)
				|| clazz.isAssignableFrom(Long.class) || clazz.isAssignableFrom(Float.class)
				|| clazz.isAssignableFrom(Double.class)) {
			return false;
		}
		return true;
	}

	protected void implementsSerializable(Class<?> clazz) throws GeneratorException {
		if (!Serializable.class.isAssignableFrom(clazz)) {
			throw new GeneratorException(
					"Type parameter '" + clazz.getName() + "' should implement 'java.io.Serializable'");
		}
	}
}
