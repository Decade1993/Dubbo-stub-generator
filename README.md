# code generator

## Feature

**1. support Spring Security with Dubbo using the annotation @DubboStub**


## Usage

1. modify the build.gradle file

```
compileOnly('com.qxkj.util:generator:{version}')
annotationProcessor('com.qxkj.util:generator:{version}')
```

2. add @DubboStub on the interface of DubboService like this:

```
@DubboStub
public interface DubboService {

}
```