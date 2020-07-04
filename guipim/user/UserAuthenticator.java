package guipim.user;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 用户认证对象
 */
public class UserAuthenticator {

    /**
     * 加密密码
     * @param password 原文
     * @return 密文
     */
    public static String encrypt(String password) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            byte[] digest  = md5.digest(password.getBytes(StandardCharsets.UTF_8));
            digest = md5.digest(digest);
            return new BigInteger(1, digest).toString(16);
        } catch (NoSuchAlgorithmException ignored) {}
        return "";
    }

    /**
     * 认证选择的用户
     * @param password 密码
     * @return 是否认证成功
     */
    public static boolean authenticate(String password) {
        String passwordEncrypted = UserData.getInstance().getPassword();
        if (passwordEncrypted == null) {
            return false;
        }
        return passwordEncrypted.equals(encrypt(password));
    }
}
