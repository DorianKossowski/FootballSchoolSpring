package com.football_school_spring.services.impl;

import com.football_school_spring.models.Coach;
import com.football_school_spring.models.Team;
import com.football_school_spring.models.User;
import com.football_school_spring.models.dto.NewPlayerDTO;
import com.football_school_spring.models.enums.UserStatusName;
import com.football_school_spring.notifications.EmailService;
import com.football_school_spring.repositories.TeamRepository;
import com.football_school_spring.repositories.UserRepository;
import com.football_school_spring.repositories.UserStatusRepository;
import com.football_school_spring.services.CoachCreationService;
import com.football_school_spring.services.ControllingFeesDatesService;
import com.football_school_spring.services.PlayerCreationService;
import com.football_school_spring.services.ServicesTests;
import com.football_school_spring.utils.validation.FeesAnalyser;
import com.football_school_spring.utils.validation.FeesAnalysisResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

// JUnit4
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@RunWith(PowerMockRunner.class)
@PrepareForTest(FeesAnalyser.class)
class ControllingFeesDatesServiceImplTest extends ServicesTests {
    @Autowired
    private ControllingFeesDatesService underTest;
    @Autowired
    private CoachCreationService coachCreationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserStatusRepository userStatusRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private PlayerCreationService playerCreationService;

    // PowerMock
    public ControllingFeesDatesServiceImplTest() {
    }

    @Test
    public void shouldBlockManager() {
        // Given
        FeesAnalysisResult mockedResult = new FeesAnalysisResult();
        mockedResult.setShouldBeBlocked(true);
        setTestManagerWithMocking(mockedResult);

        // When
        underTest.controlFees();

        // Then
        User userAfterControl = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        assertEquals(UserStatusName.BLOCKED.getName(), userAfterControl.getUserStatus().getName());
        verify(emailService, times(1)).sendMailWithBlockInfo("coach@mail.com");
    }

    @Test
    public void shouldRemindManager() {
        // Given
        FeesAnalysisResult mockedResult = new FeesAnalysisResult();
        mockedResult.setShouldRemind(true);
        setTestManagerWithMocking(mockedResult);

        // When
        underTest.controlFees();

        // Then
        User userAfterControl = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        assertEquals(UserStatusName.ACTIVE.getName(), userAfterControl.getUserStatus().getName());
        verify(emailService, times(1)).sendMailWithFeeReminder("coach@mail.com");
    }

    @Test
    public void shouldRemindParent() {
        // Given
        FeesAnalysisResult mockedResult = new FeesAnalysisResult();
        mockedResult.setShouldRemind(true);
        setTestPlayerWithMocking(mockedResult);

        // When
        underTest.controlFees();

        // Then
        verify(emailService, times(1)).sendMailWithFeeReminder("parent@mail.com");
    }

    private void setTestManagerWithMocking(FeesAnalysisResult mockedResult) {
        PowerMockito.mockStatic(FeesAnalyser.class);
        BDDMockito.given(FeesAnalyser.analyseCoachFees(Mockito.any(), Mockito.any())).willReturn(mockedResult);

        Coach coachToAdd = new Coach(2L);
        coachToAdd.setMail("coach@mail.com");
        coachCreationService.createCoach(coachToAdd);

        User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        user.setUserStatus(userStatusRepository.getByName(UserStatusName.ACTIVE.getName()));
        userRepository.save(user);
    }

    private void setTestPlayerWithMocking(FeesAnalysisResult mockedResult) {
        PowerMockito.mockStatic(FeesAnalyser.class);
        BDDMockito.given(FeesAnalyser.analysePlayerFees(Mockito.any())).willReturn(mockedResult);

        teamRepository.save(new Team());
        playerCreationService.createPlayer(new NewPlayerDTO(1L, "parent@mail.com"));
    }
}
