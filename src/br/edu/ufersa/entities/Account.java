package br.edu.ufersa.entities;

public class Account {

    private long accountID;
    private String password;
    private String cpf;
    private String clientName;
    private String address;
    private String phoneNumber;
    private float balance;
    private float savingsInvestment;
    private float fixedIncomeInvestment;

    public Account(long accountID, String password, String cpf, String clientName, String address, String phoneNumber) {
        this.setAccountID(accountID);
        this.setPassword(password);
        this.setCpf(cpf);
        this.setClientName(clientName);
        this.setAddress(address);
        this.setPhoneNumber(phoneNumber);
        this.setBalance(0.0f);
        this.setSavingsInvestment(0.0f);
        this.setFixedIncomeInvestment(0.0f);
    }

    public Account(long accountID, String password, String cpf, String clientName, String address, String phoneNumber, float balance) {
        this.setAccountID(accountID);
        this.setPassword(password);
        this.setCpf(cpf);
        this.setClientName(clientName);
        this.setAddress(address);
        this.setPhoneNumber(phoneNumber);
        this.setBalance(balance);
        this.setSavingsInvestment(0.0f);
        this.setFixedIncomeInvestment(0.0f);
    }

    public long getAccountID() {
        return accountID;
    }
    public String getPassword() {
        return password;
    }
    public float getBalance() {
        return balance;
    }
    public float getSavingsInvestment() {
        return savingsInvestment;
    }
    public float getFixedIncomeInvestment() {
        return fixedIncomeInvestment;
    }

    private void setAccountID(long accountID) {
        this.accountID = accountID;
    }
    private void setPassword(String password) {
        this.password = password;
    }
    private void setCpf(String cpf) {
        this.cpf = cpf;
    }
    private void setClientName(String clientName) {
        this.clientName = clientName;
    }
    private void setAddress(String address) {
        this.address = address;
    }
    private void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    //TODO: tratar possíveis incoerências de valores negativos
    public void setBalance(float balance) {
        this.balance = balance;
    }
    public void setSavingsInvestment(float savingsInvestment) {
        this.savingsInvestment = savingsInvestment;
    }
    public void setFixedIncomeInvestment(float fixedIncomeInvestment) {
        this.fixedIncomeInvestment = fixedIncomeInvestment;
    }

    @Override
    public String toString() {
        // return "Account [cpf=" + cpf + ", clientName=" + clientName + ", address=" + address + ", phoneNumber="
        //         + phoneNumber + "]";
        return  "Account ID : " + accountID + "\n" +
                "CPF        : " + cpf + "\n" + 
                "Name       : " + clientName + "\n" + 
                "Address    : " + address + "\n" + 
                "Phone      : " + phoneNumber + "\n";
    }
}
