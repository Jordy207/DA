package ghs.messages;

import ghs.Edge;
import ghs.GHS;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RejectMessage implements Message{

    public Edge j;

    public RejectMessage(Edge j){
        this.j = j;
    }

    @Override
    public void execute(GHS instance) throws RemoteException, NotBoundException, MalformedURLException {
        if(this.j.getStatus().equals("?_in_MST")) this.j.setStatus("not_in_MST");
        instance.test();
    }
}
