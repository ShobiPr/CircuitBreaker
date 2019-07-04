public class Response {

    boolean status;
    long responseTime;
    Message msg;

    public Response(boolean status, long responseTime, Message msg){
        this.status = status;
        this.responseTime = responseTime;
        this.msg = msg;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public boolean getStatus(){
        return status;
    }

    public int getPort(){
        return msg.getPort();
    }

    public Message getMessage(){return this.msg;}
}
