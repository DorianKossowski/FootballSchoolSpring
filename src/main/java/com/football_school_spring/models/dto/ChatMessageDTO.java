package com.football_school_spring.models.dto;

import com.football_school_spring.models.ChatMessage;

import java.time.format.DateTimeFormatter;

public class ChatMessageDTO {
    private String userName;
    private boolean isCurrentUser;
    private String date;
    private String text;

    public ChatMessageDTO(ChatMessage chatMessage, long currentUserId) {
        userName = chatMessage.getUser().getName() + " " + chatMessage.getUser().getSurname();
        isCurrentUser = chatMessage.getUser().getId() == currentUserId;
        date = chatMessage.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        text = chatMessage.getMessage();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean getIsCurrentUser() {
        return isCurrentUser;
    }

    public void setCurrentUser(boolean currentUser) {
        isCurrentUser = currentUser;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}