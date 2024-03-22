package br.edu.ufersa.utils;

public class GUI {

    // TODO: Editar as interfaces

    public static void entryScreen() {
        System.out.print("""
             ===================================
            |                                   |
            |       Hey! Welcome to the         |
            |   Amazing Digital Bank  \\(^u^)/   |
            |                                   |
            |    Please, log-in or create an    |
            |        account to continue!       |
            |                                   |
            |   [1] Login on your account       |  
            |   [2] Create an account           |  
            |                                   |
             ===================================
            Option: """);
    }

    public static void registryScreen() {
        System.out.print("""
            Please, input the required info to continue
            """);
    }

    public static void menu() {
        System.out.print("""
             ============ Main Menu ============
            |   | [1] |   Account info          |
            Z   | [2] |   Withdraw              Z
            |   | [3] |   Deposit               |
            Z   | [4] |   Balance               Z
            |   | [5] |   Transfer              |
            Z   | [6] |   Investment            Z
            |   | [7] |   Check Investments     |
            Z   | [8] |   Exit                  Z
             ===================================

            Option: """);
    }

    public static void listOps() {
        System.out.print("""
            Listing by...?
            [1]    -    General
            [2]    -    Category

            Option: """);
    }
    

    public static void clearScreen() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                // Se estiver no Windows, usa o comando "cls" para limpar o console.
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Se estiver em outro sistema operacional (como Linux ou macOS), usa o comando "clear".
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
