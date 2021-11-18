public class SESProcess implements Runnable{
    int[] destinations;
    String[] messages;
    SchiperEggliSandoz process;
    int[] delays;
    public SESProcess(int[] destinations, String[] messages, SchiperEggliSandoz process, int[] delays) {
        this.destinations = destinations;
        this.messages = messages;
        this.process = process;
        this.delays = delays;
    }

    public void run() {
        for (int i = 0; i < destinations.length; i++) {
            try {
//                Thread.sleep(1000);
                process.send(messages[i], destinations[i], delays[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
