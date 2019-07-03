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

    public Message handleResponse(Response rsp) {

        int port = rsp.msg.getPort();
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
        }
        return null;
    }
}

