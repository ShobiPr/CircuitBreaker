import java.util.ArrayList;

public class Main2 {
    public static void main(String[] args){

        int MAX_MESSAGES = 100000; // set appropriate max size
        int AMOUNT_MESSAGES = 100000;
        int AMOUNT_ENDPOINTS = 10;

        Endpoint ep = new Endpoint(AMOUNT_ENDPOINTS, 3000);
        Queue queue = new Queue(AMOUNT_MESSAGES);


        //Create all messages to be sent.
        ArrayList<Message> messageList = new ArrayList<Message>();
        Message msg;
        int randomPort;
        for(int i = 0; i < AMOUNT_MESSAGES; i ++){
            randomPort = (int)(Math.random() * AMOUNT_ENDPOINTS);
            msg = new Message(i,"This is message number: " +i, randomPort );
            messageList.add(msg);
            System.out.println("Message created with port: " + msg.getPort());
        }


        int maxResponseTime = 2700;
        int iteration = 0; int callCounter = 0; int completedMessages = 0;
        int errors, calls;
        Response response;
        int[] errorCounter = new int[AMOUNT_ENDPOINTS];

        while( completedMessages < AMOUNT_MESSAGES) {
            //ITERATION LOOP

            for(int i = 0; i < messageList.size(); i++) {
                //For each message in list:
                Message m = messageList.get(i);
                response = ep.sendResponse(m);
                callCounter++;

                long responseTime = response.getResponseTime();

                if(responseTime >= maxResponseTime || !response.getStatus()) {
                    //Too large responseTime OR endpoint is down.
                    errorCounter[response.getPort()]++; // log the error
                    queue.add(m);
                    System.out.println("Message back in queue: " + m.getId());
                }else{
                    //Message sent successful
                    completedMessages ++;
                    System.out.println("ITTERATION: " + iteration + ", Message ["+m.getId()+"] finished. Total messages completed: " + completedMessages);
                }
            }//end for
            

            messageList = queue.getNext(); // Update messageList
            if(messageList.size() > 0 ) System.out.println("Messages for next iteration: " );
            for(int j = 0; j < messageList.size(); j++){
                System.out.println(messageList.get(j).getId());
            }

            queue.nextIteration();
            iteration++;

            if(iteration >= 4 ) ep.fixAll(); // Decide that all servers should be fixed after 4 iterations.
            System.out.println();
        } // while



        System.out.println("\nITERATION: " + iteration + "  All messages completed. Total messages delivered: " + completedMessages);
        int errorCtr = 0;

        System.out.println("AMOUNT OF LOGGS - WITHOUT CIRCUIT BREAKER: ");
        for(int i = 0; i < errorCounter.length; i++){
            System.out.println("Server ["+i+"]: " + errorCounter[i]);
            errorCtr += errorCounter[i];
        }

        System.out.println("Total Logs: " + errorCtr +", total calls: " + callCounter);

    }
}
