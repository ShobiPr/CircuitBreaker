import java.util.Random;
import java.sql.Time;

public class Endpoint {

    int numEndpoints;
    boolean[] serverStatus;
    long[] reponseTime;

    public Endpoint(int numEndpoints){
        this.numEndpoints = numEndpoints;
        this.serverStatus = new boolean[numEndpoints];
        this.reponseTime = new long[numEndpoints];
        setEndpoints();
    }

    public void setEndpoints(){

        for (int i = 0; i < numEndpoints; i++) {
           serverStatus[i] = true;
        }
            /*serverStatus[7] = false;
            serverStatus[8] = false;
            serverStatus[9] = false;  */

         for (int i = 0; i < numEndpoints; i++){
            long time = (long)(Math.random() * 3000);
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
        System.out.println("In SendResponse");
        boolean serverStatus = getServerStatus(msg);
        long responsetime = getResponseTime(msg);
        Response res = new Response(serverStatus,responsetime, msg);
        return res;
    }

    public void fixServer(int port){
         serverStatus[port] = true;
    }

}
