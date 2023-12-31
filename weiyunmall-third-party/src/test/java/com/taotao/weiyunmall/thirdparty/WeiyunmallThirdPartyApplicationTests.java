package com.taotao.weiyunmall.thirdparty;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
class WeiyunmallThirdPartyApplicationTests {
    @Resource
    OSSClient ossClient;

    @Test
    void contextLoads() {
    }

    @Test
    void testUpload() throws com.aliyuncs.exceptions.ClientException {
//        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
//        String endpoint = "oss-cn-shanghai.aliyuncs.com";
//        // 强烈建议不要把访问凭证保存到工程代码里，否则可能导致访问凭证泄露，威胁您账号下所有资源的安全。
//        // 本代码示例以从环境变量中获取访问凭证为例。运行本代码示例之前，请先配置环境变量。
//        // RAM用户的访问密钥（AccessKey ID和AccessKey Secret）。
//        String accessKeyId = "";
//        String accessKeySecret = "";
//        // 使用代码嵌入的RAM用户的访问密钥配置访问凭证。
//        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(accessKeyId, accessKeySecret);
//        // 填写Bucket名称，例如examplebucket。
        String bucketName = "weiyunmall";
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName = "geigei.jpg";
        // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
        String filePath= "D:\\Explosion1.png";

        // 创建OSSClient实例。
        //OSS ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider);

        try {
            InputStream inputStream = new FileInputStream(filePath);
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
            // 创建PutObject请求。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            System.out.println("上传完成！！！");
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

}
