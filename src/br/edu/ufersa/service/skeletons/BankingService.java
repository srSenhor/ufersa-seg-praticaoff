package br.edu.ufersa.service.skeletons;

import java.rmi.Remote;
import java.rmi.RemoteException;

import br.edu.ufersa.entities.Message;

public interface BankingService extends Remote {

    Message receive(long accID, Message message) throws RemoteException;

}
