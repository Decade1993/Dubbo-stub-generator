package com.qxkj.generator.dubbo;

import com.qianxun.util.CurrentUserUtil;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.apache.dubbo.rpc.RpcContext;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Dubbo Stub 本地存根类
 * 编译时生成
 */
public class MapperWrapper {

  public static final String SUFFIX = "Stub";

  private static final String PROXY_NAME = "proxy";

  private TypeElement annotatedElement;

  private String[] excludeMethodNamesArray = {"getClass", "hashCode", "equals", "toString", "notify", "notifyAll", "wait"};

  private List<String> excludeMethodNames = Arrays.asList(excludeMethodNamesArray);

  private String qualifiedName;

  private String simpleTypeName;

  private String packageName;


  MapperWrapper(TypeElement annotatedElement) {
    this.annotatedElement = annotatedElement;
    this.qualifiedName = annotatedElement.getQualifiedName().toString();
    int index = qualifiedName.lastIndexOf(".");
    this.packageName = qualifiedName.substring(0, index);
    this.simpleTypeName = qualifiedName.substring(index + 1);

  }

  void generateCode(Elements elementUtils, Filer filer) throws IOException {
    String generateClassName = annotatedElement.getSimpleName() + SUFFIX;

    // 构造器
    MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(TypeName.get(annotatedElement.asType()), PROXY_NAME)
            .addStatement("this.$N = $N", PROXY_NAME, PROXY_NAME);

    // 代理对象
    FieldSpec.Builder proxy = FieldSpec.builder(TypeName.get(annotatedElement.asType()),
            PROXY_NAME, Modifier.PRIVATE);

    // 类
    TypeSpec.Builder builder = TypeSpec
            .classBuilder(generateClassName)
            .addModifiers(Modifier.PUBLIC)
            .addField(proxy.build())
            .addMethod(constructor.build())
            .addSuperinterface(annotatedElement.asType());


    // 实现接口方法
    List<? extends Element> allMembers = elementUtils.getAllMembers(annotatedElement);
    List<ExecutableElement> methods = allMembers.stream()
            .filter(it -> !excludeMethodNames.contains(it.getSimpleName().toString()))
            .map(it -> (ExecutableElement)it)
            .collect(Collectors.toList());
    if (methods.size() > 0) {
      for (ExecutableElement method : methods) {
        List<? extends VariableElement> parameters = method.getParameters();
        List<ParameterSpec> parameterSpecList = new ArrayList<>();
        String params = "";
        if (parameters != null && parameters.size() > 0) {
          for (VariableElement parameter : parameters) {
            ParameterSpec param = ParameterSpec.builder(TypeName.get(parameter.asType()), parameter.toString())
                    .build();
            parameterSpecList.add(param);
          }
          params = parameters.stream().map(it -> it.toString()).collect(Collectors.joining(","));
        }
        TypeMirror returnType = method.getReturnType();
        String returnPre = returnType.getKind().equals(TypeKind.VOID) ? "" : "return ";
        builder.addMethod(
                MethodSpec.methodBuilder(method.getSimpleName().toString())
                        .addAnnotation(ClassName.get("java.lang", "Override"))
                        .addModifiers(Modifier.PUBLIC)
                        .addParameters(parameterSpecList)
                        .addStatement("$T.getContext().setObjectAttachment(\"user\", $T.getUserDetails())", RpcContext.class, CurrentUserUtil.class)
                        .addStatement(returnPre + "this.$N.$N($N)", PROXY_NAME,
                                method.getSimpleName().toString(),
                                params)
                        .returns(TypeName.get(returnType))
                        .build()

        );
      }
    }

    TypeSpec typeSpec = builder.build();
    JavaFile.builder(packageName, typeSpec).build().writeTo(filer);
  }

}
