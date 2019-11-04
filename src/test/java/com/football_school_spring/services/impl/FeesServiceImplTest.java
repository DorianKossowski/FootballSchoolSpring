package com.football_school_spring.services.impl;

import com.football_school_spring.models.*;
import com.football_school_spring.models.dto.CoachFeesDTO;
import com.football_school_spring.models.dto.CoachFeesDTOListWrapper;
import com.football_school_spring.models.dto.PlayerFeesDTO;
import com.football_school_spring.models.dto.PlayerFeesDTOListWrapper;
import com.football_school_spring.models.enums.UserStatusName;
import com.football_school_spring.repositories.*;
import com.football_school_spring.services.CoachCreationService;
import com.football_school_spring.services.FeesService;
import com.football_school_spring.services.ServicesTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FeesServiceImplTest extends ServicesTests {
    @Autowired
    private FeesService underTest;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserStatusRepository userStatusRepository;
    @Autowired
    private CoachCreationService coachCreationService;
    @Autowired
    private CoachFeeRepository coachFeeRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PlayerFeeRepository playerFeeRepository;

    private Coach coach1, coach2;
    private Player player1, player2;

    @Test
    void shouldGetCoachesFees() {
        // Given
        setUpTestsCoaches();

        // When
        List<Map<Integer, Boolean>> coachesFees = underTest.getCoachesFees(2019).stream()
                .sorted(Comparator.comparing(coachFeesDTO -> coachFeesDTO.getUser().getId()))
                .map(CoachFeesDTO::getFees)
                .collect(Collectors.toList());

        // Then
        Map<Integer, Boolean> coach1Fees = getCompleteFees(Arrays.asList(1, 2));
        Map<Integer, Boolean> coach2Fees = getCompleteFees(Collections.singletonList(3));
        assertEquals(2, coachesFees.size());
        assertEquals(coach1Fees, coachesFees.get(0));
        assertEquals(coach2Fees, coachesFees.get(1));
    }

    @Test
    void shouldSetCoachesFees() {
        // Given
        setUpTestsCoaches();

        List<CoachFeesDTO> newCoachesFees = new ArrayList<>();
        newCoachesFees.add(new CoachFeesDTO(coach1, getCompleteFees(Arrays.asList(2, 3, 4)), 2019));
        newCoachesFees.add(new CoachFeesDTO(coach2, getCompleteFees(Arrays.asList(2, 4)), 2019));

        // When
        underTest.setUpdatedCoachesFees(2019, new CoachFeesDTOListWrapper(newCoachesFees));

        // Then
        List<Map<Integer, Boolean>> coachesFees = underTest.getCoachesFees(2019).stream()
                .sorted(Comparator.comparing(coachFeesDTO -> coachFeesDTO.getUser().getId()))
                .map(CoachFeesDTO::getFees)
                .collect(Collectors.toList());
        Map<Integer, Boolean> coach1Fees = getCompleteFees(Arrays.asList(2, 3, 4));
        Map<Integer, Boolean> coach2Fees = getCompleteFees(Arrays.asList(2, 4));
        assertEquals(2, coachesFees.size());
        assertEquals(coach1Fees, coachesFees.get(0));
        assertEquals(coach2Fees, coachesFees.get(1));
    }

    @Test
    void shouldThrowWhenWrongMonth() {
        Coach coach = new Coach(1L);
        coach.setId(1L);
        coachCreationService.createCoach(coach);
        coach.setDateOfCreation(LocalDateTime.of(LocalDate.of(2018, 1, 1), LocalTime.MIN));
        userRepository.save(coach);

        List<CoachFeesDTO> newCoachesFees = Collections.singletonList(
                new CoachFeesDTO(coach, getCompleteFees(Collections.singletonList(13)), 2019)
        );
        assertThrows(DateTimeException.class, () -> underTest.setUpdatedCoachesFees(2019, new CoachFeesDTOListWrapper(newCoachesFees)));
    }

    @Test
    void shouldGetPlayersFees() {
        // Given
        setUpTestsPlayers();

        // When
        List<Map<Integer, Boolean>> playerFees = underTest.getPlayersFees(1L, 2019).stream()
                .sorted(Comparator.comparing(playerFeesDTO -> playerFeesDTO.getPlayer().getId()))
                .map(PlayerFeesDTO::getFees)
                .collect(Collectors.toList());

        // Then
        Map<Integer, Boolean> player1Fees = getCompleteFees(Arrays.asList(1, 2));
        Map<Integer, Boolean> player2Fees = getCompleteFees(Collections.singletonList(3));
        assertEquals(2, playerFees.size());
        assertEquals(player1Fees, playerFees.get(0));
        assertEquals(player2Fees, playerFees.get(1));
    }

    @Test
    void shouldSetPlayersFees() {
        // Given
        setUpTestsPlayers();

        List<PlayerFeesDTO> newPlayersFees = new ArrayList<>();
        newPlayersFees.add(new PlayerFeesDTO(player1, getCompleteFees(Arrays.asList(2, 3, 4)), 2019));
        newPlayersFees.add(new PlayerFeesDTO(player2, getCompleteFees(Arrays.asList(2, 4)), 2019));

        // When
        underTest.setUpdatedPlayersFees(2019, new PlayerFeesDTOListWrapper(newPlayersFees));

        // Then
        List<Map<Integer, Boolean>> playersFees = underTest.getPlayersFees(1L, 2019).stream()
                .sorted(Comparator.comparing(playerFeesDTO -> playerFeesDTO.getPlayer().getId()))
                .map(PlayerFeesDTO::getFees)
                .collect(Collectors.toList());
        Map<Integer, Boolean> player1Fees = getCompleteFees(Arrays.asList(2, 3, 4));
        Map<Integer, Boolean> player2Fees = getCompleteFees(Arrays.asList(2, 4));
        assertEquals(2, playersFees.size());
        assertEquals(player1Fees, playersFees.get(0));
        assertEquals(player2Fees, playersFees.get(1));
    }

    private void setUpTestsCoaches() {
        coach1 = new Coach(1L);
        coach2 = new Coach(1L);
        coach1.setId(1L);
        coach2.setId(2L);
        coachCreationService.createCoach(coach1);
        coachCreationService.createCoach(coach2);
        coach1.setUserStatus(userStatusRepository.getByName(UserStatusName.ACTIVE.getName()));
        coach2.setUserStatus(userStatusRepository.getByName(UserStatusName.ACTIVE.getName()));
        coach1.setDateOfCreation(LocalDateTime.of(LocalDate.of(2018, 1, 1), LocalTime.MIN));
        coach2.setDateOfCreation(LocalDateTime.of(LocalDate.of(2018, 1, 1), LocalTime.MIN));
        userRepository.save(coach1);
        userRepository.save(coach2);

        coachFeeRepository.save(new CoachFee(coach1, LocalDate.of(2019, 1, 2)));
        coachFeeRepository.save(new CoachFee(coach1, LocalDate.of(2019, 2, 2)));
        coachFeeRepository.save(new CoachFee(coach2, LocalDate.of(2019, 3, 2)));
        coachFeeRepository.save(new CoachFee(coach2, LocalDate.of(2020, 3, 2)));
    }

    private void setUpTestsPlayers() {
        teamRepository.save(new Team());
        player1 = new Player();
        player2 = new Player();
        player1.setId(1L);
        player2.setId(2L);
        player1.setTeam(teamRepository.findById(1L).orElseThrow(RuntimeException::new));
        player2.setTeam(teamRepository.findById(1L).orElseThrow(RuntimeException::new));
        player1.setDateOfCreation(LocalDateTime.of(LocalDate.of(2018, 1, 1), LocalTime.MIN));
        player2.setDateOfCreation(LocalDateTime.of(LocalDate.of(2018, 1, 1), LocalTime.MIN));
        playerRepository.save(player1);
        playerRepository.save(player2);

        playerFeeRepository.save(new PlayerFee(player1, LocalDate.of(2019, 1, 2)));
        playerFeeRepository.save(new PlayerFee(player1, LocalDate.of(2019, 2, 2)));
        playerFeeRepository.save(new PlayerFee(player2, LocalDate.of(2019, 3, 2)));
        playerFeeRepository.save(new PlayerFee(player2, LocalDate.of(2020, 3, 2)));
    }

    private Map<Integer, Boolean> getCompleteFees(List<Integer> paidMonths) {
        Map<Integer, Boolean> result = IntStream.rangeClosed(1, 12).boxed()
                .collect(Collectors.toMap(Function.identity(), i -> false));
        paidMonths.forEach(month -> result.put(month, true));
        return result;
    }
}