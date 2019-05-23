#制定node镜像的版本
FROM java:openjdk-8u92-alpine
#声明作者
MAINTAINER donghui
#移动当前目录下面的文件到app目录下
ADD ./target/eureka-client.jar /home/docker/app/
#进入到app目录下面，类似cd
WORKDIR /home/docker/app

#对外暴露的端口
EXPOSE 8004
#程序启动脚本
ENTRYPOINT ["java","-Dserver.port=8004","-jar","eureka-client.jar"]
