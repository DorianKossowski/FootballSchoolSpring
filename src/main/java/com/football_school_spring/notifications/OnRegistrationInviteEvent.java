package com.football_school_spring.notifications;

import com.football_school_spring.models.User;
import org.springframework.context.ApplicationEvent;

public class OnRegistrationInviteEvent extends ApplicationEvent {
    private static final long serialVersionUID = 3605854568022505383L;

    private User user;

    public OnRegistrationInviteEvent(User user) {
        super(user);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}