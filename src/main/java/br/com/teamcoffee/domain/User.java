package br.com.teamcoffee.domain;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@TypeAlias("user")
public class User implements Serializable {
  @Id private String id;
  @Indexed(unique = true) public String email;
  private String name;
  private String hash;
  private String salt;
  
  public void setPassword(String password) {
    try {
      //byte[] rSalt = RandomUtils.nextBytes(16);
      byte[] rSalt = getSalt();
      this.salt = toHex(rSalt);
      
      char[] aPass = password.toCharArray();
      byte[] aSalt = this.salt.getBytes();
      
      PBEKeySpec spec = new PBEKeySpec(aPass, aSalt, 1024, 512);
      SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      SecretKey key = skf.generateSecret(spec);
      byte[] enc = key.getEncoded();
      this.hash = toHex(enc);
    } 
    catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      e.printStackTrace();
    }
    
    System.out.printf("salt: %s%nhash: %s%npassword: %s", salt, hash, password);
  }
  
  public boolean isValidPassword(String password) {
    try {
      byte[] aSalt = this.salt.getBytes();
      System.out.println("Salt -> " + this.salt);
      PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), aSalt, 1024, 512);

      SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      SecretKey key = skf.generateSecret(spec);
      byte[] enc = key.getEncoded();
      String nHash = toHex(enc);
      
      System.out.printf("This hash:[%s] => New hash:[%s]%n", this.hash, nHash);
      return this.hash.equals(nHash);
    }
    catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return false;
    }
  }
  
  @Override
  public String toString() {
    return String.format("User[id=%s, name='%s', email='%s']", id, name, email);
  }
  
  private byte[] getSalt() throws NoSuchAlgorithmException
  {
      SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
      byte[] salt = new byte[16];
      sr.nextBytes(salt);
      return salt;
  }
  
  private String toHex(byte[] array)
  {
      BigInteger bi = new BigInteger(1, array);
      String hex = bi.toString(16);
      int paddingLength = (array.length * 2) - hex.length();
      
      if(paddingLength > 0)
      {
          return String.format("%0"  +paddingLength + "d", 0) + hex;
      }
      
      return hex;
  }
}
