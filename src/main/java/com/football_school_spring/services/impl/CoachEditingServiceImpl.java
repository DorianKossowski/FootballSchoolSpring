package com.football_school_spring.services.impl;

import com.football_school_spring.models.Coach;
import com.football_school_spring.models.enums.CoachPrivilegeName;
import com.football_school_spring.models.enums.UserStatusName;
import com.football_school_spring.repositories.CoachRepository;
import com.football_school_spring.repositories.UserStatusRepository;
import com.football_school_spring.services.CoachEditingService;
import com.football_school_spring.utils.CoachEditingValidationResult;
import com.football_school_spring.utils.EditingValidationErrorName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.football_school_spring.models.enums.UserStatusName.ACTIVE;
import static com.football_school_spring.models.enums.UserStatusName.BLOCKED;

@Service
public class CoachEditingServiceImpl implements CoachEditingService {
    @Autowired
    private CoachRepository coachRepository;
    @Autowired
    private UserStatusRepository userStatusRepository;

    @Override
    public CoachEditingValidationResult isEditingValid(long coachId) {
        Optional<Coach> coachOptional = coachRepository.findById(coachId);
        if (!coachOptional.isPresent()) {
            return CoachEditingValidationResult.invalid(EditingValidationErrorName.NOT_EXISTS,
                    String.format("Can't edit coach with id: %s - doesn't exist", coachId));
        }

        Coach coach = coachOptional.get();
        if (coach.getUserStatus().getName().equals(UserStatusName.WAITING_FOR_APPROVAL.getName())) {
            return CoachEditingValidationResult.invalid(EditingValidationErrorName.NOT_ACTIVE,
                    String.format("Can't edit coach with id: %s - doesn't active", coachId));
        }
        return CoachEditingValidationResult.valid(coach);
    }

    @Override
    public CoachEditingValidationResult isTeamsNumberEditingValid(Coach coach, int newNumberOfTeams) {
        long numberOfTeamsAsManager = coach.getTeamCoaches().stream()
                .filter(teamCoach -> teamCoach.getCoachPrivilege().getName().equals(CoachPrivilegeName.MANAGER.getName()))
                .count();
        if (numberOfTeamsAsManager > newNumberOfTeams) {
            return CoachEditingValidationResult.invalid(EditingValidationErrorName.WRONG_NUMBER,
                    String.format("Can't edit coach with id: %s - too small number of teams", coach.getId()));
        }
        return CoachEditingValidationResult.valid(coach);
    }

    @Override
    public void changeCoachStatus(Coach coach) {
        if (coach.getUserStatus().getName().equals(ACTIVE.getName())) {
            coach.setUserStatus(userStatusRepository.getByName(BLOCKED.getName()));
        } else {
            coach.setUserStatus(userStatusRepository.getByName(ACTIVE.getName()));
        }
        coachRepository.save(coach);
    }

    @Override
    public void setMaxNumberOfTeams(Coach coach, int newNumberOfTeams) {
        coach.setMaxNumberOfTeams(newNumberOfTeams);
        coachRepository.save(coach);
    }
}