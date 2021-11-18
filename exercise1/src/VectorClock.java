public class VectorClock{

    public static int[] max(int[] vc1, int[] vc2){
        int[] newClock = new int[vc1.length];
        for (int i = 0; i < vc1.length; i++) {
            newClock[i] = Math.max(vc1[i], vc2[i]);
        }
        return newClock;
    }

    public static boolean lessThenEqualTo(int[] vc1, int[] vc2){
        for (int i = 0; i < vc1.length; i++) {
            if(vc2[i] > vc1[i]){
                return false;
            }
        }
        return true;
    }
}
