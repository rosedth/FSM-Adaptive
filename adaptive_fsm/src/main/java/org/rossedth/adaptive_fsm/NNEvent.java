package org.rossedth.adaptive_fsm;
import org.jeasy.states.api.AbstractEvent;

class NNEvent extends AbstractEvent {

    public NNEvent() {
        super("NNEvent");
    }

    protected NNEvent(String name) {
        super(name);
    }

}