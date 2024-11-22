
#### 介绍
基于spring boot 2.3.6、shiro、jwt、redis、mybatis  后台管理系统

#### 软件架构
软件架构说明
* 核心框架：spring boot 2.3.6
* 持久层框架：mybatis
* 数据库连接池：alibaba druid
* 安全框架：apache shiro
* 无状态 JWT
* 缓存框架：redis(自定义 RedisTemplate 序列化)
* 日志框架：logback
* 前端模板：thymeleaf+layui2 或 VUE

#### **部署**

- 下载redis 启动redis
- 创建bank_oauth数据库
- 导入bank_oauth.sql
- 启动项目
- 登录地址 http://localhost:8090/index/login
- 登录用户名密码  admin 666666


