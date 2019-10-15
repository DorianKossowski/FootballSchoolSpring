package com.football_school_spring.services.impl;

import com.football_school_spring.models.Parent;
import com.football_school_spring.models.Player;
import com.football_school_spring.notifications.OnRegistrationInviteEvent;
import com.football_school_spring.repositories.ParentRepository;
import com.football_school_spring.repositories.PlayerRepository;
import com.football_school_spring.services.ParentCreationService;
import com.football_school_spring.services.PlayerManageService;
import com.football_school_spring.utils.GettingFromDbException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class PlayerManageServiceImpl implements PlayerManageService {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ParentRepository parentRepository;
    @Autowired
    private ParentCreationService parentCreationService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public Player getPlayerById(long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new GettingFromDbException(Player.class, id));
    }

    @Override
    @Transactional
    public void update(Player player) {
        Player playerInDb = playerRepository.findById(player.getId())
                .orElseThrow(() -> new GettingFromDbException(Player.class, player.getId()));
        playerInDb.setName(player.getName());
        playerInDb.setSurname(player.getSurname());
        playerInDb.setYear(player.getYear());

        String newParentMail = player.getParent().getMail();
        if (!playerInDb.getParent().getMail().equals(newParentMail)) {
            Optional<Parent> parentOptional = parentRepository.findByMail(newParentMail);
            Parent parent;
            if (parentOptional.isPresent()) {
                parent = parentOptional.get();
            } else {
                parent = new Parent(newParentMail);
                parentCreationService.createParent(parent);
                eventPublisher.publishEvent(new OnRegistrationInviteEvent(parent));
            }
            playerInDb.setParent(parent);
        }

        playerRepository.save(playerInDb);
    }

    @Override
    public void delete(long playerId) {
        playerRepository.deleteById(Long.valueOf(playerId));
    }
}