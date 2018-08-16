/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 *
 * @author Tomáš
 */
public class Encryptor {

    public void test() {
        try {

            KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
            SecretKey myDesKey = keygenerator.generateKey();
            Cipher desCipher;

            // Create the cipher 
            desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

            // Initialize the cipher for encryption
            desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);

            //sensitive information
            byte[] text = "supersonic".getBytes();

            System.out.println("Text [Byte Format] : " + text);
            System.out.println("Text : " + new String(text));

            // Encrypt the text
            byte[] textEncrypted = desCipher.doFinal(text);

            System.out.println("Text Encryted : " + textEncrypted);

            // Initialize the same cipher for decryption
            desCipher.init(Cipher.DECRYPT_MODE, myDesKey);

            // Decrypt the text
            byte[] textDecrypted = desCipher.doFinal(textEncrypted);

            System.out.println("Text Decryted : " + new String(textDecrypted));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

    }

    public byte[] encrypt(String data) {
//        byte[] textEncrypted = null;
//        try {
//            KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
//            SecretKey myDesKey = keygenerator.generateKey();
//            Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
//            desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
//            byte[] text = data.getBytes();
//
//            textEncrypted = desCipher.doFinal(text);
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException ex) {
//            Logger.getLogger(Encryptor.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (NoSuchPaddingException ex) {
//            Logger.getLogger(Encryptor.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return textEncrypted;

        return data.getBytes();
    }

    public String decrypt(byte[] textEncrypted) {
//        byte[] textDecrypted = null;
//
//        try {
//            KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
//            SecretKey myDesKey = keygenerator.generateKey();
//            Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
//            desCipher.init(Cipher.DECRYPT_MODE, myDesKey);
//            System.out.println(textEncrypted.length);
//            textDecrypted = desCipher.doFinal(textEncrypted);
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException ex) {
//            Logger.getLogger(Encryptor.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (NoSuchAlgorithmException ex) {
//            Logger.getLogger(Encryptor.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return new String(textDecrypted);
//    }
        return new String(textEncrypted);
    }
}
