package ghs.messages;

import ghs.Edge;
import ghs.GHS;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RejectMessage implements Message{

    public int sender_id;

    public RejectMessage(int sender_id){
        this.sender_id = sender_id;
    }

    @Override
    public void execute(GHS instance) throws RemoteException, NotBoundException, MalformedURLException {
        System.out.println("Executing reject on " + instance.toString());
        Edge j = instance.getReceiveEdge(this.sender_id);
        if(j.getStatus().equals("?_in_MST")){
            j.setStatus("not_in_MST");
        }
        instance.test();
    }
}
