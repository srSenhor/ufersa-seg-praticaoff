package br.edu.ufersa.service;

import java.rmi.RemoteException;
import java.util.HashMap;

import javax.crypto.SecretKey;
// import javax.crypto.spec.IvParameterSpec;

import br.edu.ufersa.entities.SessionLogin;
import br.edu.ufersa.service.skeletons.SessionService;
import br.edu.ufersa.utils.RSAKey;

public class SessionServiceImpl implements SessionService {

    private static HashMap<Long, RSAKey> session_pukeys;
    private static HashMap<Long, SecretKey> session_aes_keys;

    public SessionServiceImpl() {
        session_pukeys = new HashMap<>();
        session_aes_keys = new HashMap<>();
    }
    
    @Override
    public RSAKey getRSAKey(long accID) throws RemoteException {
        return session_pukeys.get(accID);
    }

    @Override
    public SecretKey getAESKey(long accID) throws RemoteException {
        return session_aes_keys.get(accID);
    }

    @Override
    public void openSession(long accID, SessionLogin login) throws RemoteException {
        session_pukeys.put(accID, login.getSessionRSA().getPublicKey());
        session_aes_keys.put(accID, login.getAesKey());
    }

    @Override
    public void closeSession(long accID) throws RemoteException {
        session_pukeys.remove(accID);
        session_aes_keys.remove(accID);
    }

}
