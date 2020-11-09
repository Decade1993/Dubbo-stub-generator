package com.qxkj.generator.mvc.controller;

import com.squareup.javapoet.MethodSpec;

import java.util.List;

/**
 * Copyright  2020年 generator. All rights reserved.
 * <p>
 * 为了共建和谐社会，请为该类写点注释吧...
 * <p>
 * version 1.0.0
 *
 * @author decade
 * @date 2020/11/9 3:54 下午
 */
public interface MethodWrapper {

  List<MethodSpec> getControllerMethodSpecs();

  List<MethodSpec> getInterfaceMethodSpecs();
}
