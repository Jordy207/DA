import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Singhal_main {

    public static void main(String[] args){
        int numProcesses = 3;
        try{
            Registry registry = LocateRegistry.getRegistry(1099);

            for (int i = 0; i < numProcesses; i++) {
                Component_RMI process = (Component_RMI) registry.lookup("Component-" + i);
                if (i == 1) {
                    try {
                        Thread.sleep(5000);
                        process.Request();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (i==2) {
                    try {
                        process.Request();
                    } catch (MalformedURLException | RemoteException | NotBoundException e) {
                        e.printStackTrace();
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}




