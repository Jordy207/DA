package ghs;

import ghs.messages.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class GHS extends UnicastRemoteObject implements GHS_RMI{

    public int FN;
    public int LN;
    public String SN;
    public int id;
    public List<Edge> edges;
    public Queue<Message> message_queue;
    public int find_count;
    public Edge in_branch;
    public Edge best_edge;
    public int best_wt;
    public Edge test_edge;
    public HashMap<Integer, String> ip_dest;
    public int acceptCount;
    public int changeCount;
    public int connectCount;
    public int initiateCount;
    public int rejectCount;
    public int reportCount;
    public int testCount;


    public GHS(int id, List<Edge> edges, HashMap<Integer, String> ip_dest) throws RemoteException {
        super();
        this.id = id;
        this.edges = edges;
        this.ip_dest = ip_dest;
        this.message_queue = new LinkedList<>();
        this.LN = 0;
        this.SN = "sleeping";
        this.find_count = 0;
        this.best_wt = Integer.MAX_VALUE;
        this.test_edge = null;
        this.acceptCount = 0;
        this.changeCount = 0;
        this.connectCount = 0;
        this.initiateCount = 0;
        this.rejectCount = 0;
        this.reportCount = 0;
        this.testCount = 0;
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
        System.out.println("ghs-" + this.id + " woke up");
        Edge j = getMOE(this.edges);
        j.setStatus("in_MST");
        this.LN = 0;
        this.SN = "found";
        ConnectMessage message = new ConnectMessage(0, this.id);
        send(message, j);
    }

    public void test() throws RemoteException, NotBoundException, MalformedURLException {
        System.out.println("ghs-" + this.id + " running test");
        boolean edgeFound = false;
        for(Edge edge : this.edges){
            if(edge.getStatus().equals("?_in_MST")){
                edgeFound = true;
                break;
            }
        }
        if(edgeFound){
            this.test_edge = this.getMOE(this.edges);
            TestMessage sendMessage = new TestMessage(this.LN, this.FN, this.id);
            this.send(sendMessage, this.test_edge);
        } else {
            this.test_edge = null;
            this.report();
        }
    }

    public void report() throws RemoteException, NotBoundException, MalformedURLException {
        System.out.println("ghs-" + this.id + " running report");
        if(this.find_count == 0 && test_edge == null){
            this.SN = "found";
            ReportMessage sendMessage = new ReportMessage(this.best_wt, this.id);
            this.send(sendMessage, this.in_branch);
        }
    }

    public void changeRoot() throws RemoteException, NotBoundException, MalformedURLException {
        System.out.println("ghs-" + this.id + " running change");
        if(this.best_edge.getStatus().equals("in_MST")){
            ChangeMessage sendMessage = new ChangeMessage();
            this.send(sendMessage, this.best_edge);
        } else {
            ConnectMessage sendMessage = new ConnectMessage(this.LN, this.id);
            this.send(sendMessage, this.best_edge);
            this.best_edge.setStatus("in_MST");
        }
    }

    public synchronized void receive(Message message) throws RemoteException, NotBoundException, MalformedURLException, InterruptedException {
        String messageType = message.getClass().getSimpleName();
        System.out.println("Receiving " + messageType + " on " + this.toString());
        switch (messageType) {
            case "AcceptMessage" -> this.acceptCount++;
            case "ChangeMessage" -> this.changeCount++;
            case "ConnectMessage" -> this.connectCount++;
            case "InitiateMessage" -> this.initiateCount++;
            case "RejectMessage" -> this.rejectCount++;
            case "ReportMessage" -> this.reportCount++;
            case "TestMessage" -> this.testCount++;
        }
        message.execute(this);
        if(!this.message_queue.isEmpty()) {
            checkQueue();
        }
        System.out.println("executed");

    }

    public void send(Message message, Edge j){
        int dest_id = j.getConnectedNode();
        System.out.printf("Sending %s from %s to %d\n",
                message.getClass().getSimpleName(), this.toString(), dest_id);
        String dest = "//" + ip_dest.get(dest_id) + "/ghs-" + dest_id;
//        int wait = (int)(Math.random()*200);
        int wait = 200 + (int)(Math.random()*100);
        try {
            GHS_RMI GHS_dest = (GHS_RMI) Naming.lookup(dest);
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run(){
                            try{
                                GHS_dest.receive(message);
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }, wait
            );
        } catch (Exception e){
            e.printStackTrace();
        }
        try{
            Thread.sleep(2000);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public synchronized void checkQueue() throws RemoteException, NotBoundException, MalformedURLException {
        System.err.println("checking queue  for " + this.toString());
        int message_queue_size = this.message_queue.size();
        for (int i = 0; i < message_queue_size; i++) {
            if(this.message_queue.size() != 0){
                Message m = this.message_queue.remove();
                m.execute(this);
            }
        }
        System.err.println("queue checked " + this.toString());
    }

    public Edge getReceiveEdge(int id){
        for(Edge e : edges){
            if(e.getConnectedNode() == id){
                return e;
            }
        }
        return null;
    }

    public String toString(){
        String in_branch_s = "null";
        String test_edge_s = "null";
        String best_edge_s = "null";
        if(in_branch != null){
            in_branch_s = this.in_branch.toString();
        }
        if(test_edge != null){
            test_edge_s = this.test_edge.toString();
        }
        if(best_edge != null){
            best_edge_s = this.best_edge.toString();
        }
        return String.format(
                "(id: %d, level: %d, name: %d, status: %s, in-branch: %s, best-edge: %s, best-wt: %d, test-edge: %s, findcount: %d)",
                this.id, this.LN, this.FN, this.SN, in_branch_s, best_edge_s, this.best_wt, test_edge_s, this.find_count);
    }


    public void print_info(){
        System.out.println("Information for node " + this.id);
        for(Edge e : this.edges){
            if(e.getStatus().equals("in_MST")) {
                System.out.printf("Edge from ghs-%d %s\n", this.id, e.toString());
            }
        }
        System.out.println("Amount of accept messages: " + this.acceptCount);
        System.out.println("Amount of change messages: " + this.changeCount);
        System.out.println("Amount of connect messages: " + this.connectCount);
        System.out.println("Amount of initiate messages: " + this.initiateCount);
        System.out.println("Amount of reject messages: " + this.rejectCount);
        System.out.println("Amount of report messages: " + this.reportCount);
        System.out.println("Amount of test messages: " + this.testCount);
    }
}
