package br.edu.ufersa.entities;

import java.io.Serializable;
import javax.crypto.SecretKey;

import br.edu.ufersa.security.RSAImpl;

public class SessionLogin implements Serializable{

    private static final long serialVersionUID = 1L;
    private RSAImpl sessionRSA;
    private SecretKey sKey;

    public SessionLogin(RSAImpl sessionRSA, SecretKey sKey) {
        this.setSessionRSA(sessionRSA);
        this.setSKey(sKey);
    }

    private void setSessionRSA(RSAImpl sessionRSA) {
        if (sessionRSA != null) {
            this.sessionRSA = sessionRSA;
        } else {
            throw new RuntimeException("Erro ao inicializar RSA da sessão");
        }
    }

    private void setSKey(SecretKey sKey) {
        if (sKey != null) {
            this.sKey = sKey;
        } else {
            // throw new RuntimeException("Erro ao inicializar chave do AES da sessão");
            System.err.println("Coisa que eu vou tratar quando terminar de integrar");
        }
    }

    public RSAImpl getSessionRSA() {
        return sessionRSA;
    }

    public SecretKey getsKey() {
        return sKey;
    }

    @Override
    public String toString() {
        return "SessionLogin [rsaPukey=" + sessionRSA.getPublicKey() + ", sKey=" + sKey + "]";
    }

    
}
