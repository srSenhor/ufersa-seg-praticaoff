package br.edu.ufersa.main;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import br.edu.ufersa.entities.SessionLogin;
import br.edu.ufersa.service.skeletons.AuthService;
import br.edu.ufersa.utils.GUI;
import br.edu.ufersa.utils.ServicePorts;

public class App {

    public App() {
        this.init();
    }

    private void init() {

        Scanner cin = new Scanner(System.in);
        boolean trying = true;

        try {
            
            Registry reg = LocateRegistry.getRegistry("localhost", ServicePorts.AUTH_PORT.getValue());
            AuthService stub = (AuthService) reg.lookup("Auth");

            do {
                GUI.clearScreen();
                GUI.entryScreen();

                int opt = cin.nextInt();
                cin.nextLine();

                SessionLogin login = null;

                switch (opt) {
                    case 1:
                        System.out.print("Account ID: ");
                        Long accID = cin.nextLong();
                        cin.nextLine();
                        
                        System.out.print("Password  : ");
                        String pass = cin.nextLine();
                        
                        login = stub.auth(accID, pass);
                        break;
                    case 2:
                        GUI.registryScreen();

                        System.out.print("CPF     : ");
                        String cpf = cin.nextLine();
                        System.out.print("Name    : ");
                        String name = cin.nextLine();
                        System.out.print("Address : ");
                        String address = cin.nextLine();
                        System.out.print("Phone   : ");
                        String phone = cin.nextLine();
                        System.out.print("Password: ");
                        String password = cin.nextLine();
                        
                        login = stub.record(password, cpf, name, address, phone);
                        break;
                
                    default:
                        break;
                }
    
                if ( login != null ) {
                    System.out.println("Successful logged in!");
                    trying = false;
                    cin.nextLine(); //TODO: substituir isso por algo mais intuitivo
                    
                    mainMenu(login);

                    stub.logout(login);
                    System.out.println("Successful logged out!");
                } else {
                    System.out.println("Failed to login, there is something wrong...");
                    cin.nextLine(); //TODO: substituir isso por algo mais intuitivo
                }
            } while (trying);



        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cin.close();
        }
    }

    private void mainMenu(SessionLogin login){
        new Client(login);
    }
}
