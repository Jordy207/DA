package ghs.messages;

import ghs.Edge;
import ghs.GHS;

public class AcceptMessage implements Message {

    public Edge j;

    public AcceptMessage(Edge j){
        this.j = j;
    }

    @Override
    public void execute(GHS instance) {
        instance.test_edge = null;
        if(this.j.getWeight() < instance.best_wt){
            instance.best_edge = this.j;
            instance.best_wt = this.j.getWeight();
        }
        instance.report();
    }
}
