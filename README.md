# maven 构建插件

## 构建项目(springboot)到linux



### 发布到私服

修改pom.xml中的配置

```
<distributionManagement>
        <repository>
            <id>releases</id>
            <url>ip://xxx/repository/releases/</url>
        </repository>
        <snapshotRepository>
            <id>snapshots</id>
            <url>ip://xxx/repository/releases/</url>
        </snapshotRepository>
    </distributionManagement>
```



执行mvn deploy将插件发布到私服



### 使用插件

在需要使用的工程(springboot)里，添加插件依赖 

```
<build>
        <plugins>
            <plugin>
                 <groupId>com.cch.plugin.maven</groupId>
                 <artifactId>ssh-maven-plugin</artifactId>
                 <version>0.0.2-SNAPSHOT</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>deploy</goal>
                        </goals>
                        <configuration>
                            <host>需要将jar上传到哪个服务器地址</host>
                            <user>服务器用户名</user>
                            <pwd>服务器密码</pwd>
                            <port>服务器端口</port>
                            <remotePath>需要将jar包上传到哪个路径下，指定到目录即可</remotePath>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```



使用mvn clean package 即可将jar直接上传到linux服务器中