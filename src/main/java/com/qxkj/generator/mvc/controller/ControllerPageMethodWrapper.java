package com.qxkj.generator.mvc.controller;

import com.google.common.base.CaseFormat;
import com.qxkj.generator.mvc.Page;
import com.qxkj.generator.mvc.ResponseBean;
import com.qxkj.generator.mvc.ResponseUtil;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright  2020年 generator. All rights reserved.
 * <p>
 * 为了共建和谐社会，请为该类写点注释吧...
 * <p>
 * version 1.0.0
 *
 * @author decade
 * @date 2020/11/9 5:21 下午
 */
public class ControllerPageMethodWrapper implements MethodWrapper {

  private TypeElement annotatedElement;

  private String qualifiedName;

  private String simpleTypeName;

  private String packageName;

  private Class controllerClass;

  private MvcController.Method method;

  private TypeName returnType;

  /**
   * 抽象接口service中对应的方法名称
   */
  private String serviceName;

  /**
   * 领域，比如Order,Contract
   */
  private String domain;

  /**
   * domain的全限定名
   */
  private String qualifiedDomain;

  /**
   * Service抽象接口类的simpleName
   * 比如：OrderService
   */
  private String serviceClassSimpleName;

  /**
   * serviceClassSimpleName的字段名
   * 比如：orderService
   */
  private String serviceFieldName;


  ControllerPageMethodWrapper(TypeElement annotatedElement) {
    this.annotatedElement = annotatedElement;
    this.qualifiedName = annotatedElement.getQualifiedName().toString();
    int index = qualifiedName.lastIndexOf(".");
    this.packageName = qualifiedName.substring(0, index);
    this.simpleTypeName = qualifiedName.substring(index + 1);

    MvcController annotation = annotatedElement.getAnnotation(MvcController.class);
    this.method = annotation.method();
    this.serviceName = annotation.method().serviceName;
    this.serviceFieldName = "service";
    try {
      Class<?> returnClass = annotation.returnClass();
      returnType = TypeName.get(returnClass);
    } catch (MirroredTypeException e) {
      DeclaredType classTypeMirror = (DeclaredType) e.getTypeMirror();
      TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
      returnType = TypeName.get(classTypeElement.asType());
    }

    try {
      this.qualifiedDomain = annotation.domain().getName();
    } catch (MirroredTypeException e) {
      DeclaredType classTypeMirror = (DeclaredType) e.getTypeMirror();
      this.qualifiedDomain = classTypeMirror.toString();
      this.domain = qualifiedDomain.substring(qualifiedDomain.lastIndexOf(".") + 1);
      this.serviceClassSimpleName = domain.concat("Service");
    }
  }

  private String getDomain() {
    return domain;
  }

  private TypeElement getAnnotatedElement() {
    return annotatedElement;
  }

  private String getQualifiedName() {
    return qualifiedName;
  }

  private String getSimpleTypeName() {
    return simpleTypeName;
  }

  private String getPackageName() {
    return packageName;
  }

  private Class getControllerClass() {
    return controllerClass;
  }

  private MvcController.Method getMethod() {
    return method;
  }

  private TypeName getReturnType() {
    return returnType;
  }

  private String getServiceName() {
    return serviceName;
  }

  private String getQualifiedDomain() {
    return qualifiedDomain;
  }

  private String getServiceClassSimpleName() {
    return serviceClassSimpleName;
  }

  private String getServiceFieldName() {
    return serviceFieldName;
  }

  private String getMethodStatement() {
    String param = String.format("(%s)", getParamName());
    String invoke = serviceFieldName.concat(".").concat(serviceName).concat(param);
    return "$T data = ".concat(invoke);
  }

  private ParameterSpec getParam() {
    return ParameterSpec.builder(TypeName.get(annotatedElement.asType()), getParamName()).build();
  }

  private String getParamName() {
    return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, annotatedElement.getSimpleName().toString());
  }

  private AnnotationSpec getRequestMapping() {
    return AnnotationSpec.builder(method.requestMapping)
            .addMember("value", "$S", method.url)
            .build();
  }

  @Override
  public List<MethodSpec> getControllerMethodSpecs() {
    MethodSpec must = MethodSpec.methodBuilder(getServiceName())
            .addAnnotation(getRequestMapping())
            .addModifiers(Modifier.PUBLIC)
            .addParameter(getParam())
            .addStatement(getMethodStatement(), ParameterizedTypeName.get(ClassName.get(Page.class), returnType))
            .addStatement("return $T.responseSuccess(data)", ResponseUtil.class)
            .returns(ResponseBean.class)
            .build();
    ArrayList<MethodSpec> ret = new ArrayList<>();
    ret.add(must);
    return ret;
  }

  @Override
  public List<MethodSpec> getInterfaceMethodSpecs() {
    MethodSpec must = MethodSpec.methodBuilder(getServiceName())
            .addParameter(getParam())
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .returns(ParameterizedTypeName.get(ClassName.get(Page.class), returnType))
            .build();
    ArrayList<MethodSpec> ret = new ArrayList<>();
    ret.add(must);
    return ret;
  }
}
