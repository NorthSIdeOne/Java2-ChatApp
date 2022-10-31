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
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventService {


    private final  Map<String, Queue<ChatEvent>> eventQueues = new ConcurrentHashMap<>();

    private final  Map<String , CyclicBarrier> monitors = new ConcurrentHashMap<>();

    private final Logger LOGGER = Logger.getLogger(EventService.class.getName());

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

        try {
            notifyForEvents(message.getReceiver());
        } catch (InterruptedException e) {
            LOGGER.log(Level.INFO, "New message event added");
        }
    }

    /**
     * Return a list of events for a specific user. If no events are present for the user
     * , the user thread will wait for a new event.
     * @param username the user which is asking for events
     * @return list of chat events
     */
     public List<ChatEvent> getEvents(String username) {
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
        queue.clear();

        return chatEvents;
    }

    /**
     * If no events are present in queue , the thread will wait until is unlocked
     * @param username the user which does not have any events now
     */
    private void waitForEvents(String username)  {
        try {
            CyclicBarrier monitor = monitors.get(username);
            monitor.await();
        } catch (BrokenBarrierException | InterruptedException e) {
            LOGGER.log(Level.INFO, "New event for " + username);
        }
    }

    /**
     * Unlock the thread for the user
     * @param username the user which is notified
     */
    private void notifyForEvents(String username) throws InterruptedException {
        CyclicBarrier monitor = monitors.get(username);
        monitor.reset();
    }
}
