import java.util.ArrayList;

public class Main2 {
    public static void main(String[] args){

        //COMMUNICATION AREA
        int MAX_MESSAGES = 100000; // set appropriate max size
        int AMOUNT_MESSAGES = 100000;
        int AMOUNT_ENDPOINTS = 10;

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

        int maxResponseTime = 2700;
        int iteration = 0;
        int errors;
        int calls;
        int callCounter = 0;
        int[] errorCounter = new int[AMOUNT_ENDPOINTS];


        while( msgCounter < AMOUNT_MESSAGES) {

            //calls to endpoint and handle response

            for (int i = 0; i < messageList.size(); i++) {

                Message m = messageList.get(i);
                //System.out.println("FOR LOOP: Current message: " + m.getId());

                res = ep.sendResponse(m);
                callCounter++;


                long responseTime = res.getResponseTime();

                if(responseTime >= maxResponseTime || !res.getStatus()) {
                    errorCounter[res.getPort()]++; // log the error
                    qt.add(m);
                    System.out.println("Message back in queue: " + m.getId());
                }else{
                    //Message sent success
                    msgCounter ++;
                    System.out.println("ITTERATION: " + iteration + ", Message ["+m.getId()+"] finished. Total messages completed: " + msgCounter);
                }
            }//end for
            

            
            messageList = qt.getNext();
            if(messageList.size() > 0 ) System.out.println("Messages for next iteration: " );
            for(int j = 0; j < messageList.size(); j++){
                System.out.println(messageList.get(j).getId());
            }

            
            qt.nextIteration();
            iteration++;

            if(iteration >= 4 ) ep.fixAll();

            System.out.println();
        } // while


        System.out.println("\nITERATION: " + iteration + "  All messages completed. Total messages delivered: " + msgCounter);
        int errorCtr = 0;

        System.out.println("AMOUNT OF LOGGS: ");
        for(int i = 0; i < errorCounter.length; i++){
            System.out.println("Server ["+i+"]: " + errorCounter[i]);
            errorCtr += errorCounter[i];
        }

        System.out.println("Total Logs: " + errorCtr +", total calls: " + callCounter);



    }
}
