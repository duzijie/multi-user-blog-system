package com.yilan.blog.utils;

import java.io.File;

public class UrlTransferUtils {

    /**
     * 本地绝对路径转换成http地址
     * 如：把
     * D:\img\avatar\A8EB4E79D5904946968BEAADE9C2D25D.png
     * 转换成
     * http://localhost:8083/img/avatar/A8EB4E79D5904946968BEAADE9C2D25D.png
     * @param absolutePath
     * @return
     */
    public String localAbsoluteTransferToHttpUrl(String absolutePath){
//        String absolutePath = "D:/img/avatar/A8EB4E79D5904946968BEAADE9C2D25D.png";
//        String httpUrl = "http://localhost:8083/" + (absolutePath.substring(absolutePath.indexOf("img")).replace("\\","/"));
        String httpUrl = "http://120.24.41.41:8083/" + (absolutePath.substring(absolutePath.indexOf("img")).replace("\\","/"));
        System.out.println(httpUrl);
        return httpUrl;
    }

    public String httpUrlTransferToLocalAbsolute(String httpUrl){
//        String httpUrl = "http://localhost:8083/img/avatar/A8EB4E79D5904946968BEAADE9C2D25D.png";
//        File file = new File("D:/");
        File file = new File("/opt/");
        String localPath = (file.getAbsolutePath()+httpUrl.substring(httpUrl.indexOf("img"))).replace("\\","/");
        System.out.println(localPath);
        return localPath;       //D:/img/avatar/A8EB4E79D5904946968BEAADE9C2D25D.png
    }
}
