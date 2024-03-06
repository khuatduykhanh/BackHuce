package com.example.HuceBack.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import com.example.HuceBack.entity.User;
/**
 * @author Sampson Alfred
 */
@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {
    private User user;

    public RegistrationCompleteEvent(User user) {
        super(user);
        this.user = user;
    }
}
