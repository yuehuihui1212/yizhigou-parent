package com.yizhigou;

import org.csource.fastdfs.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main ( String[] args )throws Exception
    {
        ClientGlobal.init("E:\\java\\ideaWorkSpace\\yizhigou-parent\\fastDFSdemo\\src\\main\\resources\\fdfs_client.conf");
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageServer storageServer=null;
        StorageClient storageClient = new StorageClient(trackerServer,storageServer);
        String[] file = storageClient.upload_file("E:\\picture\\xiazi.jpg", "jpg", null);
        for(String name:file){
            System.out.println(name);
        }
    }
}
