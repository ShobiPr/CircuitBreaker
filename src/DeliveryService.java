public class DeliveryService {

    int id;
    Message[] msgQueue;
    int operators;
    int msgCounter;

    public DeliveryService(int id, int operators){
        this.id = id;
        this.operators = operators;
        this.msgQueue = new Message[1000];
        msgCounter = 0;
    }

    public void addMessageToQueue(Message msg){
        msgQueue[msgCounter] = msg;
        msgCounter++;
    }

    public Message[] getNext(){
        int elements;

        if (msgCounter >= operators){
            elements = operators;
        } else {
            elements = msgCounter;
        }

        Message[] toSend = new Message[elements];
        for (int i = 0; i < elements; i++){
            toSend[i] = msgQueue[i];
        }

        //Update msgQueue
        Message[] newQueue = new Message[1000];
        for (int i = 0; i < msgCounter; i++){
            newQueue[i] = msgQueue[elements + i];
        }

        this.msgQueue = newQueue;
        this.msgCounter-=elements;

        return toSend;
    }

}
