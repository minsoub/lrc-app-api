package com.bithumbsystems.lrc.management.api.core.util;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Aes 256 util.
 */
@Slf4j
public class AES256Util {

  private static final String ALGORITHM = "AES/GCM/NoPadding";
  private static final int TAG_LENGTH_BIT = 128;  // must be one of {128, 120, 112, 104, 96}
  private static final int IV_LENGTH_BYTE = 12;
  private static final int SALT_LENGTH_BYTE = 16;
  private static final Charset UTF_8 = StandardCharsets.UTF_8;

  public static byte[] getRandomNonce(int length) {
    byte[] nonce = new byte[length];
    new SecureRandom().nextBytes(nonce);
    return nonce;
  }

  // AES 128 bits secret key derived from a password
  public static SecretKey getAESKeyFromPassword(char[] password, byte[] salt)
      throws NoSuchAlgorithmException, InvalidKeySpecException {

    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    // iterationCount = 65536
    // keyLength = 128
    KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
    return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
  }

  // string a base64 encoded AES encrypted text
  public static String encryptAES(String password, String plainMessage) {
    var cipherMessage = "";

    try {
      // 16 bytes salt
      byte[] salt = getRandomNonce(SALT_LENGTH_BYTE);

      // GCM recommends 12 bytes iv
      byte[] iv = getRandomNonce(IV_LENGTH_BYTE);

      // secret key from password
      SecretKey aesKeyFromPassword = getAESKeyFromPassword(password.toCharArray(), salt);

      Cipher cipher = Cipher.getInstance(ALGORITHM);

      // ASE-GCM needs GCMParameterSpec
      cipher.init(Cipher.ENCRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

      byte[] cipherText = cipher.doFinal(plainMessage.getBytes(UTF_8));

      // prefix IV and Salt to cipher text
      byte[] cipherTextWithIvSalt = ByteBuffer.allocate(iv.length + salt.length + cipherText.length)
          .put(iv)
          .put(salt)
          .put(cipherText)
          .array();

      // string representation, base64, send this string to other for decryption.
      cipherMessage = java.util.Base64.getEncoder().encodeToString(cipherTextWithIvSalt);
    } catch(Exception e) {
      log.error(e.getMessage());
      e.printStackTrace();
    }

    return cipherMessage;
  }

  // we need the same password, salt and iv to decrypt it
  public static String decryptAES(String password, String cipherMessage) {
    var plainMessage = "";

    try {
      byte[] decode = Base64.getDecoder().decode(cipherMessage.getBytes(UTF_8));

      // get back the iv and salt from the cipher text
      ByteBuffer bb = ByteBuffer.wrap(decode);

      byte[] iv = new byte[IV_LENGTH_BYTE];
      bb.get(iv);

      byte[] salt = new byte[SALT_LENGTH_BYTE];
      bb.get(salt);

      byte[] cipherText = new byte[bb.remaining()];
      bb.get(cipherText);

      // get back the aes key from the same password and salt
      SecretKey aesKeyFromPassword = getAESKeyFromPassword(password.toCharArray(), salt);
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

      byte[] plainText = cipher.doFinal(cipherText);

      plainMessage = new String(plainText, UTF_8);
    } catch (Exception e) {
      log.error(e.getMessage());
      e.printStackTrace();
    }
    return plainMessage;
  }



  /**
   * Decrypt (AES)
   *
   * @param keyString  the key string
   * @param cipherText the cipher text
   * @return string
   */
  public static String decryptAESLegacy(String keyString, String cipherText) {
    String plainText = "";
    if ((keyString == null) || keyString.length() == 0 || (cipherText == null) || cipherText.length() == 0) {
      return plainText;
    }

    if ((keyString.length() != 16) && (keyString.length() != 24) && (keyString.length() != 32)) {
      return plainText;
    }

    try {
      byte[] keyBytes = keyString.getBytes(StandardCharsets.UTF_8);
      byte[] cipherTextBytes = org.apache.commons.codec.binary.Base64.decodeBase64(cipherText.getBytes(StandardCharsets.UTF_8));

      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      int bsize = cipher.getBlockSize();
      IvParameterSpec ivspec = new IvParameterSpec(Arrays.copyOfRange(keyBytes, 0, bsize));

      SecretKeySpec secureKey = new SecretKeySpec(keyBytes, "AES");
      cipher.init(Cipher.DECRYPT_MODE, secureKey, ivspec);
      byte[] decrypted = cipher.doFinal(cipherTextBytes);

      plainText = new String(decrypted, StandardCharsets.UTF_8);

    } catch (Exception e) {
      plainText = "";
      e.printStackTrace();
    }

    return plainText;
  }
}
