package ghs.messages;

import ghs.*;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ConnectMessage implements Message{
    public int L;
    public int sender_id;
    int execute_count;

    public ConnectMessage(int L, int sender_id){
        this.L = L;
        this.sender_id = sender_id;
        this.execute_count = 0;
    }

    @Override
    public void execute(GHS instance) throws RemoteException, NotBoundException, MalformedURLException {
        System.out.println("Executing connect on " + instance.toString());
        if(instance.SN.equals("sleeping")) {
            instance.wakeUp();
        }
        Edge j = instance.getReceiveEdge(this.sender_id);
        if(this.L < instance.LN){
            j.setStatus("in_MST");
            InitiateMessage sendMessage = new InitiateMessage(instance.LN, instance.FN, instance.SN, instance.id);
            instance.send(sendMessage, j);
            if(instance.SN.equals("find")){
                instance.find_count++;
            }
        } else {
            if(j.getStatus().equals("?_in_MST")){
                System.out.println("Message added to queue");
                instance.message_queue.add(this);
            } else {
                InitiateMessage sendMessage = new InitiateMessage(instance.LN + 1, j.getWeight(),
                        "find", instance.id);
                instance.send(sendMessage, j);
            }
        }
    }
}
