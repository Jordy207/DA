package ghs;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

public class GHS_main {
    public static void main(String[] args) throws RemoteException {

        String network_filename = "exercise3/graphs/graph_5_nodes.txt";
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
        }    }
}
