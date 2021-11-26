import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class Component extends UnicastRemoteObject implements Component_RMI {

    int pid;
    int processes;
    int delay;
    String[] S;
    String[] TS;
    int[] TN;
    int[] N;

    public Component(int pid, int processes, int delay) throws RemoteException {
        super();
        this.pid = pid;
        this.processes = processes;
        this.delay = delay;
        this.S = new String[processes];
        this.TS = null;
        this.TN = null;
        this.N = new int[processes];

        if (pid==0) {
            S[0] = "H";
            this.TS = new String[processes];
            TS[0] = "O";
            this.TN = new int[processes];
            for (int i = 1; i<processes; i++) {
                S[i] = "O";
                TS[i] = "O";
            }
        } else {
            for (int j = 0; j<pid; j++) {
                S[j] = "R";
            }
            for (int j = pid; j<processes; j++) {
                S[j] = "O";
            }
        }
        System.out.println("Process "+this.pid+" started. S is " + Arrays.toString(this.S)+
                ", TS is "+Arrays.toString(this.TS));
    }

    @Override
    public void receiveRequest(int id, int requestNumber) throws RemoteException, MalformedURLException, NotBoundException {
        N[id] = requestNumber;
        switch(this.S[this.pid])
        {
            case "E":
            case "O":
                this.S[id] = "R";
                break;
            case "R":
                if (!Objects.equals(S[id], "R")) {
                    this.S[id] = "R";
                    send(id, new String[] {}, new int[]{}, "request");
                }
                break;
            case "H":
                this.S[id] = "R";
                this.S[this.pid] = "O";
                this.TS[id] = "R";
                this.TN[id] = requestNumber;
                send(id, this.TS, this.TN, "token");
                this.TS = null;
                this.TN = null;
                break;
        }
        System.out.println("Process "+this.pid+" received request from "+id+". S is " + Arrays.toString(this.S)+
                ", TS is "+Arrays.toString(this.TS));
    }

    @Override
    public void receiveToken(String[] TS, int[] TN) throws RemoteException, MalformedURLException, NotBoundException {
        this.TS = Arrays.copyOf(TS, TS.length);
        this.TN = Arrays.copyOf(TN, TN.length);
        this.S[this.pid] = "E";
        System.out.println("Starting CS for process " + this.pid);
        criticalSection();

        this.S[this.pid] = "O";
        this.TS[this.pid] = "O";

        for (int j = 0; j<this.processes; j++) {
            if (this.N[j] > this.TN[j]) {
                this.TN[j] = this.N[j];
                this.TS[j] = this.S[j];
            }
            else {
                this.N[j] = this.TN[j];
                this.S[j] = this.TS[j];
            }
        }
        boolean completed = false;
        for (int j = 0; j<this.processes && Objects.equals(this.S[j], "O"); j++) {
            if (j == this.processes - 1) {
                this.S[this.pid] = "H";
                completed = true;
                break;
            }
        }
        int id=0;
        if (!completed) {
            System.out.println("Not completed.");
            for (int j = 0; j < this.processes; j++) {
                if (Objects.equals(this.S[j], "R") && j != this.pid) {
                    System.out.println("sending token to " + j);
                    id = j;
                    send(j, this.TS, this.TN, "token");
                    this.TS = null;
                    this.TN = null;
                    System.out.println("Process "+this.pid+" received token and sent it to "+id+". S is " + Arrays.toString(this.S)+
                            ", TS is "+Arrays.toString(this.TS));
                    break;
                }
            }
        }
        System.out.println("Process "+this.pid+" received token and no more requests known. S is " + Arrays.toString(this.S)+
                ", TS is "+Arrays.toString(this.TS));
    }

    public void Request() throws MalformedURLException, NotBoundException, RemoteException {
        S[this.pid] = "R";
        N[this.pid]++;
        for (int j = 0; j<this.processes; j++) {
            if (j != this.pid && Objects.equals(S[j], "R")) {
                send(j, new String[] {}, new int[] {}, "request");
            }
        }
        System.out.println("Process "+this.pid+" sent request. S is " + Arrays.toString(this.S)+
                ", TS is "+Arrays.toString(this.TS));
    }

    public void send(int destination, String[] TS, int[] TN, String type) throws MalformedURLException, NotBoundException, RemoteException {
        Component_RMI dest = (Component_RMI) Naming.lookup("rmi://145.94.166.32/Component-" + destination);
        int sender = this.pid;
        int wait;
        if(this.delay < 0){
            wait = (int) (Math.random()*10000);
        } else {
            wait = this.delay;
        }
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run(){
                        try{
                            switch (type) {
                                case "token" -> dest.receiveToken(TS, TN);
                                case "request" -> dest.receiveRequest(sender, N[sender]);
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, wait
        );
    }

    private void criticalSection() {
        int wait = (int) (Math.random()*5000);
        try {
            Thread.sleep(wait);
        } catch (InterruptedException e) {
            System.out.println("Critical section interrupted");
        }
    }

    public static void main(String[] args){
        if(System.getSecurityManager() == null){
            System.setSecurityManager(new SecurityManager());
        }
        int[] delays = {500, 1000, 1500};
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            for (int i = 0; i < 3; i++) {
                Component obj = new Component(i, 3, delays[i]);
                registry.bind("Component-" + i, obj);
                System.out.println("Process Component-" + i + " ready");
            }
        } catch(Exception e){
            System.err.println("Exception " + e.toString());
            e.printStackTrace();
        }
    }
}
