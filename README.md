## 基本说明
### camp-front

**系统B** 的前端。使用最新版`VUE3`与`ELEMENT-PLUS`从零编写。于``Node.js v22``环境下测试和编译。目前仅包含基本**登录登出**功能和基`于Oauth2.0`的**余额查询**功能。

### login

**系统B**的后端。数据库模型采用**任务3**提供的sql。使用``Mybatis-plus``生成基本代码。安全框架采用`Sa-Token`。

### springBoot_shiro_vue-productor ###
模版框架内提供的**系统A**即银行系统的管理前端。基本未做修改。

### springBoot-shiro-outh2-productor ###
模版框架内提供的**系统A**的后端。修改较多，其代码具有大量错误和缺失，无以言表。

## 部署说明 ##

**login**与**springBoot_shiro_vue-productor**在sql目录下有初始化sql脚本，分别为：``login_test-1121-dump.sql`` 和``bank_oauth-1121-dump.sql``。含建表语句和测试数据。在``mysql-8``版本进行过验证。

需要自行创建数据库：
```sql
create database login_test;
create database bank_oauth;
```
需要自行准备好``redis``,以供**springBoot_shiro_vue-productor**使用。
在``redis-7``版本进行过验证。

**login** 在``JDK19``环境下测试，理论上``JDK1.8``也没问题。
**springBoot_shiro_vue-productor** 只能在``JDK1.8``启动。

友情提示：

**springBoot_shiro_vue-productor** 只能于 ```Node.js<=v16``` 版本下运行和编译。