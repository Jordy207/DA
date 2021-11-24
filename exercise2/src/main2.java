import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class main2 {

    public static void main(String[] args){
        int numProcesses = 3;
        Thread[] threads = new Thread[numProcesses];
        try{
            Registry registry = LocateRegistry.createRegistry(1099);
            int[] delays = {500, 1000, 1500};

            for (int i = 0; i < numProcesses; i++) {
                Component process = new Component(i, numProcesses, delays[i]);
                SProcess p = new SProcess(process, i);
                threads[i] = new Thread(p);
            }
            for (int i = 0; i < numProcesses; i++) {
                threads[i].start();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}




