package br.edu.ufersa.service;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;

import br.edu.ufersa.entities.Account;
import br.edu.ufersa.entities.SessionLogin;
import br.edu.ufersa.security.BankCipher;
import br.edu.ufersa.security.RSAImpl;
import br.edu.ufersa.service.skeletons.AuthService;
import br.edu.ufersa.service.skeletons.SessionService;
import br.edu.ufersa.utils.ServicePorts;

public class AuthServiceImpl implements AuthService {

    private static HashMap<Long, Account> accounts;
    private static SessionService stub;
    
    public AuthServiceImpl() {
        accounts = new HashMap<>();
        this.init();
    }

    @Override
    public SessionLogin auth(long accID, String password) throws RemoteException {
        
        Account acc = accounts.get(accID);

        if (acc.getPassword().equals(password)) {

            BankCipher bc = null;
            SessionLogin login = null;

            try {

                bc = new BankCipher();
                login = new SessionLogin(acc.getAccountID(), new RSAImpl(), bc.getKey());
            
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            stub.openSession(accID, login);
            
            return login;    

        } else {
            return null;
        }

    }
    
    @Override
    public SessionLogin record(String pass, String cpf, String name, String addr, String phone) throws RemoteException {
        
        Account acc = new Account(new SecureRandom().nextLong(65536), pass, cpf, name, addr, phone);
        accounts.put(acc.getAccountID(), acc);

        BankCipher bc = null;
        SessionLogin login = null;

        try {
            
            bc = new BankCipher();
            login = new SessionLogin(acc.getAccountID(), new RSAImpl(), bc.getKey());
           
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        stub.openSession(acc.getAccountID(), login);
        

        return login;

    }

    private void record(long accID, String pass, String cpf, String name, String addr, String phone) throws RemoteException {
        
        Account acc = new Account(accID, pass, cpf, name, addr, phone);
        accounts.put(acc.getAccountID(), acc);

    }

    private void init() {
        try {

            Registry reg = LocateRegistry.getRegistry("localhost", ServicePorts.SESSION_PORT.getValue());
            stub = (SessionService) reg.lookup("Session");

            this.record(12345L, "senha123", "12345678910", "Seu toinho", "Rua da lagoa", "988994325");
            this.record(24235L, "senha456", "10987654321", "Seu jão", "Rua da lagoa", "988994234");

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}
