import java.util.ArrayList;

public class Main {

    public static void main(String[] args){

        int MAX_MESSAGES = 100000; // set appropriate max size
        int AMOUNT_MESSAGES = 100000;
        int AMOUNT_ENDPOINTS = 10;

        CircuitBreaker cb = new CircuitBreaker(AMOUNT_ENDPOINTS,2700, 2000, 1000000);
        Endpoint ep = new Endpoint(AMOUNT_ENDPOINTS, 3000);
        Queue queue = new Queue(AMOUNT_MESSAGES);


        //Create Messages
        ArrayList<Message> messageList = new ArrayList<Message>();
        Message msg;
        int randomPort;
        for(int i = 0; i < AMOUNT_MESSAGES; i ++){
            randomPort = (int)(Math.random() * AMOUNT_ENDPOINTS);
            msg = new Message(i,"This is message number: " +i, randomPort );
            messageList.add(msg);
            System.out.println("Message created with port: " + msg.getPort());
        }


        int iteration = 0;
        Response response;
        int completedMessages = 0;
        boolean callServer;
        while(completedMessages < AMOUNT_MESSAGES) {
            // ITERATION LOOP

            for(int i = 0; i < messageList.size(); i++) {
                //For each message
                Message m = messageList.get(i);
                System.out.println("FOR LOOP: Current message: " + m.getId());
                callServer = cb.handleMessage(m);

                if (callServer) {
                    response = ep.sendResponse(m);
                    m = cb.handleResponse(response);
                    if (response.getStatus()) {
                        // success, message delivered
                        completedMessages++;
                        System.out.println("ITTERATION: " + iteration + ", Message ["+m.getId()+"] finished. Total messages completed: " + completedMessages);
                        continue;
                    }
                }
                //Put message in queue.
                System.out.println("Message back in queue: " + m.getId());
                System.out.println("Message " + m.getId() + " uses endpoint: " +m.getPort() + ", and this endpoint is: "  + ep.getServerStatus(m));
                queue.add(m);
            }//end for


            messageList = queue.getNext(); //Update queue
            if(messageList.size() > 0 ) System.out.println("Messages for next iteration: " );
            for(int j = 0; j < messageList.size(); j++){
                System.out.println(messageList.get(j).getId());
            }

            queue.nextIteration();
            iteration++;
            if(iteration >= 4 ) ep.fixAll();

            System.out.println();
        } // while

        System.out.println("\nITERATION: " + iteration + "  All messages completed. Total messages delivered: " + completedMessages);
        int errorCtr = 0;
        int[] log = cb.getErrorLog();
        System.out.println("AMOUNT OF LOGGS - WITH CIRCUIT BREAKER ");

        for(int i = 0; i < log.length; i++){
            System.out.println("Server ["+i+"]: " + log[i]);
            errorCtr += log[i];
        }
        System.out.println("Total Logs: " + errorCtr +", total calls: " + cb.getNumberOfCalls());
    } // main
} //class

