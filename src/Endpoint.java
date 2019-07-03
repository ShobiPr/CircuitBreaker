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
    }

    public void setEndpoints(){

        for (int i = 0; i < numEndpoints; i++){

            //set serverStatus for each server
            int random = (int)(Math.random() * 50);
            if (random > 25){
                serverStatus[i] = true;
            } else {
                serverStatus[i] = false;
            }

            //set responsTime for each server
            long time = (long)(Math.random() * 3000);
            reponseTime[i] = time;
            System.out.println(reponseTime[i]);

        }
    }

    public boolean getServerStatus(Message msg){
        return serverStatus[msg.getPort()];
    }

    public long getResponseTime(Message msg){
        return reponseTime[msg.getPort()];
    }

     public Response sendResponse(Message msg){
        Response res = new Response(getServerStatus(msg),getResponseTime(msg), msg);
        return res;
    }

}
