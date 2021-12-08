import messages.Message;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class GHS extends UnicastRemoteObject implements GHS_RMI{

    int FN;
    int LN;
    String SN;
    int id;
    List<Edge> edges;
    List<Message> message_queue;
    int find_count;
    Edge in_branch;
    Edge best_edge;
    int best_wt;
    Edge test_edge;

    public GHS(int id, List<Edge> edges) throws RemoteException {
        super();
        this.id = id;
        this.edges = edges;
        this.message_queue = new ArrayList<>();
        this.LN = 0;
        this.SN = "sleeping";
        this.find_count = 0;
        this.best_wt = Integer.MAX_VALUE;
    }

    public Edge getMOE(List<Edge> edges){
        int best_weight = Integer.MAX_VALUE;
        Edge resEdge = null;
        for (Edge edge: edges) {
            if(edge.getStatus().equals("?_in_MST") && edge.getWeight() < best_weight){
                best_weight = edge.getWeight();
                resEdge = edge;
            }
        }
        return resEdge;
    }

    public void wakeUp(){
        Edge j = getMOE(this.edges);
        j.setStatus("in_MST");
        this.LN = 0;
        this.SN = "found";
        //sendConnect(0, j)
    }

    public void receiveConnect(ConnectMessage message){
        if(this.SN.equals("sleeping")) wakeUp();
        if(message.L < this.LN){
            message.j.setStatus("in_MST");
            //sendInitiate(this.LN, this.FN, this.SN, j)
            if(this.SN.equals("find")) find_count++;
        } else {
            if(message.j.getStatus().equals("?_in_MST")){
                this.message_queue.add(message);
            }
        }
    }
}
