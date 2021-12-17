package ghs;

import java.io.BufferedReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class GHS_main {
    public static void main(String[] args) throws RemoteException, MalformedURLException {

        String network_filename = "exercise3/graphs/fully_4_nodes.txt";
        HashMap<Integer, List<Edge>> network = new HashMap<>();
        int num_nodes = 0;
        try {
            FileReader networkFile = new FileReader(network_filename);
            BufferedReader networkReader = new BufferedReader(networkFile);

            String line = networkReader.readLine();
            String[] data = line.split(" ");

            num_nodes = Integer.parseInt(data[0]);

            for (int i = 0; i < num_nodes; i++) {
                network.put(i, new ArrayList<>());
            }

            int src, dst, w;
            while ((line = networkReader.readLine()) != null) {
                data = line.split(" ");
                src = Integer.parseInt(data[0]);
                dst = Integer.parseInt(data[1]);
                w = Integer.parseInt(data[2]);
                Edge e1 = new Edge(dst, w);
                Edge e2 = new Edge(src, w);
                network.get(src).add(e1);
                network.get(dst).add(e2);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        GHS wakeup = null;
        String ip_jordy = "145.94.167.94";
        String ip_mariette = "145.94.228.51";
        HashMap<Integer, String> ip_map = new HashMap<>();
        List<Integer> myNodes = new ArrayList<>();
//        for(int i = 0; i < num_nodes; i++){
//            ip_map.put(i, ip_jordy);
//            myNodes.add(i);
//        }
        System.setProperty("java.security.policy","exercise3/mypolicy.policy");
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        Registry registry = LocateRegistry.getRegistry();
        ip_map.put(0, ip_jordy);
        ip_map.put(1, ip_jordy);
        ip_map.put(2, ip_mariette);
        ip_map.put(3, ip_mariette);
        myNodes.add(2);
        myNodes.add(3);
        for(int node : myNodes){
            GHS client = new GHS(node, network.get(node), ip_map);
            String dest = "//" + ip_map.get(node) + "/ghs-" + node;
            Naming.rebind(dest, client);
            System.out.println("ghs-" + node + " ready");
            if(node == 0){
                wakeup = client;
            }
        }

        try {
            if(wakeup != null && wakeup.SN.equals("sleeping")) {
                wakeup.wakeUp();
            }
        } catch (NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
