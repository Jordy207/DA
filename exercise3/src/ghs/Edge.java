package ghs;

import java.util.Objects;

public class Edge {

    String SE;
    int weight;
    int connectedNode;

    public Edge(int connectedNode, int weight){
        this.weight = weight;
        this.SE = "?_in_MST";
        this.connectedNode = connectedNode;
    }

    public String getStatus(){
        return this.SE;
    }

    public void setStatus(String SE){
        this.SE = SE;
    }

    public int getWeight(){
        return this.weight;
    }

    public int getConnectedNode() {
        return this.connectedNode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return weight == edge.weight &&
                connectedNode == edge.connectedNode;
    }

    public String toString() {
        return this.connectedNode + " " + this.weight;
    }
}
