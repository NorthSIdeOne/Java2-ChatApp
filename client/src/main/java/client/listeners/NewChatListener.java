package client.listeners;

import lib.event.NewChatEvent;

public interface NewChatListener extends ChatEventListener<NewChatEvent> {

    @Override
    default boolean isApplicable(Class<?> eventClass){
        return NewChatEvent.class.equals(eventClass);
    }
}
