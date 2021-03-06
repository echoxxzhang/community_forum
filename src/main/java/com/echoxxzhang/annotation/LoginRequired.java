package com.echoxxzhang.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 作用目标是 方法
@Target(ElementType.METHOD)
// 运行时起作用
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {

}
