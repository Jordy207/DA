package ghs.messages;

import ghs.Edge;
import ghs.GHS;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class TestMessage implements Message {
    public int L;
    public int F;
    public int sender_id;

    public TestMessage(int L, int F, int sender_id) {
        this.L = L;
        this.F = F;
        this.sender_id = sender_id;
    }

    @Override
    public void execute(GHS instance) throws RemoteException, NotBoundException, MalformedURLException {
        System.out.println("Executing test on " + instance.toString());
        if(instance.SN.equals("sleeping")){
            instance.wakeUp();
        }
        Edge j = instance.getReceiveEdge(this.sender_id);
        if(this.L > instance.LN){
            instance.message_queue.add(this);
        } else {
            if(this.F != instance.FN){
                AcceptMessage sendMessage = new AcceptMessage(instance.id);
                instance.send(sendMessage, j);
            } else {
                if(j.getStatus().equals("?_in_MST")){
                    j.setStatus("not_in_MST");
                }
                if(instance.test_edge != null){
                    if(!instance.test_edge.equals(j)) {
                        RejectMessage sendMessage = new RejectMessage(instance.id);
                        instance.send(sendMessage, j);
                    } else {
                        instance.test();
                    }
                } else {
                    instance.test();
                }
            }
        }
    }
}
