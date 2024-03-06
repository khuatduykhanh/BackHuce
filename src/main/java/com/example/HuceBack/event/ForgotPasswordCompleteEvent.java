package com.example.HuceBack.event;

import com.example.HuceBack.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class ForgotPasswordCompleteEvent extends ApplicationEvent {
    private User user;
    public ForgotPasswordCompleteEvent(User user) {
        super(user);
        this.user = user;
    }
}
