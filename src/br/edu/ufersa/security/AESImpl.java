package br.edu.ufersa.security;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class AESImpl {

    private static final String ALG = "AES/CBC/PKCS5Padding";
    private KeyGenerator keygen;
    private SecretKey skey;
    private static IvParameterSpec iv;
    private String message;
    private String encryptedMessage;

    public AESImpl(int keySize) throws NoSuchAlgorithmException {
        this.generateKey(keySize);
        this.iv = generateIv();
    }

    public AESImpl(SecretKey skey) throws NoSuchAlgorithmException {
        this.skey = skey;
        this.iv = generateIv();
    }

    public AESImpl(SecretKey skey, IvParameterSpec iv) throws NoSuchAlgorithmException {
        this.skey = skey;
        this.iv = iv;
    }

    public SecretKey getKey() {
        return this.skey;
    }

    public IvParameterSpec getIv() {
        return this.iv;
    }

    private void generateKey(int keySize) throws NoSuchAlgorithmException {
        keygen = KeyGenerator.getInstance("AES");
        keygen.init(keySize);
        skey = keygen.generateKey();
    }

    private static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
    
    //TODO: Implementar a interface e tentar fazer na memória antes de implementar o serviço de thread e sockets com topologia em anel

    public String encrypt(String message) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

        byte[] encryptedMessageBytes;
        Cipher cipher;

        this.message = message;

        cipher = Cipher.getInstance(ALG);
        cipher.init(Cipher.ENCRYPT_MODE, skey, iv);

        encryptedMessageBytes = cipher.doFinal(this.message.getBytes());

        this.encryptedMessage = code(encryptedMessageBytes);
        return this.encryptedMessage;
    }

    public String decrypt(String message) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        byte[] encryptedMessageBytes = decode(message);
        
        Cipher decipher = Cipher.getInstance(ALG);
        decipher.init(Cipher.DECRYPT_MODE, skey, iv);
        
        byte[] decryptedMessageBytes = decipher.doFinal(encryptedMessageBytes);
        String decryptedMessage = new String(decryptedMessageBytes);
        
        this.message = decryptedMessage;
        return this.message;
    }

    private byte[] decode(String codedMessage) {
        return Base64.getDecoder().decode(codedMessage);
    }
    private String code(byte[] encryptedMessage) {
        return Base64.getEncoder().encodeToString(encryptedMessage);
    }


}

