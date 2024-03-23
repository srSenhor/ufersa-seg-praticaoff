package br.edu.ufersa.service;

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

import br.edu.ufersa.entities.Account;
import br.edu.ufersa.entities.Message;
import br.edu.ufersa.entities.Request;
import br.edu.ufersa.security.BankCipher;
import br.edu.ufersa.security.RSAImpl;
// import br.edu.ufersa.service.skeletons.AuthService;
import br.edu.ufersa.service.skeletons.BankingService;
import br.edu.ufersa.service.skeletons.SessionService;
import br.edu.ufersa.utils.RSAKey;
import br.edu.ufersa.utils.ServicePorts;

public class BankingServiceImpl implements BankingService {

    private static SessionService sessionStub;
    // private static AuthService authStub;
    private static RSAImpl rsa;
    
    public BankingServiceImpl() {
        rsa = new RSAImpl();
        this.init();
    }
    
    @Override
    public Message receive(long accID, Message message) throws RemoteException {

        RSAKey clientPuKey = sessionStub.getRSAKey(accID);
        String receivedHash = rsa.checkSign(message.getHash(), clientPuKey);
        String content = "";

        try {
            BankCipher bc = new BankCipher(sessionStub.getAESKey(accID));
            
            if(!bc.genHash(message.getContent()).equals(receivedHash)) {
                System.err.println("failed to receive this request, please try again");
                System.err.println("(stop try, weirdo)");
                return new Message("failure", "failure");
            }
            
            content = bc.dec(message.getContent());

        } catch (InvalidKeyException e) {
            e.printStackTrace();
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
        }

        Request req = Request.fromString(content);

        System.err.println("Requisicao" + req.toString());

        return null;
    }

    // TODO: criar um banco de dados (HashMap) das contas aqui e deixar na autenticação só um de usuários
    // private Message showInfo(Request request, BankCipher bc) {

    //     // Account acc = authStub.

    // }
    
    private void init() {
        try {

            Registry reg = LocateRegistry.getRegistry("localhost", ServicePorts.SESSION_PORT.getValue());
            sessionStub = (SessionService) reg.lookup("Session");
            // Registry authReg = LocateRegistry.getRegistry("localhost", ServicePorts.AUTH_PORT.getValue());
            // AuthService stub = (AuthService) authReg.lookup("Auth");

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

    }


}
