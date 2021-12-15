package ghs.messages;

import ghs.Edge;
import ghs.GHS;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class AcceptMessage implements Message {

    public int sender_id;

    public AcceptMessage(int sender_id){
        this.sender_id = sender_id;
    }

    @Override
    public void execute(GHS instance) throws RemoteException, NotBoundException, MalformedURLException {
        System.out.println("Executing accept on " + instance.toString());
        Edge j = instance.getReceiveEdge(this.sender_id);
        instance.test_edge = null;
        if(j.getWeight() < instance.best_wt){
            instance.best_edge = j;
            instance.best_wt = j.getWeight();
        }
        instance.report();
    }
}
