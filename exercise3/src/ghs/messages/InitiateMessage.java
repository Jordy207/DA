package ghs.messages;

import ghs.Edge;
import ghs.GHS;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class InitiateMessage implements Message{
    int L;
    int F;
    String S;
    int sender_id;

    public InitiateMessage(int L, int F, String S, int sender_id){
        this.L = L;
        this.F = F;
        this.S = S;
        this.sender_id = sender_id;
    }

    @Override
    public void execute(GHS instance) throws RemoteException, NotBoundException, MalformedURLException {
        System.out.println("Executing initiate on " + instance.toString());
        Edge j= instance.getReceiveEdge(this.sender_id);
        instance.LN = this.L;
        instance.FN = this.F;
        instance.SN = this.S;
        instance.in_branch = j;
        instance.best_edge = null;
        instance.best_wt = Integer.MAX_VALUE;
        for(Edge edge : instance.edges){
            if(!edge.equals(j) && edge.getStatus().equals("in_MST")){
                InitiateMessage sendMessage = new InitiateMessage(this.L, this.F, this.S, instance.id);
                instance.send(sendMessage, edge);
                if(this.S.equals("find")) {
                    instance.find_count++;
                }
            }
        }
        if(S.equals("find")){
            instance.test();
        }
    }
}
