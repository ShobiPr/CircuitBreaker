import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static void main(String[] args){

        //COMMUNICATION AREA
        int MAX_MESSAGES = 1000; // set appropriate max size
        int AMOUNT_MESSAGES = 10;

        CircuitBreaker cb = new CircuitBreaker(4,10000,10000);
        Endpoint ep = new Endpoint(4);
        Queue qt = new Queue(AMOUNT_MESSAGES);
        Response res;
        int msgCounter = 0;

        //Create messages
        ArrayList<Message> messageList = new ArrayList<Message>();
        Message msg;
        int randomPort;
        for(int i = 0; i < AMOUNT_MESSAGES; i ++){
            randomPort = (int)(Math.random() * 5);
            msg = new Message(i,"This is message number: " +i, randomPort );
            messageList.add(msg);
        }

        int ctr = 0;
        while(ctr < 80) {

            //calls to endpoint and handle response
            boolean toCall;
            for (int i = 0; i < messageList.size(); i++) {

                Message m = messageList.get(i);
                toCall = cb.handleMessage(m);
                System.out.println("ToCall: " + toCall);

                if (toCall) {
                    res = ep.sendResponse(m);
                    m = cb.handleResponse(res);
                    if (m == null) {
                        msgCounter++;
                        System.out.println("ITTERATION: " + ctr + " Messages completed: " + msgCounter);
                        continue;
                    }
                }

                qt.add(m);

            }


            messageList = qt.getNext();
            qt.nextIteration();

            ctr++;

            /*if(ctr >= 25){ ep.fixServer(8); }
            if(ctr >= 33){ ep.fixServer(9); }
            if(ctr >= 45){ ep.fixServer(7); }*/



        } // while

    } // main

} //class

