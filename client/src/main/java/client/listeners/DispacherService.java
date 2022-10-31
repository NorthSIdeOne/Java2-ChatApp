package client.listeners;

import client.services.ChatController;
import lib.event.ChatEvent;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class DispacherService {

    private ScheduledExecutorService executor;

    private static final Map<String, DispacherService> registry = new ConcurrentHashMap<>();

    private String username;

    private final List<ChatEventListener> listeners = new CopyOnWriteArrayList<>();

    private DispacherService(String username) {
        this.username = username;

        System.out.println("WTFFFFFFFFFFFFFFFFFFFFFFFFFF  " + username);
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleWithFixedDelay(
                () -> {
                    List<ChatEvent> events = ChatController.getInstance().getEvents(username);
                    events.forEach(this::notifyListners);
                    System.out.println("AVEM EVENIMENTEEEE dimension " + events.size());
                },
                0,
                1,
                TimeUnit.SECONDS
        );
    }

    public static DispacherService getInstance(String username){
        registry.putIfAbsent(username, new DispacherService(username));
        return registry.get(username);
    }

    public void addListner(ChatEventListener listner){
        listeners.add(listner);
    }

    public void removeListner(ChatEventListener listner){
        listeners.remove(listner);
        System.out.println("Listner removed ");
    }

    public void notifyListners(ChatEvent event){
        System.out.println("NOTIFICATIOOOOON");
        listeners.stream()
//                .filter(listner -> listner.isApplicable(event.getClass()))
                .forEach(listner  ->listner.accept(event));
    }
}
