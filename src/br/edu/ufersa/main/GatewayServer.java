package br.edu.ufersa.main;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import br.edu.ufersa.service.AuthServiceImpl;
import br.edu.ufersa.service.BankingServiceImpl;
import br.edu.ufersa.service.SessionServiceImpl;
import br.edu.ufersa.service.skeletons.AuthService;
import br.edu.ufersa.service.skeletons.BankingService;
import br.edu.ufersa.service.skeletons.SessionService;
import br.edu.ufersa.utils.ServicePorts;

public class GatewayServer {

    public static void main(String[] args) {

        try {
            
            SessionServiceImpl sessionObjRef = new SessionServiceImpl();
            SessionService sessionSkeleton = (SessionService) UnicastRemoteObject.exportObject(sessionObjRef, 0);
            
            LocateRegistry.createRegistry( ServicePorts.SESSION_PORT.getValue() );
            Registry sessionReg = LocateRegistry.getRegistry( ServicePorts.SESSION_PORT.getValue() );
            sessionReg.bind("Session", sessionSkeleton);

            AuthServiceImpl authObjRef = new AuthServiceImpl();
            AuthService authSkeleton = (AuthService) UnicastRemoteObject.exportObject(authObjRef, 0);

            LocateRegistry.createRegistry( ServicePorts.AUTH_PORT.getValue() );
            Registry authReg = LocateRegistry.getRegistry( ServicePorts.AUTH_PORT.getValue() );
            authReg.bind("Auth", authSkeleton);

            BankingServiceImpl bankObjRef = new BankingServiceImpl();
            BankingService bankSkeleton = (BankingService) UnicastRemoteObject.exportObject(bankObjRef, 0);

            LocateRegistry.createRegistry( ServicePorts.BANKING_PORT.getValue() );
            Registry bankReg = LocateRegistry.getRegistry( ServicePorts.BANKING_PORT.getValue() );
            bankReg.bind("Banking", bankSkeleton);
            
            System.out.println("Server is running now: ");

        } catch (Exception e) {
            System.err.println("An error has ocurred in server: " + e.getMessage());
            e.printStackTrace();
        }

    }

}
