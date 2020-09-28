package com.qxkj.generator.dubbo;

import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
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
@AutoService(Processor.class)
public class StubProcessor extends AbstractProcessor {

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
    annotationSet.add(DubboStub.class.getCanonicalName());
    return annotationSet;
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

    try {
      List<MapperWrapper> wrappers = new ArrayList<>();

      Set<? extends Element> mappers = roundEnvironment.getElementsAnnotatedWith(DubboStub.class);
      if (mappers != null && mappers.size() > 0) {
        for (Element mapper : mappers) {
          if (mapper.getKind() != ElementKind.INTERFACE) {
            error(mapper, "Only INTERFACE can be annotated with @%s",
                    DubboStub.class.getCanonicalName());
            return true;
          }
          wrappers.add(new MapperWrapper((TypeElement) mapper));
        }
      }

      for (MapperWrapper wrapper : wrappers) {
        wrapper.generateCode(elementUtils, filer);
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
