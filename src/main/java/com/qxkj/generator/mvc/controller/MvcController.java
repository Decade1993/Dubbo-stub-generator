package com.qxkj.generator.mvc.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

  /**
   * 每一个controller唯一
   * @return
   */
  Class<?> domain();

  Class<?> returnClass() default Void.class;

  Method method() default Method.PAGE;



  /**
   * 如果指定了这个值，会覆盖MethodType中默认的url值
   * @return
   */
  String url() default "";

  enum Method {

    CREATE(RequestMethod.POST, "", "create", PostMapping.class),
    FIND_BY_ID(RequestMethod.GET, "/{id}", "findById", GetMapping.class),
    PAGE(RequestMethod.GET, "", "page", GetMapping.class),
    UPDATE(RequestMethod.PUT, "", "update", PutMapping.class),
    DELETE(RequestMethod.DELETE, "/{id}/deleted", "delete", DeleteMapping.class);

    RequestMethod method;
    String url;
    String serviceName;
    Class requestMapping;

    Method(RequestMethod method, String url, String serviceName, Class requestMapping) {
      this.method = method;
      this.url = url;
      this.serviceName = serviceName;
      this.requestMapping = requestMapping;
    }
  }
}
