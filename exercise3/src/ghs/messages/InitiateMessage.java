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
    Edge j;

    public InitiateMessage(int L, int F, String S, Edge j){
        this.L = L;
        this.F = F;
        this.S = S;
        this.j = j;
    }

    @Override
    public void execute(GHS instance) throws RemoteException, NotBoundException, MalformedURLException {
        instance.LN = this.L;
        instance.FN = this.F;
        instance.SN = this.S;
        instance.in_branch = this.j;
        instance.best_edge = null;
        instance.best_wt = Integer.MAX_VALUE;
        for(Edge edge : instance.edges){
            if(!edge.equals(j) && edge.getStatus().equals("in_MST")){
                InitiateMessage sendMessage = new InitiateMessage(this.L, this.F, this.S, edge);
                instance.send(sendMessage, edge);
                if(S.equals("find")) instance.find_count++;
            }
        }
        if(S.equals("find")) instance.test();
    }
}
