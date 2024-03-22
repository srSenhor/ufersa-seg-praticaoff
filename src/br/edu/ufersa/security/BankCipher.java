package br.edu.ufersa.security;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
// import javax.crypto.spec.IvParameterSpec;

public class BankCipher {

    private AESImpl aes;
    private SecretKey skey;
    // private IvParameterSpec iv;

    public BankCipher() throws NoSuchAlgorithmException {
        this.aes = new AESImpl(192);
        this.skey = aes.getKey();
    }

    public BankCipher(SecretKey skey) throws NoSuchAlgorithmException {
        this.aes = new AESImpl(skey);
        this.skey = skey;
        // this.iv = aes.getIv();
    }

    // public BankCipher(SecretKey skey, IvParameterSpec iv) throws NoSuchAlgorithmException {
    //     this.aes = new AESImpl(skey, iv);
    //     this.skey = skey;
    //     this.iv = iv;
    // }

    public SecretKey getKey() {
        return skey;
    }

    // public IvParameterSpec getIv(){
    //     return this.iv;
    // }
    
    public String enc(String message) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException { 
        return aes.encrypt(message); 
    }

    public String dec(String message) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException { 
        return message = aes.decrypt(message);
    }

    public String genHash(String message) {
        return HMACImpl.hMac(skey.toString(), message);
    }
}
