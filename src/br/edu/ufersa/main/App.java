package br.edu.ufersa.main;

import br.edu.ufersa.security.RSAImpl;

public class App {
    public static void main(String[] args) {
        
        RSAImpl rsa = new RSAImpl();

        String plain_text = "esse trabalho de segurança talvez funcione bem";

        String encrypted_text = rsa.encrypt(plain_text);
        String decrypted_text = rsa.decrypt(encrypted_text);

        System.out.println("Mensagem original: " + plain_text); 
        System.out.println("Mensagem cifrada: " + encrypted_text); 
        System.out.println("Mensagem decifrada: " + decrypted_text); 

        if (plain_text.equals(decrypted_text)) {
            System.out.println("Os textos são iguais");
        } else {
            System.out.println("Deu ruim familia");
        }
    }
}
