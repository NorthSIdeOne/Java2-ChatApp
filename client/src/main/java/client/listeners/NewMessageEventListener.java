package client.listeners;

import lib.event.NewMessageEvent;

public interface NewMessageEventListener extends ChatEventListener<NewMessageEvent> {

    @Override
    default boolean isApplicable(Class<?> eventClass){
        return NewMessageEvent.class.equals(eventClass);
    }
}
