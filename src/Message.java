public class Message {

    int port;
    String text;
    int id;

    public Message(int number, String text, int id){
        this.port = number;
        this.text = text;
        this.id = id;
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
}
