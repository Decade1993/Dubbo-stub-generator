package com.qxkj.generator.mvc.controller;

import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Copyright  2020年 generator. All rights reserved.
 * <p>
 * 为了共建和谐社会，请为该类写点注释吧...
 * <p>
 * version 1.0.0
 *
 * @author decade
 * @date 2020/11/5 11:43 上午
 */
public @interface MvcController {

  Class controllerClass() default Void.class;

  Class<?> returnClass() default Void.class;

  Method method() default Method.PAGE;

  /**
   * 如果指定了这个值，会覆盖MethodType中默认的url值
   * @return
   */
  String url() default "";

  enum Method {

    CREATE(RequestMethod.POST, ""),
    FIND_BY_ID(RequestMethod.GET, "/{id}"),
    PAGE(RequestMethod.GET, ""),
    UPDATE(RequestMethod.PUT, ""),
    DELETE(RequestMethod.DELETE, "/{id}/deleted");

    RequestMethod method;
    String url;

    Method(RequestMethod method, String url) {
      this.method = method;
      this.url = url;
    }
  }
}
