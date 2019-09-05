# ZuulProxy
基于Zuul实现的身份认证反向代理

## TOKEN令牌

sha256(zuul+yyyyMMdd)

## 访问地址

http://localhost:7890?token=

# Zuul知道

## Zuul简介
Netfix公司出品，常用作设备及网页到后台服务的网关，旨在实现动态路由，监控，弹性和安全性。

## Zuul特性

- **身份认证&安全**： 对每个资源的身份进行验证，只允许白名单通过。

- 洞察&监控： 在边缘跟踪有意义的数据和统计数据，以便为我们提供准确的生产视图

- **动态路由**：根据需要动态的将请求分发到不同的集群

- 压力测试： 增加集群的流量平衡性能

- 限流： 为每种类型的请求分配容量并删除超过限制的请求。

- 静态响应处理： 直接在边缘构建一些响应，而不是将它们转发到内部集群

## Zuul架构：

Zuul有两个版本 1.x和2.x ，两个版本采用不同的架构。

### Zuul 1.x: 

Zuul 1.x的本质是Servlet, 采用多线程阻塞模型。当访问量特别大时，应配合Hystrix使用，防止出现线程池耗尽而导致服务阻塞。

![image](https://github.com/favccxx/ZuulProxy/raw/master/images/zuul1.png)

### Zuul 2.x: 

Zuul 2.x采用Netty实现了异步非阻塞模型，在内部采用消息队列来处理客户端的请求。

![image](https://github.com/favccxx/ZuulProxy/raw/master/images/zuul2.png)

### Zuul 1.x  VS Zuul 2.x

Zuul 1.x 模型简单，开发运维成本低，易调试。相对Zuul 2.x性能相对稍微有一点点点差。
Zuul 2.x 模型复杂，开发运维成本高，性能高。

本项目目前使用的Zuul 1.x版本
