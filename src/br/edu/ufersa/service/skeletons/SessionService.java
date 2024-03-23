package br.edu.ufersa.service.skeletons;

import java.rmi.Remote;
import java.rmi.RemoteException;

import javax.crypto.SecretKey;

import br.edu.ufersa.entities.SessionLogin;
import br.edu.ufersa.utils.RSAKey;

public interface SessionService extends Remote {

    RSAKey getRSAKey(long accID) throws RemoteException;
    SecretKey getAESKey(long accID) throws RemoteException;
    void openSession(long accID, SessionLogin login) throws RemoteException;
    void closeSession(long accID) throws RemoteException;
    

}
