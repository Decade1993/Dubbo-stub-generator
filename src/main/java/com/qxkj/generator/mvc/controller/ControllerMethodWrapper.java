package com.qxkj.generator.mvc.controller;

import com.google.common.base.CaseFormat;
import com.qxkj.generator.mvc.ResponseBean;
import com.qxkj.generator.mvc.ResponseUtil;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;


public class ControllerMethodWrapper {

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


  ControllerMethodWrapper(TypeElement annotatedElement) {
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
//      this.qualifiedDomain = classTypeMirror.toString();
      this.qualifiedDomain = "com.qxkj.sale.rpc.vo.BoundGoods";
      this.domain = qualifiedDomain.substring(qualifiedDomain.lastIndexOf(".") + 1);
      this.serviceClassSimpleName = domain.concat("Service");
    }




  }

  public String getDomain() {
    return domain;
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

  public TypeName getReturnType() {
    return returnType;
  }

  public String getServiceName() {
    return serviceName;
  }

  public String getQualifiedDomain() {
    return qualifiedDomain;
  }

  public String getServiceClassSimpleName() {
    return serviceClassSimpleName;
  }

  public String getServiceFieldName() {
    return serviceFieldName;
  }

  public String getMethodStatement() {
    String param = String.format("(%s)", getParamName());
    String invoke = serviceFieldName.concat(".").concat(serviceName).concat(param);
    return "$T data = ".concat(invoke);
  }

  public ParameterSpec getParam() {
    ParameterSpec.Builder builder;
    builder = ParameterSpec.builder(TypeName.get(annotatedElement.asType()), getParamName());
    return builder.build();
  }

  private String getParamName() {
    if (method.equals(MvcController.Method.CREATE) || method.equals(MvcController.Method.DELETE)) {
      return "id";
    }
    return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, annotatedElement.getSimpleName().toString());
  }

  public AnnotationSpec getRequestMapping() {
    return AnnotationSpec.builder(method.requestMapping)
            .addMember("value", "$S", method.url)
            .build();
  }

  public MethodSpec getControllerMethodSpec() {
    MethodSpec must = MethodSpec.methodBuilder(getServiceName())
            .addAnnotation(getRequestMapping())
            .addModifiers(Modifier.PUBLIC)
            .addParameter(getParam())
            .addStatement(getMethodStatement(), getReturnType())
            .addStatement("return $T.responseSuccess(data)", ResponseUtil.class)
            .returns(ResponseBean.class)
            .build();
    return must;
  }

  public MethodSpec getInterfaceMethodSpec() {
    return MethodSpec.methodBuilder(getServiceName())
            .addParameter(getParam())
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .returns(getReturnType())
            .build();
  }


}
