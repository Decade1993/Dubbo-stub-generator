package com.qxkj.generator.mvc.controller;

import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Copyright  2020年 dubbo-stub-generator. All rights reserved.
 * <p>
 *  Dubbo Stub 本地存根类
 *  编译时生成
 * <p>
 * version 1.0.0
 *
 * @author decade
 * @date 2020/9/28 11:06 上午
 */
@AutoService(javax.annotation.processing.Processor.class)
public class ControllerProcessor extends AbstractProcessor {

  private Types typeUtils;
  private Elements elementUtils;
  private Filer filer;
  private Messager messager;


  @Override
  public synchronized void init(ProcessingEnvironment processingEnvironment) {
    super.init(processingEnvironment);
    typeUtils = processingEnvironment.getTypeUtils();
    elementUtils = processingEnvironment.getElementUtils();
    filer = processingEnvironment.getFiler();
    messager = processingEnvironment.getMessager();
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    Set<String> annotationSet = new LinkedHashSet<>();
    annotationSet.add(MvcController.class.getCanonicalName());
    return annotationSet;
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

    try {

      Map<String, ControllerWrapper> wrappers = new HashMap<>();

      Set<? extends Element> mappers = roundEnvironment.getElementsAnnotatedWith(MvcController.class);
      if (mappers != null && mappers.size() > 0) {
        for (Element mapper : mappers) {
          if (mapper.getKind() != ElementKind.CLASS) {
            error(mapper, "Only CLASS can be annotated with @%s",
                    MvcController.class.getCanonicalName());
            return true;
          }
          MvcController annotation = mapper.getAnnotation(MvcController.class);
          String domain = annotation.domain();
          ControllerWrapper controllerWrapper = wrappers.get(domain);
          if (controllerWrapper == null) {
            controllerWrapper = new ControllerWrapper(domain);
            wrappers.put(domain, controllerWrapper);
          }
          controllerWrapper.add(new ControllerMethodWrapper((TypeElement) mapper));
        }
      }
      for (ControllerWrapper wrapperList : wrappers.values()) {
        wrapperList.generateCode(elementUtils, filer);
      }
    } catch (Exception e) {
      error(null, e.getMessage());
    }


    return true;
  }

  private void error(Element e, String msg, Object... args) {
    messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
  }

  private void warn(Element e, String msg, Object... args) {
    messager.printMessage(Diagnostic.Kind.WARNING, String.format(msg, args), e);
  }
}
