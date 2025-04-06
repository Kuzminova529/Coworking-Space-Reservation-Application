import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.reservationapplication.domain.dto.CoworkingSpaceDto;
import org.reservationapplication.domain.dto.ReservationDto;
import org.reservationapplication.domain.model.CoworkingSpace;
import org.reservationapplication.domain.model.Reservation;
import org.reservationapplication.domain.model.User;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.reservationapplication.domain.repository.JPARepos.ReservationRepositoryJPA;
import org.reservationapplication.domain.repository.SpringDataJPARepos.ReservationRepositorySpring;
import org.reservationapplication.service.CoworkingSpaceServiceImpl;
import org.reservationapplication.service.ReservationServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceImplTest {


    @Mock
    private ReservationRepositorySpring reservationRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Test
    public void testGetAllReservation_Empty() {
        ReservationServiceImpl reservationService = new ReservationServiceImpl(reservationRepository);

        when(reservationRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(reservationService.getAllReservation().isEmpty());
    }

    @Test
    public void testGetPersonalReservation_Empty() {
        // Создаем экземпляр ReservationService
        ReservationServiceImpl reservationService = new ReservationServiceImpl(reservationRepository);

        // Создаем резервации
        Reservation reservation1 = new Reservation();
        reservation1.setId(1L);
        reservation1.setUserID(1L);
        reservation1.setStartDateTime(LocalDateTime.now().plusHours(1));
        reservation1.setEndDateTime(LocalDateTime.now().plusHours(2));
        User user = new User();
        user.setId(99L);

        when(reservationRepository.readPersonalReservations(user.getId())).thenReturn(new ArrayList<>());

        List<ReservationDto> result = reservationService.getPersonalReservation(user.getId());

        assertTrue(result.isEmpty());
    }

    @Test
    public void testRemoveReservationById_Success() {
        ReservationServiceImpl reservationService = new ReservationServiceImpl(reservationRepository);

        // Creating test reservations
        Reservation reservation1 = new Reservation();
        reservation1.setId(1L);
        reservation1.setUserID(1L);
        reservation1.setStartDateTime(LocalDateTime.now());
        reservation1.setEndDateTime(LocalDateTime.now().plusHours(2));

        Reservation reservation2 = new Reservation();
        reservation2.setId(2L);
        reservation2.setUserID(2L);
        reservation2.setStartDateTime(LocalDateTime.now());
        reservation2.setEndDateTime(LocalDateTime.now().plusHours(2));

        long reservationId = 1L;
        doNothing().when(reservationRepository).updateStatus(reservationId);

        reservationService.removeReservationById(reservationId);

        verify(reservationRepository, times(1)).updateStatus(reservationId);
    }

    @Test
    public void testUserAddReservationWithPastDate() {
        CoworkingSpaceServiceImpl coworkingSpaceService = mock(CoworkingSpaceServiceImpl.class);
        ReservationServiceImpl reservationService = new ReservationServiceImpl(reservationRepository);

        CoworkingSpaceDto coworkingSpace = new CoworkingSpaceDto();
        coworkingSpace.setActive(true);
        when(coworkingSpaceService.getCoworkingSpaceByID(1L)).thenReturn(coworkingSpace);

        User user = new User();
        user.setId(1L);

        // Creating an object with a past booking date
        String reservationName = "Test Reservation";
        LocalDate bookingDate = LocalDate.now().minusDays(1); // Yesterday
        LocalDateTime startDateTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endDateTime = startDateTime.plusHours(1);

        // Executing the method and checking for the exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.userAddReservation(1L, reservationName, bookingDate, startDateTime, endDateTime, user, coworkingSpaceService);
        });

        assertEquals("You cannot register a past date!", exception.getMessage()); // Verifying the exception message
    }

}
