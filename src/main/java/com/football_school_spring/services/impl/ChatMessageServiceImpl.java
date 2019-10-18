package com.football_school_spring.services.impl;

import com.football_school_spring.models.ChatMessage;
import com.football_school_spring.models.Coach;
import com.football_school_spring.models.Team;
import com.football_school_spring.models.User;
import com.football_school_spring.models.dto.ChatMessageDTO;
import com.football_school_spring.repositories.ChatMessageRepository;
import com.football_school_spring.repositories.TeamRepository;
import com.football_school_spring.services.ChatMessageService;
import com.football_school_spring.utils.exception.GettingFromDbException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private TeamRepository teamRepository;

    @Override
    public void addMessage(String message, long teamId, User user) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new GettingFromDbException(Team.class, teamId));
        chatMessageRepository.save(new ChatMessage(user, team, LocalDateTime.now(), message));
    }

    @Override
    public List<ChatMessageDTO> getMessagesDTO(long teamId) {
        Coach coach = (Coach) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return chatMessageRepository.findByTeamId(teamId).stream()
                .sorted(Comparator.comparing(ChatMessage::getDate).reversed())
                .map(chatMessage -> new ChatMessageDTO(chatMessage, coach.getId()))
                .collect(Collectors.toList());
    }
}