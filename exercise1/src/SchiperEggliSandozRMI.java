import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SchiperEggliSandozRMI extends Remote {
    public void receive(Message m) throws RemoteException;
}
