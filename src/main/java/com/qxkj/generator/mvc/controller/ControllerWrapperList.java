package com.qxkj.generator.mvc.controller;


import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

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
public class ControllerWrapperList extends ArrayList<ControllerWrapper> {

  private Class controllerClass;


  void generateCode(Elements elementUtils, Filer filer) throws IOException {


    TypeSpec.Builder classBuilder = TypeSpec.classBuilder("TestControllerName")
            .addModifiers(Modifier.PUBLIC);


    for (ControllerWrapper wrapper : this) {
      MethodSpec build = MethodSpec.methodBuilder(wrapper.getMethodName())
              .addModifiers(Modifier.PUBLIC)
              .addParameters(wrapper.getParams())
              .addStatement(wrapper.getMethodStatement())
              .returns(ArrayList.class)
              .build();
      classBuilder.addMethod(build);
    }
    TypeSpec classFile = classBuilder.build();
    JavaFile.builder(controllerClass.getPackage().getName(), classFile).build().writeTo(filer);
  }

  public ControllerWrapperList(Class controllerClass) {
    this.controllerClass = controllerClass;
  }

}
