package ghs;

import ghs.messages.Message;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GHS_RMI extends Remote {
    void receive(Message message) throws RemoteException, NotBoundException, MalformedURLException, InterruptedException;
}
