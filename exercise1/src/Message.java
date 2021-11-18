import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

public class Message implements Serializable {
    String message;
    Map<Integer, int[]> buffer;
    int[] vectorClock;

    public Message(String message, Map<Integer, int[]> buffer, int[] vectorClock) {
        this.message = message;
        this.buffer = buffer;
        this.vectorClock = vectorClock;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setBuffer(Map<Integer, int[]> buffer) {
        this.buffer = buffer;
    }

    public void setVectorClock(int[] vectorClock) {
        this.vectorClock = vectorClock;
    }

    public Map<Integer, int[]> getBuffer() {
        return buffer;
    }

    public int[] getVectorClock() {
        return vectorClock;
    }

    @Override
    public String toString() {
        String print = "Message{" +
                "message='" + message + '\'' +
                ", buffer=";
        for(var entry : this.buffer.entrySet()){
            print += "{" + entry.getKey() + "=" + Arrays.toString(entry.getValue()) + "}";
        }
        print +=  ", vectorClock=" + Arrays.toString(vectorClock) +
                '}';
        return print;
    }
}
