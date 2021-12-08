package ghs.messages;

import ghs.Edge;
import ghs.GHS;

public class RejectMessage implements Message{

    public Edge j;

    public RejectMessage(Edge j){
        this.j = j;
    }

    @Override
    public void execute(GHS instance) {
        if(this.j.getStatus().equals("?_in_MST")) this.j.setStatus("not_in_MST");
        instance.test();
    }
}
