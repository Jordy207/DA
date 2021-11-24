import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Component_RMI extends Remote {

    public void receiveRequest(int id, int requestNumber) throws RemoteException, MalformedURLException, NotBoundException;
    public void receiveToken(String[] TS, int[] TN) throws RemoteException, MalformedURLException, NotBoundException;
}