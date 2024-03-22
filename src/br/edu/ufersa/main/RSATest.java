package br.edu.ufersa.main;

import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import br.edu.ufersa.security.BankCipher;
import br.edu.ufersa.security.RSAImpl;
import br.edu.ufersa.utils.RSAKey;

public class RSATest {

    public static void main(String[] args) {
        Scanner cin = new Scanner(System.in);

        try {
            BankCipher bc = new BankCipher();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        // Exemplo de assinatura e checagem de assinatura  
        RSAImpl rsaClient = new RSAImpl();
        RSAImpl rsaServer = new RSAImpl();

        RSAKey puKeyClient = rsaClient.getPublicKey();
        RSAKey puKeyServer = rsaServer.getPublicKey();

        System.out.println("Digite uma frase: ");
        String plain_text = cin.nextLine();

        String signed_text = rsaServer.sign(plain_text);
        // String check_text = rsaClient.checkSign(signed_text, puKeyClient); // Não funciona se for uma chave pública diferente
        String check_text = rsaClient.checkSign(signed_text, puKeyServer);

        cin.close();
        if (check_text.equals(plain_text)) {
            System.out.println("O texto é o mesmo >:3");
        } else {
            System.out.println("Deu ruim familia :')");
        }
    }
}
