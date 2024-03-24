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
import br.edu.ufersa.entities.Message;
import br.edu.ufersa.entities.Request;
import br.edu.ufersa.entities.SessionLogin;
import br.edu.ufersa.security.BankCipher;
import br.edu.ufersa.security.RSAImpl;
import br.edu.ufersa.service.skeletons.BankingService;
import br.edu.ufersa.service.skeletons.SessionService;
import br.edu.ufersa.utils.RSAKey;
import br.edu.ufersa.utils.ServicePorts;

public class BankingServiceImpl implements BankingService {

    private static HashMap<Long, Account> accounts;
    private static SessionService sessionStub;
    private static RSAImpl rsa;
    
    public BankingServiceImpl() {
        accounts = new HashMap<>();
        rsa = new RSAImpl();
        this.init();
    }
    
    @Override
    public Message receive(long accID, Message message) throws RemoteException {

        Account acc = accounts.get(accID);

        if (acc == null) {
            return null;
        }

        if (message == null || message.getContent() == null || message.getHash() == null) {
            return null;
        }

        RSAKey clientPuKey = sessionStub.getRSAKey(accID);
        String receivedHash = rsa.checkSign(message.getHash(), clientPuKey);
        BankCipher bc = null;
        String content = "";

        try {
            bc = new BankCipher(sessionStub.getAESKey(accID));
            
            if(!bc.genHash(message.getContent()).equals(receivedHash)) {
                System.err.println("failed to receive this request, please try again");
                System.err.println("(stop try, weirdo)");
                return null;
            }
            
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

        switch (req.getOpType()) {
            case 1:
                return this.showInfo(req.getSenderID(), bc);
            case 2:
                return this.withdraw(req.getSenderID(), req.getAmount(), bc);
            case 3:
                return this.deposit(req.getSenderID(), req.getAmount(), bc);
            case 4:
                return this.balance(req.getSenderID(), bc);
            case 5:
                return this.transfer(req.getSenderID(), req.getReceiverID(), req.getAmount(), bc);
            case 6:
                return this.invest(req.getSenderID(), req.getAmount(), req.getInvestType(), bc);
            case 7:
                return this.checkInvestments(req.getSenderID(), bc);
            default:
                return null;
        }
    }

    private Message showInfo(long senderID, BankCipher bc){
        Account acc = accounts.get(senderID);

        Message response = new Message("", "");

        if (acc == null) {
            response.setContent("Não foi possível encontrar as informações da sua conta");
        } else {
            response.setContent(acc.toString());
        }

        prepare(response, bc);

        return response;
    }

    private Message withdraw(long senderID, float amount, BankCipher bc){
        Account acc = accounts.get(senderID);

        Message response = new Message("", "");

        if (acc == null) {
            response.setContent("cannot find your account...");
        } else {
            float balance = acc.getBalance();
            StringBuilder info = new StringBuilder();

            if (amount > 0.0f && balance > 0.0f && balance >= amount) {
                acc.setBalance(acc.getBalance() - amount);

                info.append(amount + " withdrawed! Your actual balance: ");
                balance = acc.getBalance();
                info.append("R$ " + balance);

                response.setContent(info.toString());
            } else {
                response.setContent("cannot withdraw this amount...");
            }
        }

        prepare(response, bc);

        return response;    
    }
    
    private Message deposit(long senderID, float amount, BankCipher bc){
        Account acc = accounts.get(senderID);

        Message response = new Message("", "");

        if (acc == null) {
            response.setContent("cannot find your account...");
        } else {
            float balance = acc.getBalance();
            StringBuilder info = new StringBuilder();

            if (amount > 0.0f) {
                acc.setBalance(acc.getBalance() + amount);

                info.append(amount + " deposited! Your actual balance: ");
                balance = acc.getBalance();
                info.append("R$ " + balance);

                response.setContent(info.toString());
            } else {
                response.setContent("cannot deposit this amount...");
            }
        }

        prepare(response, bc);

        return response;
    }

    private Message balance(long senderID, BankCipher bc){
        Account acc = accounts.get(senderID);

        Message response = new Message("", "");

        if (acc == null) {
            response.setContent("Não foi possível encontrar as informações da sua conta");
        } else {
            response.setContent("\tR$ " + acc.getBalance() + "\n");
        }

        prepare(response, bc);

        return response;
    }

    private Message transfer(long senderID, long receiverID, float amount, BankCipher bc){
        Account senderAcc = accounts.get(senderID);
        Account receiverAcc = accounts.get(receiverID);

        Message response = new Message("", "");

        if (senderAcc == null) {
            response.setContent("cannot find your account...");
        } else if (receiverAcc == null) {
            response.setContent("cannot find the receiver account...");
        } else {
            float balance = senderAcc.getBalance();
            StringBuilder info = new StringBuilder();

            if (amount > 0.0f && balance > 0.0f && balance >= amount) {
                senderAcc.setBalance(balance - amount);
                receiverAcc.setBalance(receiverAcc.getBalance() + amount);

                info.append(amount + " transfered! Your actual balance: ");
                balance = senderAcc.getBalance();
                info.append("R$ " + balance);
                
                response.setContent(info.toString());

            } else {
                response.setContent("cannot transfer this amount...");
            }
        }

        prepare(response, bc);

        // System.out.println("Response: " + response.getContent());
        // System.out.println("Hash: " + response.getHash());

        return response;
    
    }

    private Message invest(long senderID,  float amount, int investType, BankCipher bc){
        Account senderAcc = accounts.get(senderID);

        Message response = new Message("", "");

        if (senderAcc == null) {
            response.setContent("cannot find your account...");
        } else {
            float balance = senderAcc.getBalance();
            StringBuilder info = new StringBuilder();
  
            if (amount > 0.0f && balance > 0.0f && balance >= amount) {
                senderAcc.setBalance(balance - amount);
    
                switch (investType) {
                    case 1:
                        senderAcc.setSavingsInvestment(senderAcc.getSavingsInvestment() + amount);
                        info.append(amount + " invested in savings! Your actual balance: ");
                        break;
                    case 2:
                        senderAcc.setFixedIncomeInvestment(senderAcc.getFixedIncomeInvestment() + amount);
                        info.append(amount + " invested in fixed income! Your actual balance: ");
                        break;
                }
                
                balance = senderAcc.getBalance();
                info.append("R$ " + balance + "\n");
                info.append("Please, check your investments on [7] Check Investments\n");
                
                response.setContent(info.toString());
            
            } else {
                response.setContent("cannot invest this amount...");
            }
        }

        prepare(response, bc);

        return response;
    }

    private Message checkInvestments(long senderID, BankCipher bc){
        Account acc = accounts.get(senderID);

        Message response = new Message("", "");

        if (acc == null) {
            response.setContent("Não foi possível encontrar as informações da sua conta");
        } else {
            StringBuilder info = new StringBuilder("Here, a projection for the next months\n\n");

            float savingsInvestment = acc.getSavingsInvestment();
            float fixedIncomeInvestment = acc.getFixedIncomeInvestment();

            if (savingsInvestment > 0.0f) {
                info.append("""
                                    Savings (default)
                            
                        Amount invested: R$ """ + savingsInvestment + "\n\n");
                info.append("""
                        3 months         6 months        12 months
                        """);

                for (int i = 1; i <= 12; i++) {
                    savingsInvestment = (savingsInvestment * 1.005f);
                    if (i % 3 == 0 && i != 9) {
                        info.append("R$ " + savingsInvestment + "\t");
                    }
                }

                info.append("\n\n\n");
            }

            if (fixedIncomeInvestment > 0.0f) {
                info.append("""
                                        Fixed Income
                            
                        Amount invested: R$ """ + fixedIncomeInvestment + "\n\n");
                info.append("""
                        3 months         6 months        12 months
                        """);

                for (int i = 1; i <= 12; i++) {
                    fixedIncomeInvestment = (fixedIncomeInvestment * 1.015f);
                    if (i % 3 == 0 && i != 9) {
                        info.append("R$ " + fixedIncomeInvestment + "\t");
                    }
                }

                info.append("\n\n");
            }


            response.setContent(info.toString());
        }

        prepare(response, bc);

        return response;
    }

    private void prepare(Message message, BankCipher bc){
        try {

            message.setContent(bc.enc(message.getContent()));
            message.setHash(bc.genHash(message.getContent()));
            message.setHash(rsa.sign(message.getHash()));

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
    
    
    private void record(long accID, String pass, String cpf, String name, String addr, String phone, float balance) throws RemoteException {
        
        Account acc = new Account(accID, pass, cpf, name, addr, phone, balance);
        accounts.put(acc.getAccountID(), acc);

    }

    private void init() {
        try {

            Registry reg = LocateRegistry.getRegistry("localhost", ServicePorts.SESSION_PORT.getValue());
            sessionStub = (SessionService) reg.lookup("Session");

            this.record(12345L, "senha123", "12345678910", "Seu Toinho", "Rua da lagoa", "988994325", 1500.0f);
            this.record(67890L, "senha456", "10987654321", "Seu Jão", "Rua da lagoa", "988994234", 500.0f);
            this.record(24680L, "senha246", "24681012140", "Seu Resmungão", "Rua da lagoa", "988994236", 2000.0f);


        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

    }
}