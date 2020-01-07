package img2md;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Region;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author pengqingsong
 * @date 12/09/2017
 * @desc
 */
public class QiniuHelper {

    private static final UploadManager UPLOAD_MANAGER = new UploadManager(new Configuration(Region.region2()));
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String cndDomain;
    private int uploadRetryTimes = 3;

    public QiniuHelper(String accessKey, String secretKey, String bucket, String cndDomain) {
        this(accessKey, secretKey, bucket, cndDomain, 3);
    }

    public QiniuHelper(String accessKey, String secretKey, String bucket, String cndDomain, int uploadRetryTimes) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucket = bucket;
        this.cndDomain = cndDomain;
        if (uploadRetryTimes <= 0) {
            uploadRetryTimes = 3;
        } else if (uploadRetryTimes > 20) {
            uploadRetryTimes = 20;
        }
        this.uploadRetryTimes = uploadRetryTimes;
    }

    public static String upToken(String accessKey, String secretKey, String bucket) {
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        return upToken;
    }

    public String upToken() {
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        return upToken;
    }

    public String upToken(Long ttlSecond) {
        Auth auth = Auth.create(accessKey, secretKey);
        if (ttlSecond == null || ttlSecond <= 1) {
            return upToken();
        }
        String upToken = auth.uploadToken(bucket, (String) null, ttlSecond, (StringMap) null, true);
        return upToken;
    }

    public String upToken(String key, long expires, StringMap policy, boolean strict) {
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket, key, expires, policy, strict);
        return upToken;
    }

    public String upload(File file, String fileName) {
        RuntimeException ex = new RuntimeException("七牛图片上传失败[未知错误]");
        for (int i = 0; i < uploadRetryTimes; i++) {
            String token = upToken();
            try {
                Response response = UPLOAD_MANAGER.put(file, fileName, token);
                if (response.isOK()) {
                    return cndDomain + fileName;
                }
                ex = new RuntimeException("七牛图片上传失败[" + response.bodyString() + "]");
            } catch (QiniuException e) {
                ex = new RuntimeException("七牛图片上传失败", e);
            }
        }
        throw ex;
    }

    public String upload(byte[] data, String fileName) {
        RuntimeException ex = new RuntimeException("七牛图片上传失败[未知错误]");
        for (int i = 0; i < uploadRetryTimes; i++) {
            String token = upToken();
            try {
                Response response = UPLOAD_MANAGER.put(data, fileName, token);
                if (response.isOK()) {
                    return cndDomain + fileName;
                }
                ex = new RuntimeException("七牛图片上传失败[" + response.bodyString() + "]");

            } catch (QiniuException e) {
                ex = new RuntimeException("七牛图片上传失败", e);
            }
        }
        throw ex;
    }

    public String upload(BufferedImage img, String fileName) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, "PNG", result);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return upload(result.toByteArray(), fileName);
    }

    public String upload(InputStream fileIs, String fileName) {
        RuntimeException ex = new RuntimeException("七牛图片上传失败[未知错误]");
        for (int i = 0; i < uploadRetryTimes; i++) {
            String token = upToken();
            try {
                Response response = UPLOAD_MANAGER.put(fileIs, fileName, token, null, null);
                if (response.isOK()) {
                    return cndDomain + fileName;
                }
                ex = new RuntimeException("七牛图片上传失败[" + response.bodyString() + "]");

            } catch (QiniuException e) {
                ex = new RuntimeException("七牛图片上传失败", e);
            }
        }
        throw ex;
    }


}
