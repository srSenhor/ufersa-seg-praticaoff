package br.edu.ufersa.service;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import br.edu.ufersa.entities.Account;
// import br.edu.ufersa.entities.Account;
import br.edu.ufersa.entities.Message;
import br.edu.ufersa.entities.Request;
import br.edu.ufersa.entities.SessionLogin;
import br.edu.ufersa.security.BankCipher;
import br.edu.ufersa.security.RSAImpl;
// import br.edu.ufersa.service.skeletons.AuthService;
import br.edu.ufersa.service.skeletons.BankingService;
import br.edu.ufersa.service.skeletons.SessionService;
import br.edu.ufersa.utils.RSAKey;
import br.edu.ufersa.utils.ServicePorts;

public class BankingServiceImpl implements BankingService {

    private static HashMap<Long, Account> accounts;
    private static SessionService sessionStub;
    // private static AuthService authStub;
    private static RSAImpl rsa;
    
    public BankingServiceImpl() {
        accounts = new HashMap<>();
        rsa = new RSAImpl();
        this.init();
    }
    
    @Override
    public Message receive(long accID, Message message) throws RemoteException {

        RSAKey clientPuKey = sessionStub.getRSAKey(accID);
        String receivedHash = rsa.checkSign(message.getHash(), clientPuKey);
        BankCipher bc = null;
        String content = "";
        
        // TODO: Remover isso depois de debugar
        // String responseTest = "";
        // String responseTestHash = "";

        try {
            bc = new BankCipher(sessionStub.getAESKey(accID));
            
            if(!bc.genHash(message.getContent()).equals(receivedHash)) {
                System.err.println("failed to receive this request, please try again");
                System.err.println("(stop try, weirdo)");
                return new Message("failure", "failure");
            }

            // responseTest = bc.enc("DEU BOM FAMILIA");
            // responseTestHash = bc.genHash(responseTest);
            // responseTestHash = rsa.sign(responseTestHash);
            
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

        // System.err.println("Requisicao: " + req.toString());

        switch (req.getOpType()) {
            case 1:
                return this.showInfo(req, bc);
            default:
                return null;
        }
        

        // return new Message("", responseTestHash);
    }

    private Message showInfo(Request request, BankCipher bc){

        Account acc = accounts.get(request.getSenderID());
        String response = null;
        String responseHash = null;

        if (acc == null) {
            response = "Não foi possível encontrar as informações da sua conta";
        } else {
            response = acc.toString();
        }

        try {

            response = bc.enc(response);
            responseHash = bc.genHash(response);
            responseHash = rsa.sign(responseHash);

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

        return new Message(response, responseHash);
        
    }
    
    // TODO: transportar isso, posteriormente, para um server socket que vai tratar de receber a conexão com os serviços
    @Override
    public SessionLogin record(String className, String pass, String cpf, String name, String addr, String phone) throws RemoteException {
        
        if (className.equalsIgnoreCase(AuthServiceImpl.class.getName())) {
            Account acc = new Account(new SecureRandom().nextLong(65536), pass, cpf, name, addr, phone);
            accounts.put(acc.getAccountID(), acc);
    
            BankCipher bc = null;
            SessionLogin login = null;
    
    
            try {
                
                bc = new BankCipher();
                login = new SessionLogin(acc.getAccountID(), new RSAImpl(), rsa.getPublicKey(), bc.getKey());
               
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            return login;
            
        } else {

            System.err.println("Não foi possível criar a conta");
            return null;

        }
    }

    
    @Override
    public RSAKey getPuKey() throws RemoteException {
        return rsa.getPublicKey();
    }
    
    
    private void record(long accID, String pass, String cpf, String name, String addr, String phone) throws RemoteException {
        
        Account acc = new Account(accID, pass, cpf, name, addr, phone);
        accounts.put(acc.getAccountID(), acc);

    }

    private void init() {
        try {

            Registry reg = LocateRegistry.getRegistry("localhost", ServicePorts.SESSION_PORT.getValue());
            sessionStub = (SessionService) reg.lookup("Session");
            // Registry authReg = LocateRegistry.getRegistry("localhost", ServicePorts.AUTH_PORT.getValue());
            // AuthService stub = (AuthService) authReg.lookup("Auth");

            this.record(12345L, "senha123", "12345678910", "Seu toinho", "Rua da lagoa", "988994325");
            this.record(67890L, "senha456", "10987654321", "Seu jão", "Rua da lagoa", "988994234");


        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

    }
}