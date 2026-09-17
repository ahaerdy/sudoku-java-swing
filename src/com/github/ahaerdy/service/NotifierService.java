package com.github.ahaerdy.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.ahaerdy.service.EventEnum.CLEAR_SPACE;

public class NotifierService {

    private final Map<EventEnum, List<EventListener>> listeners = new HashMap<>() {{
        put(CLEAR_SPACE, new ArrayList<>());
    }};

    public void subscribe(final EventEnum eventType, final EventListener listener) {
        listeners.get(eventType).add(listener);
    }

    public void notify(final EventEnum eventType) {
        listeners.get(eventType).forEach(l -> l.update(eventType));
    }

}