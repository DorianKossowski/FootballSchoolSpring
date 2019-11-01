package com.football_school_spring.services;

import com.football_school_spring.models.User;
import com.football_school_spring.models.dto.ChatMessageDTO;

import java.util.List;

public interface ChatMessageService {
    void addMessage(String message, long teamId, User user);

    List<ChatMessageDTO> getMessagesDTO(long teamId, User currentUser);
}