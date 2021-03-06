# Tool for java
*******************
[![Maintainability](https://api.codeclimate.com/v1/badges/86a89f04514045c8246d/maintainability)](https://codeclimate.com/github/Kuangcp/kcp-tool-java/maintainability)
[![codebeat badge](https://codebeat.co/badges/9a4f8f3c-8edb-41ed-b43a-2fe6d18c3c11)](https://codebeat.co/projects/github-com-kuangcp-kcp-tool-java-master)
[![License](https://img.shields.io/badge/license-Apache%20License%202.0-brightgreen.svg)](LICENSE)

***************

| 模块 | 功能 |
|:----|:----|
| kcp-tool  | 以下模块的总和 |
| kcp-math  | 数学工具      |
| kcp-mock  | mock数据      |
| kcp-core  | 基础工具      |
| kcp-tuple | 元组         |

## 使用
由于没有发布到中央仓库, 只是简单的发布到了码云上

### 使用远程库
> [使用说明](https://gitee.com/gin9/MavenRepos)

### 手动安装
1. `git clone --depth 1 https://gitee.com/gin9/kcp-tool-java.git`
1. `gradle install -x test`

## 引入依赖
**`Gradle`**  
```groovy
    implementation 'com.github.kuangcp:kcp-tool:x.y.z'
```

**`Maven`**
```xml
    <dependency>
      <groupId>com.github.kuangcp</groupId>
      <artifactId>kcp-tool</artifactId>
      <version>x.y.z</version>
    </dependency>
```

