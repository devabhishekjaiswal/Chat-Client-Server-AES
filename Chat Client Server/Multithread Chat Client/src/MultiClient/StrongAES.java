package MultiClient;

import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class StrongAES {
    
    public static void main(String args[]) {
        StrongAES s  = new StrongAES();
        try {
            System.out.println(s.encrypt("hell"));
        } catch (Exception ex) {
            Logger.getLogger(StrongAES.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            System.out.println(s.decrypt(s.encrypt("hell")));
        } catch (Exception ex) {
            Logger.getLogger(StrongAES.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static final String ALGORITHM = "AES";
    private static final byte[] keyValue = 
        new byte[] { 'T', 'h', 'i', 's', 'I', 's', 'A', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y' };

     public String encrypt(String encrypIt) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] result = c.doFinal(encrypIt.getBytes());
        String encryptedData = new BASE64Encoder().encode(result);
        return encryptedData;
    }

    public String decrypt(String encryptedData) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedData = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decrypted = c.doFinal(decryptedData);
        String decryptedValue = new String(decrypted);
        return decryptedValue;
    }

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGORITHM);
        return key;
    }
}