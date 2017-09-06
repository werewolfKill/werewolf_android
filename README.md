# werewolf_android
狼人杀Android版


## 测试APK

[v0.9-beta.1 测试版本](https://raw.githubusercontent.com/werewolfKill/werewolf_android/master/app/werewolf-0.9.apk)


## 安装指南

本项目为狼人杀android版本，服务器项目在[werewolf server](https://github.com/werewolfKill/werewolf_server)。若使用首先需安装服务器。
具体过程如下：

1. 下载[服务器代码](https://github.com/werewolfKill/werewolf_server)并打包为jar包，放在服务器任一目录，执行 java -jar werewolf.jar(jar包名)。此过程前提为该服务器需开放9000端口。
2. 下载该项目源码，修改其中`Constants`类中的`app_host`字段，将其改为上步配置的服务器ip地址，`app_port`需与服务器端口保持一致，默认9000。
3. 打包项目为apk,安装到手机即可。


如发现问题，欢迎提Issues。



## 备注

本项目为业余项目，欢迎同学们贡献代码，如觉得做的不错，可以star。在次表示感谢。