import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static void main(String[] args){

        //COMMUNICATION AREA
        int MAX_MESSAGES = 100000; // set appropriate max size
        int AMOUNT_MESSAGES = 100000;
        int AMOUNT_ENDPOINTS = 10;

        CircuitBreaker cb = new CircuitBreaker(AMOUNT_ENDPOINTS,2700, 2000);
        Endpoint ep = new Endpoint(AMOUNT_ENDPOINTS, 3000);
        Queue qt = new Queue(AMOUNT_MESSAGES);
        Response res;
        int msgCounter = 0;

        //Create messages
        ArrayList<Message> messageList = new ArrayList<Message>();
        Message msg;
        int randomPort;
        for(int i = 0; i < AMOUNT_MESSAGES; i ++){
            randomPort = (int)(Math.random() * AMOUNT_ENDPOINTS);
            msg = new Message(i,"This is message number: " +i, randomPort );
            messageList.add(msg);
            System.out.println("Message created with port: " + msg.getPort());
        }

        int ctr = 0;
        while(  msgCounter < AMOUNT_MESSAGES) {

            //calls to endpoint and handle response
            boolean toCall;
            for (int i = 0; i < messageList.size(); i++) {

                Message m = messageList.get(i);
                System.out.println("FOR LOOP: Current message: " + m.getId());
                toCall = cb.handleMessage(m);


                if (toCall) {
                    res = ep.sendResponse(m);
                    m = cb.handleResponse(res);
                    if (res.getStatus()) {
                        msgCounter++; // success, message delivered
                        System.out.println("ITTERATION: " + ctr + ", Message ["+m.getId()+"] finished. Total messages completed: " + msgCounter);
                        continue;
                    }
                }
                System.out.println("Message back in queue: " + m.getId());
                System.out.println("Message " + m.getId() + " uses endpoint: " +m.getPort() + ", and this endpoint is: "  + ep.getServerStatus(m));
                qt.add(m);

            }


            messageList = qt.getNext();
            if(messageList.size() > 0 ) System.out.println("Messages for next iteration: " );
            for(int j = 0; j < messageList.size(); j++){
                System.out.println(messageList.get(j).getId());
            }

            qt.nextIteration();

            ctr++;

            if(ctr >= 4 ) ep.fixAll();

            System.out.println();

        } // while
        System.out.println("\nITERATION: " + ctr + "  All messages completed. Total messages delivered: " + msgCounter);


        int errorCtr = 0;
        int[] log = cb.getLog();
        System.out.println("AMOUNT OF LOGGS: ");
        for(int i = 0; i < log.length; i++){
            System.out.println("Server ["+i+"]: " + log[i]);
            errorCtr += log[i];
        }
        System.out.println("Total Logs: " + errorCtr +", total calls: " + cb.getCalls());

    } // main

} //class

