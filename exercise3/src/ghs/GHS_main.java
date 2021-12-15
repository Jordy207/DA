package ghs;

import java.io.BufferedReader;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
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
    public static void main(String[] args) throws RemoteException {

        String network_filename = "graphs/graph_2_nodes.txt";
        HashMap<Integer, List<Edge>> network = new HashMap<>();
        int num_nodes = 0;
        int num_edges = 0;
        try {
            FileReader networkFile = new FileReader(network_filename);
            BufferedReader networkReader = new BufferedReader(networkFile);

            String line = networkReader.readLine();
            String[] data = line.split(" ");

            num_nodes = Integer.parseInt(data[0]);
            num_edges = Integer.parseInt(data[1]);

            for (int i = 0; i < num_nodes; i++) {
                network.put(i, new ArrayList<>());
            }

            int src, dst, w;
            while ((line = networkReader.readLine()) != null) {
                data = line.split(" ");
                src = Integer.parseInt(data[0]);
                dst = Integer.parseInt(data[1]);
                w = Integer.parseInt(data[2]);
                Edge e = new Edge(dst, w);
                network.get(src).add(e);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        GHS wakeup = null;
        String ip_jordy = "localhost";
        HashMap<Integer, String> ip_map = new HashMap<>(Map.of(0, ip_jordy,
                1, ip_jordy, 2, ip_jordy, 3, ip_jordy, 4, ip_jordy));
        Registry registry = LocateRegistry.getRegistry();
        List<Integer> myNodes = new ArrayList<>(List.of(0,1,2,3,4));
        for(int node : myNodes){
            GHS client = new GHS(node, network.get(node), ip_map);
            registry.rebind("ghs-" + node, client);
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
