package ghs.messages;

import ghs.Edge;
import ghs.GHS;
import ghs.GHS_RMI;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ReportMessage implements Message{

    public int w;
    public int sender_id;

    public ReportMessage(int w, int sender_id){
        this.w = w;
        this.sender_id = sender_id;
    }

    @Override
    public void execute(GHS instance) throws RemoteException, NotBoundException, MalformedURLException {
        System.out.println("Executing report on " + instance.toString() + " w given is " + this.w);
        Edge j = instance.getReceiveEdge(this.sender_id);
        if(!j.equals(instance.in_branch)) {
//            System.out.println(j.toString() + " does not equal in-branch:" + instance.in_branch.toString());
            instance.find_count--;
            if(this.w < instance.best_wt){
                instance.best_wt = this.w;
                instance.best_edge = j;
            }
            instance.report();
        } else {
            if(instance.SN.equals("find")){
                instance.message_queue.add(this);
            } else {
                if(this.w > instance.best_wt){
                    instance.changeRoot();
                } else if(this.w == instance.best_wt && instance.best_wt == Integer.MAX_VALUE) {
                    // HALT
                    System.out.println("HALT");
                    for(int dest : instance.ip_dest.keySet()){
                        String destination = "//" + instance.ip_dest.get(dest) + "/ghs-" + dest;
                        GHS_RMI GHS_dest = (GHS_RMI) Naming.lookup(destination);
                        GHS_dest.print_info();
                    }
                }
            }
        }
    }
}
