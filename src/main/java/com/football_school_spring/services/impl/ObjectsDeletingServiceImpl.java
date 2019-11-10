package com.football_school_spring.services.impl;

import com.football_school_spring.models.*;
import com.football_school_spring.repositories.*;
import com.football_school_spring.services.ObjectsDeletingService;
import com.football_school_spring.utils.exception.GettingFromDbException;
import com.football_school_spring.utils.exception.ManagerWithTeamException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ObjectsDeletingServiceImpl implements ObjectsDeletingService {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TeamCoachRepository teamCoachRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private FixtureRepository fixtureRepository;

    @Autowired
    private PlayerFeeRepository playerFeeRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private CoachRepository coachRepository;
    @Autowired
    private CoachFeeRepository coachFeeRepository;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void deleteTeam(long id) {
        teamCoachRepository.deleteAll(teamRepository.findById(id)
                .orElseThrow(() -> new GettingFromDbException(Team.class, id))
                .getTeamCoaches()
        );
        chatMessageRepository.deleteAll(chatMessageRepository.findByTeamId(id));
        fixtureRepository.deleteAll(fixtureRepository.findByTeamId(id));

        playerFeeRepository.deleteAll(playerFeeRepository.findByPlayerTeamId(id));
        playerRepository.deleteAll(playerRepository.findByTeamId(id));

        teamRepository.deleteById(id);
    }

    @Override
    public void deletePlayer(long id) {
        playerFeeRepository.deleteAll(playerFeeRepository.findByPlayerId(id));
        playerRepository.deleteById(id);
    }

    @Override
    public void deleteCoach(long id) {
        Coach coach = coachRepository.findById(id)
                .orElseThrow(() -> new GettingFromDbException(Coach.class, id));
        if (coach.getNumberOfTeamsAsManager() > 0) {
            throw new ManagerWithTeamException();
        }
        teamCoachRepository.deleteAll(coach.getTeamCoaches());
        coachFeeRepository.deleteAll(coachFeeRepository.findByUserId(id));
        deleteVerificationToken(id);
        userRepository.deleteById(id);
    }

    @Override
    public void deleteParent(long id) {
        List<Player> assignedPlayers = playerRepository.findByParentId(id);
        assignedPlayers.forEach(player -> player.setParent(null));
        playerRepository.saveAll(assignedPlayers);
        deleteVerificationToken(id);
        userRepository.deleteById(id);
    }

    @Override
    public void deleteExpiredUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new GettingFromDbException(User.class, id));
        if (user instanceof Coach) {
            deleteCoach(id);
        } else if (user instanceof Parent) {
            deleteParent(id);
        }
    }

    private void deleteVerificationToken(long id) {
        Optional<VerificationToken> tokenOptional = verificationTokenRepository.findByUserId(id);
        tokenOptional.ifPresent(verificationToken -> verificationTokenRepository.delete(verificationToken));
    }
}