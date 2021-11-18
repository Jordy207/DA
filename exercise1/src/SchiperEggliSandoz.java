import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class SchiperEggliSandoz extends UnicastRemoteObject implements SchiperEggliSandozRMI {
    List<Message> messageBuffer;
    Map<Integer, int[]> buffer;
    int[] vectorClock;
    int pid;

    public SchiperEggliSandoz(int pid, int processes) throws RemoteException {
        super();
        this.messageBuffer = new ArrayList<>();
        this.buffer = new HashMap<Integer, int[]>();
        this.vectorClock = new int[processes];
        this.pid = pid;

        try {
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("SchiperEggliSandoz-" + pid, this);
            System.out.println("Process " + pid + " ready");
        } catch(Exception e){
            System.err.println("Exception " + e.toString());
            e.printStackTrace();
        }
    }

    public synchronized void send(String m, int destination, int delay) throws RemoteException, NotBoundException, MalformedURLException {
        SchiperEggliSandozRMI dest = (SchiperEggliSandozRMI) Naming.lookup("SchiperEggliSandoz-" + destination);
        HashMap<Integer, int[]> bufferCopy = new HashMap<>();
        for(var entry: this.buffer.entrySet()){
            bufferCopy.put(entry.getKey(), entry.getValue());
        }

        this.vectorClock[this.pid]++;
        int[] vcCopy = Arrays.copyOf(this.vectorClock, this.vectorClock.length);
        Message message = new Message(m, bufferCopy, vcCopy);

        System.out.println("Message " + message.toString() + " send from " + this.pid + " to " + destination);
        int wait;
        if(delay < 0){
            wait = (int) (Math.random()*10000);
        } else {
            wait = delay;
        }
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run(){
                        try{
                            dest.receive(message);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, wait
        );
        this.buffer.put(destination, this.vectorClock);
        String print ="Buffer after sending message ";
        for(var entry : this.buffer.entrySet()){
            print += "{" + entry.getKey() + "=" + Arrays.toString(entry.getValue()) + "}";
        }
        System.out.println(print);
    }

    public synchronized void receive(Message message){
        if(Buffer.deliverCondition(message.getBuffer(), this.pid, this.vectorClock)){
            deliver(message);
            System.out.println("Message '" + message.toString() + "' delivered to " + this.pid);
            String print = "Buffer after receiving message for " + this.pid + ": ";
            for(var entry : this.buffer.entrySet()){
                print += "{" + entry.getKey() + "=" + Arrays.toString(entry.getValue()) + "}";
            }
            System.out.println(print);
            checkBuffer();
        } else {
            messageBuffer.add(message);
        }
    }

    public synchronized void deliver(Message message){
        this.buffer = Buffer.merge(message.getBuffer(),this.buffer);
        this.vectorClock = VectorClock.max(this.vectorClock, message.getVectorClock());
        this.vectorClock[this.pid]++;
    }

    public synchronized void checkBuffer(){
        for (int i = 0; i < messageBuffer.size(); i++) {
            if(Buffer.deliverCondition(messageBuffer.get(i).getBuffer(), this.pid, this.vectorClock)){
                deliver(messageBuffer.remove(i));
                checkBuffer();
            }
        }
    }
}
