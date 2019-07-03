public class CircuitBreaker {

    int[] errorCounter;
    long[] lastFailureTime;
    boolean[] states;
    long maxResponseTime;
    long[] responseTime;
    int[] calls;
    long timeoutTime;

    public CircuitBreaker(int numServer, long maxResponseTime, long timeoutTime){
        this.states = new boolean[numServer];
        this.errorCounter = new int[numServer];
        this.maxResponseTime = maxResponseTime;
        this.calls = new int[100];
        this.timeoutTime = timeoutTime;
    }

    // true - good, false - broken
    // 0 - call port, 1 = new check, 2 - set in queue
   public int callEndpoint(Message msg) {
       int res = 0;
       int id = msg.getId();

       if (calls[id] != 0){
           if (!states[msg.getPort()]){
               if (System.currentTimeMillis() - lastFailureTime[msg.getPort()] > timeoutTime){
                   calls[id]++;
                   res = 1;
                   lastFailureTime[msg.getPort()] = 0;
               } else {
                   res = 2;
               }
           }
       }

       calls[id]++;
       return res;
   }

   //response class {fail, message, }

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

    public void collectRespond(Response rsp) {

        int id = rsp.msg.getId();
        int port = rsp.msg.getPort();

        states[port] = rsp.getStatus();
        responseTime[port] = rsp.getResponseTime();

        if (responseTime[port] >= maxResponseTime) {
            if (states[port] != false) {
                states[port] = false;
                lastFailureTime[port] = System.currentTimeMillis();
            }

        }
    }
}

