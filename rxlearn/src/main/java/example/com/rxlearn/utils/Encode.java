package example.com.rxlearn.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2016/6/27.
 */
public class Encode {

    public static String getMD5Str(String str, int offset, int len) {
        MessageDigest messageDigest = null;
        if (str == null)
            return "";

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] bytes = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            if (Integer.toHexString(0xFF & bytes[i]).length() == 1)
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF & bytes[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & bytes[i]));
        }

        return md5StrBuff.substring(offset, len).toString();
    }
}
