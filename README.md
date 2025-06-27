# 志愿者管理系统 (Java 实现版本)

这是一个基于Spring Boot 3实现的志愿者管理系统后端，作为学习Spring Boot框架的练手项目。本项目是对之前[Go语言版本](https://github.com/Minshenyao/volunteer-system-backend)的重构，实现了相同的业务功能，但采用了Java生态系统的最佳实践。前端部分请查看[前端仓库](https://github.com/Minshenyao/volunteer-system-frontend)。

## 项目简介

一个基于Spring Boot 3的志愿者管理平台后端，支持活动发布、报名、签到等基本功能：

- 后端：Spring Boot 3 + Spring Security + MySQL
- 前端：Vue3 + Element Plus（详见前端仓库）
- 特点：采用JPA自动建表，RESTful API设计

## 主要功能

- 用户认证与权限管理（基于Spring Security）
- 志愿者信息管理
- 活动发布与报名
- 签到管理
- 志愿时长统计
- 文件上传（支持阿里云OSS）

## 技术栈

### 后端
- Spring Boot 3
- Spring Security
- Spring Data JPA
- MySQL 数据库
- JWT 认证
- 阿里云OSS对象存储
- Swagger/OpenAPI 接口文档

## 快速开始

### 1. 获取代码
```bash
# 下载代码
git clone https://github.com/your-username/volunteer-system-backend.git

# 进入项目目录
cd volunteer-system-backend

# 复制配置文件模板
cp src/main/resources/application.properties.bak src/main/resources/application.properties
```

### 2. 准备工作

#### 创建MySQL数据库
```sql
CREATE DATABASE volunteer_db;
```

#### 修改配置文件
编辑 `src/main/resources/application.properties` 填写相关配置：

```yaml
spring.application.name=volunteer-system-backend

spring.datasource.url=jdbc:mysql://127.0.0.1:3306/volunteer_db?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
#spring.jpa.hibernate.ddl-auto=create
spring.datasource.username=
spring.datasource.password=

jwt.secret=

aliyun.oss.endpoint=
aliyun.oss.access-key-id=
aliyun.oss.access-key-secret=
aliyun.oss.bucket-name=
aliyun.oss.folder=
```

### 3. 启动服务
```bash
# 使用Maven打包
./mvnw clean package

# 运行应用
java -jar target/volunteer-system-backend-0.0.1-SNAPSHOT.jar
```

### 4. 默认管理员账号
```
该项目没有写自动创建管理员账号，需要手动创建。
```

## 环境要求
- JDK 17+
- Maven 3.8+
- MySQL 8.0+

## 特性说明
- 自动建表：使用JPA的自动建表特性，首次启动时自动创建数据表结构
- 安全性：集成Spring Security，提供完善的认证和授权机制
- 接口文档：集成OpenAPI 3.0，提供在线API文档
- 文件存储：集成阿里云OSS进行文件存储
- 数据验证：使用Spring Validation进行请求参数验证

## 与Go版本的区别
- 采用Spring Boot 3框架，更适合企业级应用开发
- 完整的依赖注入支持，便于进行单元测试
- 更强大的ORM支持，包含完整的事务管理
- 集成更多企业级特性，如更完善的日志系统、监控等

## 说明
这是一个学习Spring Boot 3的练手项目，实现了与Go版本相同的功能，但采用了Java生态系统的最佳实践。虽然在性能上可能不如Go版本，但在功能完整性和企业特性支持上有其独特优势。欢迎交流学习，如有问题或建议，欢迎提出。
