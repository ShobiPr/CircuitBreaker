import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static void main(String[] args){

        //COMMUNICATION AREA
        int MAX_MESSAGES = 1000; // set appropriate max size
        int AMOUNT_MESSAGES = 100;

        //Create messages
        ArrayList<Message> messageList = new ArrayList<Message>();
        Message msg;
        int randomPort;
        for(int i = 0; i < AMOUNT_MESSAGES; i ++){
            randomPort = (int)(Math.random() * 100);
            msg = new Message(i,"This is message number: " +i, randomPort );
            messageList.add(msg);
        }


        //send messages from deliveryService to CB




        //call on server with given portNum from CB

        //return success/fail call back to CB with port,status, message

        //send failed message to queue

        //maybe use enum in switch?

        }

}

