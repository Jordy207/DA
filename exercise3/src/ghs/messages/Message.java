package ghs.messages;
import ghs.GHS;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public interface Message {
    void execute(GHS instance) throws RemoteException, NotBoundException, MalformedURLException;
}
