package ghs.messages;

import ghs.Edge;
import ghs.GHS;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class AcceptMessage implements Message {

    public Edge j;

    public AcceptMessage(Edge j){
        this.j = j;
    }

    @Override
    public void execute(GHS instance) throws RemoteException, NotBoundException, MalformedURLException {
        instance.test_edge = null;
        if(this.j.getWeight() < instance.best_wt){
            instance.best_edge = this.j;
            instance.best_wt = this.j.getWeight();
        }
        instance.report();
    }
}
