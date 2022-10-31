package client.listeners;

import lib.event.ChatEvent;

public interface ChatEventListener <EV extends ChatEvent> {

    boolean isApplicable(Class<?> eventClass);

    void accept(EV event);
}
