package com.qxkj.generator.dubbo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright  2020年 dubbo-stub-generator. All rights reserved.
 *
 * 用于在java编译时自动生成Dubbo Stub类
 *  Stub类的功能是：
 *    将Spring Security领域中的用户信息传到Dubbo的RPC上下文中，
 *    以便RPC调用时客户端和服务端能以同一认证用户进行业务处理，并且无感于业务层
 *
 * version 1.0.0
 *
 * @author decade
 * @date 2020/9/28 11:07 上午
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface DubboStub {

}
