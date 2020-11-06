package com.qxkj.generator.mvc.controller;

import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.List;


public class ControllerWrapper {

  private TypeElement annotatedElement;

  private String qualifiedName;

  private String simpleTypeName;

  private String packageName;

  private Class controllerClass;

  private MvcController.Method method;

  private Class<?> returnClass;

  private String methodName;


  ControllerWrapper(TypeElement annotatedElement) {
    this.annotatedElement = annotatedElement;
    this.qualifiedName = annotatedElement.getQualifiedName().toString();
    int index = qualifiedName.lastIndexOf(".");
    this.packageName = qualifiedName.substring(0, index);
    this.simpleTypeName = qualifiedName.substring(index + 1);

    MvcController annotation = annotatedElement.getAnnotation(MvcController.class);
    this.controllerClass = annotation.controllerClass();
    this.method = annotation.method();
    this.returnClass = annotation.returnClass();
    this.methodName = annotation.method().method.name();

  }

  public TypeElement getAnnotatedElement() {
    return annotatedElement;
  }

  public String getQualifiedName() {
    return qualifiedName;
  }

  public String getSimpleTypeName() {
    return simpleTypeName;
  }

  public String getPackageName() {
    return packageName;
  }

  public Class getControllerClass() {
    return controllerClass;
  }

  public MvcController.Method getMethod() {
    return method;
  }

  public Class<?> getReturnClass() {
    return returnClass;
  }

  public String getMethodName() {
    return methodName;
  }

  public String getMethodStatement() {
    return "documentService.list();";
  }

  public List<ParameterSpec> getParams() {
    List<ParameterSpec> list = new ArrayList<>();
    ParameterSpec.Builder builder = ParameterSpec.builder(TypeName.get(annotatedElement.asType()), annotatedElement.getSimpleName().toString());
    list.add(builder.build());
    return list;
  }
}
