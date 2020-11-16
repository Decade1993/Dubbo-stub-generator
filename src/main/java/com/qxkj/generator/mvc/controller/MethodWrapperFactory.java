package com.qxkj.generator.mvc.controller;

import javax.lang.model.element.TypeElement;

/**
 * Copyright  2020年 generator. All rights reserved.
 * <p>
 * 为了共建和谐社会，请为该类写点注释吧...
 * <p>
 * version 1.0.0
 *
 * @author decade
 * @date 2020/11/9 3:55 下午
 */
public class MethodWrapperFactory {

  public static MethodWrapper create(MvcController mvcController, TypeElement typeElement) {
    if (mvcController.method().equals(MvcController.Method.FIND_BY_ID)) {
      return new ControllerFindByIdMethodWrapper(typeElement);
    } else if (mvcController.method().equals(MvcController.Method.PAGE)) {
      return new ControllerPageMethodWrapper(typeElement);
    } else if (mvcController.method().equals(MvcController.Method.UPDATE)) {
      return new ControllerUpdateMethodWrapper(typeElement);
    } else if (mvcController.method().equals(MvcController.Method.CREATE)) {
      return new ControllerCreateMethodWrapper(typeElement);
    }
    throw new RuntimeException(String.format("不支持的方法类型:%s", mvcController.method().toString()));
  }
}