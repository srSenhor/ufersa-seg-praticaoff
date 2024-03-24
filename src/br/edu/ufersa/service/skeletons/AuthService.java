package br.edu.ufersa.service.skeletons;

import java.rmi.Remote;
import java.rmi.RemoteException;

import br.edu.ufersa.entities.SessionLogin;

public interface AuthService extends Remote {

    SessionLogin auth(long accID, String password) throws RemoteException;
    SessionLogin record(String pass, String cpf, String name, String addr, String phone) throws RemoteException;
    void logout(SessionLogin login) throws RemoteException;

}
