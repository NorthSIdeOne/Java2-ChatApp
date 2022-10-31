package server.service;

import lib.dto.MessageDTO;
import lib.event.ChatEvent;
import lib.event.NewChatEvent;
import lib.event.NewMessageEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;

public class EventService {


    private final  Map<String, Queue<ChatEvent>> eventQueues = new ConcurrentHashMap<>();

    private final  Map<String , CyclicBarrier> monitors = new ConcurrentHashMap<>();


    public void createEventQueue(String username) {
        eventQueues.putIfAbsent(username, new LinkedBlockingQueue<>());
        monitors.putIfAbsent(username, new CyclicBarrier(Integer.MAX_VALUE));
    }

    public void addNewChatEvent(final int chatId, String sender, final String recipient){
        Queue<ChatEvent> eventQueue = eventQueues.get(recipient);
        eventQueue.add(new NewChatEvent(chatId,sender,recipient));
        try {
            notifyForEvents(recipient);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * Create an event when a new message is sent and add it to the receiver event queue
     * @param message DTO message object
     */
    public void addNewMessageEvent(final MessageDTO message){
        Queue<ChatEvent> eventQueue = eventQueues.get(message.getReceiver());
        if(eventQueue == null){
            createEventQueue(message.getReceiver());
            eventQueue = eventQueues.get(message.getReceiver());
        }

        NewMessageEvent newMessageEvent = new NewMessageEvent(message);
        eventQueue.add(newMessageEvent);
        if(eventQueue.isEmpty()){
            System.out.println("NEW EVENT MESSAGE , event quee is empty for ->" + message.getReceiver());
        }else{
            System.out.println("NEW EVENT MESSAGE , event quee dimension is " + eventQueue.size() + " For  ->" +message.getReceiver());
        }
        try {
            notifyForEvents(message.getReceiver());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return a list of events for a specific user. If no events are present for the user
     * , the user thread will wait for a new event.
     * @param username the user which is asking for events
     * @return list of chat events
     */
     public List<ChatEvent> getEvents(String username) {
        System.out.println("#### GET EVENTS LOGS -START ######");
        System.out.println("GET events for {"+username+"}");
        System.out.println("Currrent event queues size " + eventQueues.size() + "  " + eventQueues.get(username));
        Queue<ChatEvent> queue = eventQueues.get(username);
        if(queue == null){
             createEventQueue(username);
             queue = eventQueues.get(username);
         }

        if (queue.isEmpty()) {
            waitForEvents(username);
        }

        queue = eventQueues.get(username);
        final List<ChatEvent> chatEvents = new ArrayList<>(queue);

        System.out.println("Events sent for {" + username+"}");
        System.out.println("LIST EVENTs dimension is " +chatEvents.size());
        System.out.println("QUEUE EVENTs dimension is " +queue.size());
        queue.clear();

        System.out.println("#### GET EVENTS LOGS -FINISH ######");
        return chatEvents;
    }

    /**
     * If no events are present in queue , the thread will wait until is unlocked
     * @param username the user which does not have any events now
     */
    private void waitForEvents(String username)  {
        try {
            System.out.println("#### WaitForEvents LOGS -START ######");
            CyclicBarrier monitor = monitors.get(username);
            System.out.println("WAIT FOR " + username);
            System.out.println("NUMBER WAITING (WAIT METHOD): " + monitor.getNumberWaiting());
            monitor.await();
            System.out.println("BROKEN BARIEEER FOR " + username);
            System.out.println("#### WaitForEvents LOGS  LOGS -FINISH ######");
        } catch (BrokenBarrierException | InterruptedException e) {
            System.out.println("THREAD UNLOCKED FOR " + username);
        }
    }

    /**
     * Unlock the thread for the user
     * @param username the user which is notified
     */
    private void notifyForEvents(String username) throws InterruptedException {
        System.out.println("#### NOTIFY  LOGS  LOGS -START ######");
        System.out.println("Monitor unlocked for : " + username);
        CyclicBarrier monitor = monitors.get(username);
        monitor.reset();
        System.out.println("Parties number : " + monitor.getParties());
        System.out.println("NUMBER WAITING : " + monitor.getNumberWaiting());
        System.out.println("#### NOTIFY  LOGS  LOGS -FINISH ######");
    }

    public void nofityAllUsers(){
        monitors.values().forEach(CyclicBarrier::reset);
    }
}
