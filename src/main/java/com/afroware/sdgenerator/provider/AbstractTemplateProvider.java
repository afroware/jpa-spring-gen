package com.afroware.sdgenerator.provider;

import com.afroware.sdgenerator.util.CustomResourceLoader;
import com.afroware.sdgenerator.util.GeneratorUtils;
import com.afroware.sdgenerator.util.SDLogger;
import com.afroware.sdgenerator.util.Tuple;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.Assert;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by lamallam on 28/09/17.
 */
public abstract class AbstractTemplateProvider {

    private Class<?>[] excludeClasses;
    private String postfix;
    private boolean debug;
    private Collection<File> includeFilter;
    private String includeFilterPostfix = "";
    private boolean overwrite;
	private String repositorySuperClassName;
	protected CustomResourceLoader loader;

    public AbstractTemplateProvider(AnnotationAttributes attributes) {
        Assert.notNull(attributes, "AnnotationAttributes must not be null!");
        this.excludeClasses = attributes.getClassArray(getExcludeClasses());
        this.postfix = attributes.getString(getPostfix());
        this.repositorySuperClassName = attributes.getString(getRepositorySuperClass());
        this.debug = attributes.getBoolean("debug");
        this.overwrite = attributes.getBoolean("overwrite");
        if (excludeClasses.length > 0 && debug) {
            SDLogger.debug(String.format("Exclude %s %s in the %s generator", excludeClasses.length, excludeClasses.length == 1 ? "entity":"entities", postfix));
        }
    }

    public AbstractTemplateProvider(CustomResourceLoader customResourceLoader) {
        Assert.notNull(customResourceLoader, "CustomResourceLoader must not be null!");
        this.postfix = customResourceLoader.getPostfix();
        this.repositorySuperClassName = customResourceLoader.getRepositorySuperClassName();
        this.debug = true;
        this.excludeClasses = new Class[]{};
        this.loader = customResourceLoader;
        this.overwrite = customResourceLoader.isOverwrite();
    }

    public void initializeCreation(String path, String ePackage, Collection<BeanDefinition> candidates ,boolean impl) {
        int generatedCount = 0;
        
        if(!GeneratorUtils.verifyPackage(impl?path+"/impl":path)){
            return;
        }

        for (BeanDefinition beanDefinition : candidates) {
            if (verifyEntityNonExclude(beanDefinition.getBeanClassName())){
                continue;
            }

            if (createHelper(path, beanDefinition, repositorySuperClassName, postfix, ePackage ,impl)) {
                generatedCount++;
            }
        }

        SDLogger.plusGenerated(generatedCount);
    }

    protected void setIncludeFilter(Collection<File> includeFilter){
        this.includeFilter = includeFilter;
    }

    protected void setIncludeFilterPostfix(String includeFilterPostfix) {
        this.includeFilterPostfix = includeFilterPostfix;
    }

    private Tuple<Boolean, Integer> verifyIncludeFilter(String beanDefinitionName) {

        int warnPosition = 0;
        if (includeFilter == null) {
            return new Tuple<>(Boolean.TRUE, warnPosition);
        }

        boolean result = includeFilter.stream().anyMatch(i ->
            i.getName().replace(".java", "")
                    .equals(beanDefinitionName + includeFilterPostfix)
        );

        if (!result) {
            warnPosition = SDLogger.addWarn(String.format("%s ignored: Repository not found for %s entity class", postfix, beanDefinitionName));
        }
        return new Tuple<>(result, warnPosition);
    }

    private boolean verifyEntityNonExclude(String beanClassName){
        return Arrays.stream(excludeClasses).anyMatch(b -> b.getName().equals(beanClassName));
    }

    private boolean createHelper(String path, BeanDefinition beanDefinition,String repositorySuperClassName, String postfix, String repositoryPackage, boolean impl){
        String simpleClassName = GeneratorUtils.getSimpleClassName(beanDefinition.getBeanClassName());
        Tuple<Boolean, Integer> result = null;
        if(simpleClassName != null){
        	
            String fileHelper = simpleClassName + postfix + ".java";
            String filePath = path + "/" + fileHelper;
            if(impl) {
            	fileHelper = simpleClassName + postfix + "Impl.java";
            	filePath = path + "/impl/" + fileHelper;
            }
            Tuple<Boolean, Integer> verifyInclude = verifyIncludeFilter(simpleClassName);
            if (!verifyInclude.left()) {
                SDLogger.addRowGeneratedTable(postfix, fileHelper, "Warn #" + verifyInclude.right());
                return false;
            }

            File file = new File(filePath);

            String fileCondition = "Created";
            if (overwrite && file.exists()) {
                file.delete();
                fileCondition = "Overwritten";
            }

            if (!file.exists()){
                result = createFileFromTemplate(filePath, repositoryPackage, simpleClassName, repositorySuperClassName, postfix, beanDefinition);
                if (debug){
                    SDLogger.addRowGeneratedTable(postfix, fileHelper, result.left() ? fileCondition : "Error #" + result.right());
                }
            }
        } else {
            SDLogger.addError(String.format("Could not get SimpleName from: %s", beanDefinition.getBeanClassName()));
        }

        return result == null ? false : result.left();
    }

    protected abstract Tuple<String, Integer> getContentFromTemplate(String mPackage, String simpleClassName, String repositorySuperClassName,String postfix, BeanDefinition beanDefinition);

    private Tuple<Boolean, Integer> createFileFromTemplate(String path, String repositoryPAckage, String simpleClassName,String repositorySuperClassName, String postfix, BeanDefinition beanDefinition){
        Tuple<String, Integer> content = getContentFromTemplate(repositoryPAckage, simpleClassName, repositorySuperClassName, postfix, beanDefinition);
        if (content.left() == null) {
            return new Tuple<>(false, content.right());
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write(content.left());
            return new Tuple<>(true, 0);
        } catch (IOException e) {
            return new Tuple<>(false, SDLogger.addError("Error occurred while persisting file: " + e.getMessage()));
        }
    }

    protected abstract String getExcludeClasses();
    protected abstract String getPostfix();
    protected abstract String getRepositorySuperClass();

}
