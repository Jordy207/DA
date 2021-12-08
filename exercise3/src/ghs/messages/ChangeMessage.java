package ghs.messages;

import ghs.Edge;
import ghs.GHS;

public class ChangeMessage implements Message{

    Edge j;

    public ChangeMessage(Edge j){
        this.j = j;
    }

    @Override
    public void execute(GHS instance) {
        instance.changeRoot();
    }
}
