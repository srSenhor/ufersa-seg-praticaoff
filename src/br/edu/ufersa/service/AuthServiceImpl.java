package br.edu.ufersa.service;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import br.edu.ufersa.entities.SessionLogin;
import br.edu.ufersa.entities.User;
import br.edu.ufersa.security.BankCipher;
import br.edu.ufersa.security.RSAImpl;
import br.edu.ufersa.service.skeletons.AuthService;
import br.edu.ufersa.service.skeletons.BankingService;
import br.edu.ufersa.service.skeletons.SessionService;
import br.edu.ufersa.utils.RSAKey;
import br.edu.ufersa.utils.ServicePorts;

public class AuthServiceImpl implements AuthService {

    private static HashMap<Long, User> users;
    private static SessionService sessionStub;
    private static BankingService bankStub;
    private static RSAKey serverPuKey;
    
    public AuthServiceImpl() {
        users = new HashMap<>();
        this.init();
    }

    @Override
    public SessionLogin auth(long accID, String password) throws RemoteException {
        
        User user = users.get(accID);

        if (!user.isLogged() && user.getPassword().equals(password)) {

            BankCipher bc = null;
            SessionLogin login = null;

            try {

                bc = new BankCipher();
                login = new SessionLogin(user.getAccID(), new RSAImpl(), serverPuKey, bc.getKey());
            
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            
            user.userLoggedIn();
            sessionStub.openSession(accID, login);
            
            return login;    

        } else {
            return null;
        }

    }
    
    @Override
    public SessionLogin record(String pass, String cpf, String name, String addr, String phone) throws RemoteException {
        
        SessionLogin login = bankStub.record(this.getClass().getName(), pass, cpf, name, addr, phone);

        if (login != null) {

            User user = new User(login.getAccountId(), pass);
            users.put(login.getAccountId(), user);
            
            user.userLoggedIn();
            sessionStub.openSession(login.getAccountId(), login);
        }

        return login;
    }

    @Override
    public void logout(SessionLogin login) throws RemoteException {

        User user = users.get(login.getAccountId());

        if(user != null && user.isLogged()) {
            user.userLoggedOut();
            sessionStub.closeSession(login.getAccountId());
            return;
        } 

    }

    private void init() {
        try {

            Registry reg = LocateRegistry.getRegistry("localhost", ServicePorts.SESSION_PORT.getValue());
            sessionStub = (SessionService) reg.lookup("Session");
            Registry bankReg = LocateRegistry.getRegistry("localhost", ServicePorts.BANKING_PORT.getValue());
            bankStub = (BankingService) bankReg.lookup("Banking");

            serverPuKey = bankStub.getPuKey();

            users.put(12345L, new User(12345L, "senha123"));
            users.put(67890L, new User(67890L, "senha456"));
            users.put(67890L, new User(24680L, "senha246"));

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

}
