package MultiServer;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class StrongAES {
    
    public String runEncrypt(String text) {
        
      try {
          
         String key = "Bar12345Bar12345"; // 128 bit key

         // Create key and cipher
         Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
         Cipher cipher = Cipher.getInstance("AES");

         // encrypt the text
         cipher.init(Cipher.ENCRYPT_MODE, aesKey);
          byte[] encrypted = cipher.doFinal(text.getBytes());
         System.out.println(new String(encrypted));

         return (new String(encrypted));
      }catch(Exception e) {
         e.printStackTrace();
      }
      return null;
    }
    
    public String runDecrypt(String encrypted) {
      try {
          
         String key = "Bar12345Bar12345"; // 128 bit key

         // Create key and cipher
         Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
         Cipher cipher = Cipher.getInstance("AES");

         // encrypt the tex
        

         // decrypt the text
         cipher.init(Cipher.DECRYPT_MODE, aesKey);
         String decrypted = new String(cipher.doFinal(encrypted.getBytes()));
         System.out.println(decrypted);
         
         return decrypted;
      }catch(Exception e) {
         e.printStackTrace();
      }
      return "WRONG\n";
    }
   
   

    /*public static void main(String[] args) {
       StrongAES app = new StrongAES();
       app.run();
    }*/
}