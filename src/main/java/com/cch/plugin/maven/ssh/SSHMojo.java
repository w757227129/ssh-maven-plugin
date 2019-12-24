package com.cch.plugin.maven.ssh;


import com.cch.plugin.maven.ssh.util.JSchExecutor;
import com.jcraft.jsch.JSchException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


@Mojo(name = "deploy", defaultPhase = LifecyclePhase.PACKAGE, requiresProject = true, threadSafe = true,
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME,
        requiresDependencyCollection = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class SSHMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.build.directory}", readonly = true, required = true)
    private String buildDirectory;

    @Parameter(defaultValue = "${project.build.finalName}", readonly = true, required = true)
    private String appJarFileName;

    @Parameter(property = "host")
    private String host;

    @Parameter(property = "user")
    private String user;

    @Parameter(property = "pwd")
    private String pwd;

    @Parameter(property = "port")
    private Integer port;

    @Parameter(property = "remotePath")
    private String remotePath;

    public void execute()throws MojoExecutionException {

        JSchExecutor jSchExecutor = new JSchExecutor(user,pwd,host,port);

        getLog().info("开始连接服务器:"+host);
        try {
            jSchExecutor.connect();
        } catch (JSchException e) {
            getLog().error("连接服务器出现错误");
            e.printStackTrace();
        }


        File srcFile = new File(buildDirectory,appJarFileName+".jar");

        if(!srcFile.exists()){
            getLog().error(srcFile.getAbsolutePath()+"不存在");
            return;
        }

        String srcFilePath = srcFile.getAbsolutePath();


        getLog().info("开始传输文件"+srcFilePath+"->"+host+":"+remotePath);

        try {
            jSchExecutor.uploadFile(srcFilePath,remotePath);
        } catch (Exception e) {
            getLog().error("上传文件出现错误");
            e.printStackTrace();
        }
        getLog().info(srcFilePath+"上传结束");
        jSchExecutor.disconnect();

    }


}
