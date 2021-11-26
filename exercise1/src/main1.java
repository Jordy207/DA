import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class main1 {

    public static void main(String[] args){
        int numProcesses = 3;
        Thread[] threads = new Thread[numProcesses];
        try{
            Registry registry = LocateRegistry.createRegistry(1099);
            int[][] destinations = {{1,2}, {}, {1}};
            String[][] messages = {{"1", "2"}, {}, {"3"}};
            int[][] delays = {{5000,0}, {}, {500}};

            for (int i = 0; i < numProcesses; i++) {
                SchiperEggliSandoz process = new SchiperEggliSandoz(i, numProcesses);
                SESProcess p = new SESProcess(destinations[i], messages[i], process, delays[i]);
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



