package ghs;

import ghs.messages.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GHS extends UnicastRemoteObject implements GHS_RMI{

    public int FN;
    public int LN;
    public String SN;
    public int id;
    public List<Edge> edges;
    public List<Message> message_queue;
    public int find_count;
    public Edge in_branch;
    public Edge best_edge;
    public int best_wt;
    public Edge test_edge;
    public HashMap<Integer, String> ip_dest;


    public GHS(int id, List<Edge> edges, HashMap<Integer, String> ip_dest) throws RemoteException {
        super();
        this.id = id;
        this.edges = edges;
        this.ip_dest = ip_dest;
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

    public void wakeUp() throws RemoteException, NotBoundException, MalformedURLException {
        System.out.println("ghs-" + this.id + " ready");
        Edge j = getMOE(this.edges);
        j.setStatus("in_MST");
        this.LN = 0;
        this.SN = "found";
        ConnectMessage message = new ConnectMessage(0, j);
        send(message, j);
    }

    public void test() throws RemoteException, NotBoundException, MalformedURLException {
        boolean edgeFound = false;
        for(Edge edge : this.edges){
            if(edge.getStatus().equals("?_in_MST")){
                edgeFound = true;
                break;
            }
        }
        if(edgeFound){
            test_edge = this.getMOE(this.edges);
            TestMessage sendMessage = new TestMessage(this.LN, this.FN, test_edge);
            this.send(sendMessage, test_edge);
        }
    }

    public void report() throws RemoteException, NotBoundException, MalformedURLException {
        if(this.find_count == 0 && test_edge == null){
            this.SN = "found";
            ReportMessage sendMessage = new ReportMessage(this.best_wt, this.in_branch);
            this.send(sendMessage, this.in_branch);
        }
    }

    public void changeRoot() throws RemoteException, NotBoundException, MalformedURLException {
        if(this.best_edge.getStatus().equals("in_MST")){
            ChangeMessage sendMessage = new ChangeMessage(this.best_edge);
            this.send(sendMessage, this.best_edge);
        } else {
            ConnectMessage sendMessage = new ConnectMessage(this.LN, this.best_edge);
            this.send(sendMessage, this.best_edge);
            this.best_edge.setStatus("in_MST");
        }
    }

    public synchronized void receive(Message message) throws RemoteException, NotBoundException, MalformedURLException {
        message.execute(this);
        checkQueue();
    }

    public void send(Message message, Edge j){
        String dest = "//" + ip_dest.get(j.getConnectedNode()) + "/ghs-" + j.getConnectedNode();
        try {
            GHS_RMI GHS_dest = (GHS_RMI) Naming.lookup(dest);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        Thread.sleep((int)(Math.random()*5000));
                        GHS_dest.receive(message);
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void checkQueue() throws RemoteException, NotBoundException, MalformedURLException {
        for(Message m : message_queue){
            m.execute(this);
        }
    }
}
