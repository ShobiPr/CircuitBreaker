public class Message {

    int port;
    String text;
    int id;
    boolean finished;

    public Message(int id,  String text, int number){
        this.port = number;
        this.text = text;
        this.id = id;
        this.finished = false;
    }

    public int getPort() {
        return port;
    }

    public  String getText() {
        return text;
    }

    public int getId() {
        return id;
    }

    public void setPort(int number) {
        this.port = number;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setFinished(){this.finished = true;}
    public boolean isFinished(){return this.finished;}
}
