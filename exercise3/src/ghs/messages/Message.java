package ghs.messages;
import ghs.GHS;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public interface Message extends Serializable {
    void execute(GHS instance) throws RemoteException, NotBoundException, MalformedURLException;
}
