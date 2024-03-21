package br.edu.ufersa.security;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RSAImpl {

    private BigInteger e;
    private BigInteger d;
    private BigInteger n;

    public RSAImpl() {
        this.init();    
    }
    
    // Vamos revisar o passo a passo do RSA

    private void init() {

        // Passo 1: Sortear números primos grandes (BigInteger) aleatórios p e q
        BigInteger p = BigInteger.probablePrime(1024, new SecureRandom());
        BigInteger q = p.nextProbablePrime();

        // System.out.println("Números primos escolhidos:");
        // System.out.println("p = " + p);
        // System.out.println("q = " + q);
        // System.out.println("==========================");
        
        // Passo 2: Calcular o módulo n, onde n = p * q
        // BigInteger n = p.multiply(q);
        this.n = p.multiply(q);
        
        // System.out.println("Módulo de n calculado:");
        // System.out.println("n = " + p + " * " + q + " = " + n);
        // System.out.println("======================");

        // Passo 3: Calcular phi de Euler de n, considerando que phi(n) = (p-1) * (q-1)
        BigInteger phi = phi(p, q);

        // System.out.println("Phi de Euler (coprimos) calculado:");
        // System.out.println("phi(" + n + ") = " + phi);
        // System.out.println("==================================");

        // Passo 4: Calcular o expoente e (chave pública) tal que mdc(e, phi(n)) = 1 e 1 < e < phi(n)
        // BigInteger e = calculateE(phi);
        this.e = calculateE(phi);

        // System.out.println("E calculado:");
        // System.out.println("número que satisfaz mdc(e, " + n + ") = " + e);
        // System.out.println("==================================");
        
        // Passo 5: Calcular o expoente d (chave privada) tal que (d * e) mod phi(n) = 1
        // BigInteger d = e.modInverse(phi);
        this.d = e.modInverse(phi);

        // System.out.println("D calculado:");
        // System.out.println("inverso modular e mod phi(" + n + ")  = " + d);
        // System.out.println("==================================");

        // Passo 6: Compôr as chaves pública e privada com o módulo n e os expoentes e e d
        // System.out.println("Chave pública {" + e + ", " + n + "}");
        // System.out.println("Chave privada {" + d + ", " + n + "}");
    }


    // Passo 7 (cifragem): Char cifrado = (char mensagem)^e mod n
    public String encrypt(String message){
        StringBuilder encrypted_message = new StringBuilder();

        for (char ch : message.toCharArray()) {
            int i = ch;
            BigInteger m = new BigInteger(Integer.toString(i));
            BigInteger c = m.modPow(e, n);
            // System.out.println("Char: " + ch + " | Codigo ASCII:" + m);
            encrypted_message.append(c + " ");
        }

        return encrypted_message.toString();
    }

    // Passo 8 (decifragem): Char decifrado = (char cifrado)^d mod n
    public String decrypt(String message){
        StringBuilder decrypted_message = new StringBuilder();

        String chars[] = message.split(" ");
        for (String ch : chars) {
            BigInteger c = new BigInteger(ch);
            BigInteger m = c.modPow(d, n);
            char v = (char) m.intValue();
            decrypted_message.append(v);
        }

        return decrypted_message.toString();
    }


    private static BigInteger phi(BigInteger p, BigInteger q) {
        return p.subtract(new BigInteger("1")).multiply(q.subtract(new BigInteger("1")));
    }

    private static BigInteger calculateE(BigInteger phi){
        BigInteger e = new BigInteger("2");
        BigInteger mdc = e.gcd(phi);
        
        do {
            if (e.isProbablePrime(1)) {
                mdc = e.gcd(phi);
            }
            e = e.add(BigInteger.ONE);
        } while (mdc.compareTo(new BigInteger("1")) != 0 && e.compareTo(phi) < 0);
        
        // System.out.println("e é um primo. e = " + e);
        return e.subtract(BigInteger.ONE);     
    }
}