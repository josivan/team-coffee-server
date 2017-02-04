package br.com.teamcoffee.rest.utils;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import spark.Request;

@Component
public class TeamCoffeeUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(TeamCoffeeUtils.class);
  private static SecretKeyFactory SECRET_KEY_FACTORY;
  private static SecureRandom SECURE_RANDOM;

  static {
    try {
      SECRET_KEY_FACTORY = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      SECURE_RANDOM = SecureRandom.getInstance("SHA1PRNG");
    }
    catch (NoSuchAlgorithmException nsaex) {
      LOGGER.error("Error on create Secret Key Factory.", nsaex);
    }
  }

  public JsonObject getBodyAsJsonObject(Request request) {
    return getAsJsonObject(request.body());
  }

  public JsonObject getAsJsonObject(String json) {
    JsonParser parser = new JsonParser();
    return parser.parse(json).getAsJsonObject();
  }

  private byte[] generateSalt() {
    Assert.notNull(SECURE_RANDOM, "SECURE_RANDOM wasn't created.");
    byte[] salt = new byte[16];
    SECURE_RANDOM.nextBytes(salt);
    return salt;
  }

  public String generateSaltAsHexadecimal() {
    return toHex(generateSalt());
  }

  public String generateHashAsHexadecimal(String salt, String password) {
    try {
      PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 1024, 512);
      SecretKey secretKey = SECRET_KEY_FACTORY.generateSecret(spec);
      return toHex(secretKey.getEncoded());
    }
    catch (InvalidKeySpecException e) {
      LOGGER.error("Error on generateHashAsHexadecimal.", e);
      return null;
    }
  }

  private String toHex(byte[] array) {
    BigInteger bi = new BigInteger(1, array);
    String hex = bi.toString(16);
    int paddingLength = (array.length * 2) - hex.length();

    if (paddingLength > 0) {
      return String.format("%0" + paddingLength + "d", 0) + hex;
    }

    return hex;
  }
}
