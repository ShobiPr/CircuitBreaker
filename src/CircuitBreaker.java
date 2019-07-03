public class CircuitBreaker {

    int[] errorCounter;
    private long[] lastFailureTime;
    private boolean[] states;
    private long maxResponseTime;
    private long[] responseTime;
    private int[] calls;
    private long timeoutTime;

    public CircuitBreaker(int numServer, long maxResponseTime, long timeoutTime){
        this.states = new boolean[numServer];
        this.errorCounter = new int[numServer];
        this.maxResponseTime = maxResponseTime;
        this.calls = new int[100];
        this.timeoutTime = timeoutTime;
        this.lastFailureTime = new long[numServer];
        this.responseTime = new long[numServer];
    }

    // true - good, false - broken
    // 0 - call port, 1 = set in queue
   public boolean handleMessage(Message msg) {
       boolean res = true;
       int id = msg.getId();

       if (calls[id] != 0){
           //System.out.println("In handleMessage " + msg.getPort());
           if (!states[msg.getPort()]){
               if (System.currentTimeMillis() - lastFailureTime[msg.getPort()] > timeoutTime){
                   calls[id]++;
                   //System.out.println("In handleMessage: Timeout mode done - try to reconnect: " + id);
                   res = true;
               } else {
                   //System.out.println("In handleMessage: Timeout - cannot connect: " + id);
                   res = false;
               }
           }
       }

       calls[id]++;
       return res;
   }

    public Message handleResponse(Response rsp) {

        int port = rsp.getPort();
        states[port] = rsp.getStatus();
        responseTime[port] = rsp.getResponseTime();

        //endpoint exceeds maxResponseTime
        if (responseTime[port] >= maxResponseTime){
            if (states[port]) {
                states[port] = false;
                lastFailureTime[port] = System.currentTimeMillis(); //start timeout
                errorCounter[port]++; //log the error
                return rsp.msg; //add message to queue
            }
        } else if (!states[port]){
            lastFailureTime[port] = System.currentTimeMillis(); //start timeout
            errorCounter[port]++; //log the error
            return rsp.msg; //add message to queue
        }

        return null;
    }
}

