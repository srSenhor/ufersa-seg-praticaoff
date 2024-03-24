package br.edu.ufersa.main;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import br.edu.ufersa.entities.Message;
import br.edu.ufersa.entities.Request;
import br.edu.ufersa.entities.SessionLogin;
import br.edu.ufersa.security.BankCipher;
import br.edu.ufersa.service.skeletons.BankingService;
import br.edu.ufersa.utils.GUI;
import br.edu.ufersa.utils.ServicePorts;

public class Client {
    
    private SessionLogin login;
    private Scanner cin;
    private BankingService bankingStub;
    
    public Client(SessionLogin login) {
        this.login = login;
        this.cin = new Scanner(System.in);
        this.exec();
    }
    
    private void exec(){

        try {
            Registry bankingReg = LocateRegistry.getRegistry("localhost", ServicePorts.BANKING_PORT.getValue());
            this.bankingStub = (BankingService) bankingReg.lookup("Banking");

        } catch (Exception e) {
            e.printStackTrace();
        }

        int op = 0;

        do {
            GUI.clearScreen();
            GUI.menu();

            op = cin.nextInt();
            cin.nextLine();
            float amount;

            switch (op) {
                case 1:
                    System.out.println("========= ACCOUNT INFO =========\n");
                    send(op, login.getAccountId(), -1, -1, -1);
                    System.out.println("================================");
                    
                    cin.nextLine();
                    break;
                case 2:
                    System.out.print("Amount to be withdrawed: R$ ");
                    amount = cin.nextFloat();
                    cin.nextLine();
                    
                    System.out.println("========= WITHDRAW =========\n");
                    send(op, login.getAccountId(), -1, -1, amount);
                    System.out.println("============================\n");
                    
                    cin.nextLine();
                    break;
                case 3:
                    System.out.print("Amount to be deposited: R$ ");
                    amount = cin.nextFloat();
                    cin.nextLine();
        
                    System.out.println("========= DEPOSIT =========\n");
                    send(op, login.getAccountId(), -1, -1, amount);
                    System.out.println("===========================\n");
                    
                    cin.nextLine();
                    break;
                case 4:
                    System.out.println("========= BALANCE =========\n");
                    send(op, login.getAccountId(), -1, -1, -1);
                    System.out.println("===========================");
                    
                    cin.nextLine();
                    break;
                case 5:
                    System.out.print("Receiver Account Id: ");

                    int recAccID = cin.nextInt();
                    cin.nextLine();
        
                    System.out.print("How much do you want to transfer: R$ ");
                    amount = cin.nextFloat();
                    cin.nextLine();

                    System.out.println("\n============= TRANSFER =============\n");
                    send(op, login.getAccountId(), recAccID, -1, amount);
                    System.out.println("====================================");
                    
                    cin.nextLine();
                    break;
                case 6:
                    GUI.investmentOps();
                    int investType = cin.nextInt();
                    cin.nextLine();
        
                    System.out.print("How much do you want to invest: R$ ");
                    amount = cin.nextFloat();
                    cin.nextLine();
        
                    System.out.println("\n============= INVEST =============\n");
                    send(op, login.getAccountId(), -1, investType, amount);
                    System.out.println("===================================\n");
                    
                    cin.nextLine();
                    break;
                case 7:
                    System.out.println("\n========= CHECK INVESTMENTS =========\n");
                    send(op, login.getAccountId(), -1, -1, -1);
                    System.out.println("=====================================");

                    cin.nextLine();
                    break;
                case 8:
                    System.out.println("bye my friend!");
                    break;
                default:
                    System.err.println("undefined operation");
                    break;
            }
        } while(op != 8);

        cin.close();
    }

    private void send(int opType, long senderID, long receiverID, int investType, float amount) {
        String request = new Request(opType, senderID, receiverID, investType, amount).toString();
        
        try {
            
            BankCipher bc = new BankCipher(login.getAesKey());
            request = bc.enc(request);

            String hash = bc.genHash(request);           
            hash = login.getSessionRSA().sign(hash);

            Message response = bankingStub.receive(senderID, new Message(request, hash));

            if (response == null) {
                System.err.println("cannot do this, please try again...'");
            } else {
                String responseTestHash = login.getSessionRSA().checkSign(response.getHash(), login.getServerPuKey());
    
                if(!bc.genHash(response.getContent()).equals(responseTestHash)) {
                    System.err.println("an error has ocurred, please try again");
                } else {
                    System.err.println(bc.dec(response.getContent()));
                }
                
            }


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
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
