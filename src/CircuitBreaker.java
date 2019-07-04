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
        this.calls = new int[100000];
        this.timeoutTime = timeoutTime;
        this.lastFailureTime = new long[numServer];
        this.responseTime = new long[numServer];
    }

    // true - good, false - broken
    // 0 - call port, 1 = set in queue
   public boolean handleMessage(Message msg) {
       boolean res = true;
       int id = msg.getId();
       long responseTime = System.currentTimeMillis() - lastFailureTime[msg.getPort()]; // time since last failure

       if (calls[id] != 0){
           //Message has been here before
           //System.out.println("In handleMessage " + msg.getPort());
           if (!states[msg.getPort()]){
               if (responseTime >= timeoutTime){
                   calls[id]++;
                   System.out.println("In handleMessage: Timeout mode done - try to reconnect: " + id);
                   res = true;

               } else {
                   System.out.println("In handleMessage: Timeout - cannot connect: " + id);
                   System.out.println("responseTime: " + responseTime + ",   timeout threshold: "+ timeoutTime);
                   res = false;
               }
           }
           return res;
       }

       //First time a message visits
       if(lastFailureTime[msg.getPort()] != 0){
           res = false;
       }else{
           calls[id]++;
       }
       return res;
   }

    public Message handleResponse(Response rsp) {

        int port = rsp.getPort();
        states[port] = rsp.getStatus();
        responseTime[port] = rsp.getResponseTime();


        if (responseTime[port] >= maxResponseTime || !states[port]){
                //Response time too long or server broken
                if (states[port]) {
                    states[port] = false;
                }
                lastFailureTime[port] = System.currentTimeMillis(); //start timeout
                errorCounter[port]++; //log the error
        } else {
            //Message succesfully delivered to endpoint.
            rsp.getMessage().setFinished();
            lastFailureTime[port] = 0;
        }

        return rsp.getMessage();
    }


    public int[] getLog() {return this.errorCounter;}

    public int getCalls(){
        int res = 0;
        for(int i = 0; i < calls.length; i++){
            res += calls[i];
        }
        return res;
    }
}

