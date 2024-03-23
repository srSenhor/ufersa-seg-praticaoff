package br.edu.ufersa.service.skeletons;

import java.rmi.Remote;
import java.rmi.RemoteException;

import br.edu.ufersa.entities.Message;
import br.edu.ufersa.entities.SessionLogin;
import br.edu.ufersa.utils.RSAKey;

public interface BankingService extends Remote {

    Message receive(long accID, Message message) throws RemoteException;
    SessionLogin record(String className, String pass, String cpf, String name, String addr, String phone) throws RemoteException;
    RSAKey getPuKey() throws RemoteException;

}
