package ghs.messages;

import ghs.*;

public class ConnectMessage implements Message{
    public int L;
    public Edge j;

    public ConnectMessage(int L, Edge j){
        this.L = L;
        this.j = j;
    }

    @Override
    public void execute(GHS instance) {
        if(instance.SN.equals("sleeping")) instance.wakeUp();
        if(this.L < instance.LN){
            this.j.setStatus("in_MST");
            InitiateMessage sendMessage = new InitiateMessage(instance.LN, instance.FN, instance.SN, this.j);
            instance.send(sendMessage, this.j);
            if(instance.SN.equals("find")) instance.find_count++;
        } else {
            if(this.j.getStatus().equals("?_in_MST")){
                instance.message_queue.add(this);
            } else {
                InitiateMessage sendMessage = new InitiateMessage(instance.LN + 1, this.j.getWeight(),
                        "find", this.j);
                instance.send(sendMessage, this.j);
            }
        }
    }
}
