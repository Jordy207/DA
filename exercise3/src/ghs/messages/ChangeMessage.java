package ghs.messages;

import ghs.Edge;
import ghs.GHS;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ChangeMessage implements Message{

    Edge j;

    public ChangeMessage(Edge j){
        this.j = j;
    }

    @Override
    public void execute(GHS instance) throws RemoteException, NotBoundException, MalformedURLException {
        instance.changeRoot();
    }
}
