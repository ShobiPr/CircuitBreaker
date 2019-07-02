public class CircuitBreaker {

    int[] errorCounter;
    long[] failureTime;
    boolean[] states;
    long timeout = 2000*10000;


    public CircuitBreaker(int numServer){
        this.state = new boolean[numServer];
        this.errorCounter = new int[numServer];
        this.lastFailureTime = new long[numServer];
    }

    // true - good, false - broken
    // 1 = new check, 2 - set in queue, 0 - call port
   public int getPort(Message msg) {
       int res = 0;
       if (!states[port]) {
           if (System.nanoTime() - failureTime[port] > timeout) {
               res = 1; // call port again
           } else {
               res = 2; // set in queue
           }
       }
       return res;
   }

    public Message handleRespond(int port, int status, Message msg) {
        switch (status) {
            case -1: //timeout


                break;

            case 0: // OK
                return null;

            case 1: //fail
                errorCounter[port]++;
                failureTime[port] = System.nanoTime();
                states[port] = false;
                //log
                //send msg to queue (main)
                break;

            default:
                System.out.println("Unexpected behaviour");
        }

        return msg;

    }

}

