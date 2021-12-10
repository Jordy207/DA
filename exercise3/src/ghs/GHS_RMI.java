package ghs;

import ghs.messages.Message;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public interface GHS_RMI extends Serializable {
    void receive(Message message) throws RemoteException, NotBoundException, MalformedURLException;
}
