import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SchiperEggliSandozRMI extends Remote {
    public void receive(Message m) throws RemoteException;
    public void send(String message, int destination, int delay) throws RemoteException, NotBoundException, MalformedURLException;
}
