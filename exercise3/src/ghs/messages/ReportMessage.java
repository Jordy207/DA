package ghs.messages;

import ghs.Edge;
import ghs.GHS;

public class ReportMessage implements Message{

    public int w;
    public Edge j;

    public ReportMessage(int w, Edge j){
        this.w = w;
        this.j = j;
    }

    @Override
    public void execute(GHS instance) {
        if(!this.j.equals(instance.in_branch)) {
            instance.find_count--;
            if(this.w < instance.best_wt){
                instance.best_wt = this.w;
                instance.best_edge = this.j;
            }
        } else {
            if(instance.SN.equals("find")){
                instance.message_queue.add(this);
            } else {
                if(this.w > instance.best_wt){
                    instance.changeRoot();
                } else if(this.w == instance.best_wt && instance.best_wt == Integer.MAX_VALUE) {
                    // HALT
                    System.out.println("HALT");
                }
            }
        }
    }
}
