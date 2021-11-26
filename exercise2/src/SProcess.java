import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Objects;

public class SProcess implements Runnable{
    Component process;
    int pid;
    public SProcess(Component process, int pid) {
        this.process = process;
        this.pid = pid;
    }

    public void run() {
        if (this.pid == 1) {
            try {
                Thread.sleep(5000);
                this.process.Request();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (this.pid==2) {
            try {
                this.process.Request();
            } catch (MalformedURLException | RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        }
    }
}
