import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Buffer{


    public static boolean deliverCondition(Map<Integer, int[]> buffer, int pid, int[] vectorClockProcess){
        if(buffer.containsKey(pid)){
            if(VectorClock.lessThenEqualTo(buffer.get(pid),vectorClockProcess)){
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public static Map<Integer, int[]> merge(Map<Integer, int[]> messageBuffer, Map<Integer, int[]> processBuffer){
        HashMap<Integer, int[]> pBufferCopy = new HashMap<>();
        for(var entry: processBuffer.entrySet()){
            pBufferCopy.put(entry.getKey(), entry.getValue());
        }
        for(var entry : messageBuffer.entrySet()){
            if(pBufferCopy.containsKey(entry.getKey())){
                int[] newVectorClock = VectorClock.max(messageBuffer.get(entry.getKey()), pBufferCopy.get(entry.getKey()));
                pBufferCopy.put(entry.getKey(), newVectorClock);
            } else {
                pBufferCopy.put(entry.getKey(), messageBuffer.get(entry.getKey()));
            }
        }
        return pBufferCopy;
    }
}
