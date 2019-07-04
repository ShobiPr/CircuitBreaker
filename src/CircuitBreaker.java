public class CircuitBreaker {

    private int[] errorCounter;
    private long[] lastFailureTime;
    private boolean[] serverStates;
    private long maxResponseTime;
    private long[] responseTime;
    private int[] numberOfCalls;
    private long timeoutTime;

    public CircuitBreaker(int numServer, long maxResponseTime, long timeoutTime, int maxNumberOfCalls){
        this.serverStates = new boolean[numServer];
        this.errorCounter = new int[numServer];
        this.maxResponseTime = maxResponseTime;
        this.numberOfCalls = new int[maxNumberOfCalls];
        this.timeoutTime = timeoutTime;
        this.lastFailureTime = new long[numServer];
        this.responseTime = new long[numServer];
    }

   public boolean handleMessage(Message msg) {

       boolean call = true;
       int id = msg.getId();
       long responseTime = System.currentTimeMillis() - lastFailureTime[msg.getPort()]; // time since last failure

       //Not first call to endport by this message
       if (numberOfCalls[id] != 0){
           if (!serverStates[msg.getPort()]){
               if (responseTime >= timeoutTime){
                   //Timeout done - try to call endport
                   numberOfCalls[id]++;
                   call = true;

               } else {
                   //Timeout - set message to queue
                   call = false;
               }
           }
           return call;
       }

       //First call by this message
       //If timeout - don't call
       if(lastFailureTime[msg.getPort()] != 0){
           call = false;
       } else {
           numberOfCalls[id]++; }
       return call;
   }

    public Message handleResponse(Response rsp) {

        int port = rsp.getPort();
        serverStates[port] = rsp.getStatus();
        responseTime[port] = rsp.getResponseTime();

        //Response time too long or server broken
        if (responseTime[port] >= maxResponseTime || !serverStates[port]){

            if (serverStates[port]) {
                serverStates[port] = false;
            }
            //start timeout
            lastFailureTime[port] = System.currentTimeMillis();
            //log the error
            errorCounter[port]++;

        } else {
            //Message successfully delivered to endpoint
            rsp.getMessage().setFinished();
            lastFailureTime[port] = 0;
        }
        return rsp.getMessage();
    }

    public int[] getErrorLog() { return this.errorCounter; }

    public int getNumberOfCalls(){
        int calls = 0;
        for(int i = 0; i < numberOfCalls.length; i++){
            calls += numberOfCalls[i];
        }
        return calls;
    }
}

