package ghs.messages;

import ghs.Edge;
import ghs.GHS;

public class TestMessage implements Message {

    public int L;
    public int F;
    public Edge j;

    public TestMessage(int L, int F, Edge j){
        this.L = L;
        this.F = F;
        this.j = j;
    }
    @Override
    public void execute(GHS instance) {
        if(instance.SN.equals("sleeping")) instance.wakeUp();
        if(this.L > instance.LN){
            instance.message_queue.add(this);
        } else {
            if(this.F != instance.FN){
                AcceptMessage sendMessage = new AcceptMessage(this.j);
                instance.send(sendMessage, this.j);
            } else {
                if(this.j.getStatus().equals("?_in_MST")) this.j.setStatus("not_in_MST");
                if(!instance.test_edge.equals(this.j)){
                    RejectMessage sendMessage = new RejectMessage(this.j);
                    instance.send(sendMessage, this.j);
                } else {
                    instance.test();
                }
            }
        }
    }
}
