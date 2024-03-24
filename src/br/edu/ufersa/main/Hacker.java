package br.edu.ufersa.main;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import br.edu.ufersa.entities.Message;
import br.edu.ufersa.entities.Request;
import br.edu.ufersa.security.BankCipher;
import br.edu.ufersa.security.RSAImpl;
import br.edu.ufersa.service.skeletons.BankingService;
import br.edu.ufersa.utils.ServicePorts;

public class Hacker {
    
    public static void main(String[] args) throws RemoteException, NotBoundException {
        
        Registry bankingReg = LocateRegistry.getRegistry("localhost", ServicePorts.BANKING_PORT.getValue());
        BankingService bankingStub = (BankingService) bankingReg.lookup("Banking");

        String hackerReq = new Request(2, 12345, -1, -1, 1000.0f).toString();

        // Tentando sem um hash assinado
        // try {
        //     BankCipher bc = new BankCipher();

        //     hackerReq = bc.enc(hackerReq);
            
        //     String hash = bc.genHash(hackerReq);           
            
        //     Message response = bankingStub.receive(12345, new Message(hackerReq.toString(), hash));
            
        //     if (response == null) {
        //         System.err.println("Tentativa frustrada :(");
        //     } else {

        //         if(!bc.genHash(response.getContent()).equals(response.getHash())) {
        //             System.err.println("an error has ocurred, please try again");
        //         } else {
        //             System.err.println(bc.dec(response.getContent()));
        //         }
                
        //     }
            
            
        // } catch (NoSuchAlgorithmException e) {
        //     e.printStackTrace();
        // } catch (NoSuchPaddingException e) {           
        //     e.printStackTrace();
        // } catch (InvalidAlgorithmParameterException e) {            
        //     e.printStackTrace();
        // } catch (IllegalBlockSizeException e) {            
        //     e.printStackTrace();
        // } catch (BadPaddingException e) {           
        //     e.printStackTrace();
        // } catch (InvalidKeyException e) {
        //     e.printStackTrace();
        // } catch (RemoteException e) {
        //     e.printStackTrace();
        // }
        
        // Tentando com hash
        try {
            BankCipher bc = new BankCipher();
            RSAImpl rsa = new RSAImpl();

            hackerReq = bc.enc(hackerReq);
            
            String hash = bc.genHash(hackerReq);           
            hash = rsa.sign(hash);
            
            Message response = bankingStub.receive(12345, new Message(hackerReq.toString(), hash));
            
            if (response == null) {
                System.err.println("Tentativa frustrada :(");
            } else {
                String responseTestHash = rsa.checkSign(response.getHash(), bankingStub.getPuKey());
                
                if(!bc.genHash(response.getContent()).equals(responseTestHash)) {
                    System.err.println("an error has ocurred, please try again");
                } else {
                    System.err.println(bc.dec(response.getContent()));
                }
                
            }
            
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {           
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {            
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {            
            e.printStackTrace();
        } catch (BadPaddingException e) {           
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
}
