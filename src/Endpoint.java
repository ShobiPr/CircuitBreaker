import java.util.Random;
import java.sql.Time;

public class Endpoint {

    int numEndpoints;
    boolean[] serverStatus;
    long[] reponseTime;
    int maxResponseTime;

    public Endpoint(int numEndpoints, int maxResponseTime){
        this.numEndpoints = numEndpoints;
        this.serverStatus = new boolean[numEndpoints];
        this.reponseTime = new long[numEndpoints];
        this.maxResponseTime = maxResponseTime;
        setEndpoints();
    }

    public void setEndpoints(){

        for (int i = 0; i < numEndpoints; i++) {
           serverStatus[i] = true;
        }
        serverStatus[3] = false;
        serverStatus[4] = false;

         for (int i = 0; i < numEndpoints; i++){
             long time = 680;
            reponseTime[i] = time;
        }
    }

    public boolean getServerStatus(Message msg){
        return serverStatus[msg.getPort()];
    }

    public long getResponseTime(Message msg){
        return reponseTime[msg.getPort()];
    }

     public Response sendResponse(Message msg){
        //System.out.println("In SendResponse, msg id: " + msg.getId() + ", server for port: " + msg.getPort() +", is: " + serverStatus[msg.getPort()]);
        long random = (long)(Math.random() * 100);
        long time;
        if(random > 99 ) {
            time = 2800;
        }else{
            //680
            time = getResponseTime(msg);
        }

        Response res = new Response(serverStatus[msg.getPort()],time, msg);
        return res;
    }

    public void fixServer(int port){
         serverStatus[port] = true;
    }
    public void fixAll(){
        for(int i = 0;  i < numEndpoints; i++){
            serverStatus[i] = true;
        }
    }

}
