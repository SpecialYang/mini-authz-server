## Mini Authz Server
这个仓库是在 [spring-boot-jwt](https://github.com/murraco/spring-boot-jwt) 基础上进行修改的，它实现了一个小型的用户登录系统。你可以创建用户，然后用户登录并获取登录态Token。

你可以把它当做一个小型的基于用户的授权系统，只有用户登录成功并获得授权服务颁发的Token，才能访问你要保护的业务系统。

由于Mini Authz Server颁发的Token的格式符合标准JWT规范，借助于JWT本身的自包含和防篡改特性，只要你持有Mini Authz Server中私钥对应的公钥，你可以在任何地方来验证Token的有效性。

通常情况下，为了解耦业务代码与验签代码，往往会在网关直接校验Token的有效性。

## 使用
### 部署
#### 本地部署
直接运行MiniAuthzServerApp类即可

#### Docker 部署
```
// 构建镜像
make docker-build

// 推送远端仓库 （可选）
make docker-push

docker run -d mini-authz-server
```

注意：修改docker仓库

#### K8s 部署
```
make k8s-deploy
```
注意：你需要替换成你自己的镜像

### 访问

```
// 注册用户
curl --location --request POST 'localhost:8080/users/signup' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "yang",
    "password": "123456"
}'

// 登录并获取Token
curl --location --request POST 'localhost:8080/users/signin' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "yang",
    "password": "123456"
}'

```

## 注意
1. 本项目为了快速部署、快速验证，所以用户的数据库使用是内存形式，也就是说默认数据库仅仅是用来快速测试。并且每次部署都会重新初始化内存数据库，所以在你准备用于生产之前请修改默认的数据库配置。
2. 本项目默认使用的注册中心是Nacos，如你的其他服务需要访问该服务，请正确修改Nacos的服务地址。
3. 本项目JWT的使用非对称加密RSA，默认配置中的公钥、私钥仅用于快速测试。