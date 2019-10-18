package com.football_school_spring.services.impl;

import com.football_school_spring.models.Parent;
import com.football_school_spring.models.Player;
import com.football_school_spring.models.Team;
import com.football_school_spring.models.dto.NewPlayerDTO;
import com.football_school_spring.notifications.OnRegistrationInviteEvent;
import com.football_school_spring.repositories.ParentRepository;
import com.football_school_spring.repositories.PlayerRepository;
import com.football_school_spring.repositories.TeamRepository;
import com.football_school_spring.services.ParentCreationService;
import com.football_school_spring.services.PlayerCreationService;
import com.football_school_spring.utils.exception.GettingFromDbException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class PlayerCreationServiceImpl implements PlayerCreationService {
    @Autowired
    private ParentRepository parentRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ParentCreationService parentCreationService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void createPlayer(NewPlayerDTO newPlayerDTO) {
        Optional<Parent> parentOptional = parentRepository.findByMail(newPlayerDTO.getMail());
        Team team = teamRepository.findById(newPlayerDTO.getTeamId())
                .orElseThrow(() -> new GettingFromDbException(Team.class, newPlayerDTO.getTeamId()));
        Parent parent;

        if (parentOptional.isPresent()) {
            parent = parentOptional.get();
        } else {
            parent = new Parent(newPlayerDTO.getMail());
            parentCreationService.createParent(parent);
            eventPublisher.publishEvent(new OnRegistrationInviteEvent(parent));
        }
        playerRepository.save(new Player(newPlayerDTO, parent, team));
    }
}