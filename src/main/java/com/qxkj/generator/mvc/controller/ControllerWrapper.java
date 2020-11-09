package com.qxkj.generator.mvc.controller;


import com.google.common.base.CaseFormat;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Copyright  2020年 generator. All rights reserved.
 * <p>
 * 为了共建和谐社会，请为该类写点注释吧...
 * <p>
 * version 1.0.0
 *
 * @author decade
 * @date 2020/11/6 4:15 下午
 */
public class ControllerWrapper extends ArrayList<MethodWrapper> {

  private String domain;

  private String packName;

  private String url;

  private String domainSimpleName;

  /**
   * the name of the controller class
   */
  private String className;

  /**
   * the name of the interface class
   */
  private String interfaceName;

  /**
   * serviceClassSimpleName的字段名
   * 比如：orderService
   */
  private String serviceFieldName;


  void generateCode(Elements elementUtils, Filer filer) throws IOException {

    // 创建Service 接口类
    TypeSpec.Builder interfaceBuilder = TypeSpec.interfaceBuilder(this.interfaceName)
            .addModifiers(Modifier.PUBLIC);
    for (MethodWrapper wrapper : this) {
      interfaceBuilder.addMethods(wrapper.getInterfaceMethodSpecs());
    }
    TypeSpec interfaceFile = interfaceBuilder.build();
    JavaFile.builder(packName, interfaceFile).build().writeTo(filer);


    // 创建Controller class 类
    TypeSpec.Builder classBuilder = TypeSpec.classBuilder(this.className)
            .addAnnotation(RestController.class)
            .addAnnotation(AnnotationSpec.builder(RequestMapping.class).addMember("value", "$S", url).build())
            .addModifiers(Modifier.PUBLIC)
            .addField(FieldSpec.builder(ClassName.get(packName, interfaceName),
                    serviceFieldName, Modifier.PRIVATE)
                    .addAnnotation(Autowired.class).build());

    // 创建 requestMapping 方法
    for (MethodWrapper wrapper : this) {
      classBuilder.addMethods(wrapper.getControllerMethodSpecs());
    }
    TypeSpec classFile = classBuilder.build();
    JavaFile.builder(packName, classFile).build().writeTo(filer);
  }

  public ControllerWrapper(String domain) {
    this.domain = domain;
    int index = domain.lastIndexOf(".");
    this.packName = domain.substring(0, index);
    this.domainSimpleName = domain.substring(index + 1);
    this.url = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, domainSimpleName);
    this.className = domainSimpleName.concat("Controller_");
    this.interfaceName = domainSimpleName.concat("Service_");
    this.serviceFieldName = "service";
  }

}
