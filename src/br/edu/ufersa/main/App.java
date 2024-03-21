package br.edu.ufersa.main;

import br.edu.ufersa.security.RSAImpl;
import br.edu.ufersa.utils.RSAKey;

public class App {
    public static void main(String[] args) {
        
        // Exemplo de assinatura e checagem de assinatura  
        RSAImpl rsaClient = new RSAImpl();
        RSAImpl rsaServer = new RSAImpl();

        RSAKey puKeyClient = rsaClient.getPublicKey();
        RSAKey puKeyServer = rsaServer.getPublicKey();

        String plain_text = "esse trabalho de segurança talvez funcione bem";

        String signed_text = rsaServer.sign(plain_text);
        // String check_text = rsaClient.checkSign(signed_text, puKeyClient); // Não funciona se for uma chave pública diferente
        String check_text = rsaClient.checkSign(signed_text, puKeyServer);

        if (check_text.equals(plain_text)) {
            System.out.println("O texto é o mesmo >:3");
        } else {
            System.out.println("Deu ruim familia :')");
        }
    }
}
