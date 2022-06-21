# 该镜像需要依赖的基础镜像
FROM livingobjects/jre8
MAINTAINER wsj
ENV LANG en_US.UTF-8
ENV LC_ALL en_US.UTF-8
ENV LANGUAGE en_US:en
# 调整时区
RUN rm -f /etc/localtime \
&& ln -sv /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
&& echo "Asia/Shanghai" > /etc/timezone
# 将当前目录下的jar包复制到docker容器的/目录下
ADD ./target/wx.jar /app/app.jar
# 指定docker容器启动时运行jar包
EXPOSE 80
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8","-Dsun.jnu.encoding=UTF-8","-jar","app/app.jar"]
