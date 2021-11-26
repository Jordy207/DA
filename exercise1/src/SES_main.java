import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class SES_main {
    public SES_main(){}

    public static void main(String[] args){
        int numProcesses = 3;
        try{
            int[][] destinations = {{1,2}, {}, {1}};
            String[][] messages = {{"1", "2"}, {}, {"3"}};
            int[][] delays = {{5000,0}, {}, {500}};
            Registry registry = LocateRegistry.getRegistry("145.94.166.32");
            for (int i = 0; i < numProcesses; i++) {
                SchiperEggliSandozRMI process = (SchiperEggliSandozRMI) registry.lookup("SchiperEggliSandoz-" + i);
                for (int j = 0; j < destinations[i].length; j++) {
                    if(i==2){
                        Thread.sleep(1000);
                    }
                    process.send(messages[i][j], destinations[i][j], delays[i][j]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



