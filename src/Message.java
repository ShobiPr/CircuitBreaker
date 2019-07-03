public class Message {

    int number;
    String text;

    public Message(int number, String text){
        this.number = number;
        this.text = text;
    }

    public int getNumber() {
        return number;
    }

    public  String getText() {
        return text;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setText(String text) {
        this.text = text;
    }
}
