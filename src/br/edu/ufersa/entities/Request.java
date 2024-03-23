package br.edu.ufersa.entities;
public class Request {

    private int opType;
    private long senderID;
    private long receiverID;
    private int investType;
    private float amount;

    public Request(int opType, long senderID, long receiverID, int investType, float amount) {
        this.opType = opType;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.investType = investType;
        this.amount = amount;
    }

    public int getOpType() {
        return opType;
    }

    public long getSenderID() {
        return senderID;
    }

    public long getReceiverID() {
        return receiverID;
    }

    public float getAmount() {
        return amount;
    }
    
    public int getInvestType() {
        return investType;
    }
    
    @Override
    public String toString() {
        return opType + "/" + senderID + "/" + receiverID + "/" + investType + "/" + amount;
    }

    public static Request fromString(String text) {
        String fields[] = text.split("/");
        int opType = Integer.parseInt(fields[0]);
        int senderID = Integer.parseInt(fields[1]);
        int receiverID = Integer.parseInt(fields[2]);
        int investType = Integer.parseInt(fields[3]);
        float amount = Float.parseFloat(fields[4]);

        return new Request(opType, senderID, receiverID, investType, amount);
    }
}

