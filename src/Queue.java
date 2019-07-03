import java.util.ArrayList;


/*
This is a waiting-time based queue. Meaning the messages do not have a given position in the queue, but
rather each message has their own timer which decides when its time to leave.
 */


public class Queue {
    int[] waitingTimes;
    Message[] messages;
    int[] visited;
    int idx; // index of last message in queue,
    int iteration; // current iteration

    public Queue(int max_elements){
        this.waitingTimes = new int[max_elements];
        this.messages = new Message[max_elements];
        this.visited = new int[max_elements];
        this.idx = 0;
        this.iteration = 0;
    }

    //Add a new message. Generate waiting time based on number of times visited the queue beforehand.
    public void add(Message msg){
        messages[idx] = msg;
        int wt = visited[msg.getId()] +1; //This line is the logic for how long a message should wait in the queue.
        visited[msg.getId()] ++;
        waitingTimes[idx] = wt;
        idx ++;
    }

    //Returns all messages who are due.
    public ArrayList<Message> getNext(){
        ArrayList<Message> resultList = new ArrayList<Message>();

        for(int i = 0; i < idx; i++){
            if( waitingTimes[i] == 0){
                resultList.add(messages[i]);
                //remove message from queue, bubble all elements downwards.
                for(int j = i; j < idx - 1; j++){
                    messages[j] = messages[j+1];
                    waitingTimes[j] = waitingTimes[j+1];
                }
                idx --; // update last element position
                i--; // Next iteration go through the same i.
            }
        }
        //We now have all messages which are done with the queue.
        System.out.println("ITERATION: "+ iteration+ " There were [" +resultList.size()+"]  messages leaving the queue. ");
        return resultList;
    }

    //update time left for each element.
    public void nextIteration(){
        iteration ++;
        for(int i = 0; i < idx; i++){
            waitingTimes[i] --;
        }
    }


    public void printQueue(){
        System.out.println("There are " + idx + " elements in the queue:");
        for(int i = 0; i < idx; i++){
            Message msg = messages[i];
            int wt = waitingTimes[i];
            System.out.println("Message id: " + msg.getId() + ", Waiting time left: " + wt + ", times visited: " +visited[i] );
        }
    }


}//end Queue
