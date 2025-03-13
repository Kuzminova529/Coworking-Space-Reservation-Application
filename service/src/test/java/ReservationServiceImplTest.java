import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.reservationapplication.model.*;
import org.reservationapplication.service.CoworkingSpaceServiceImpl;
import org.reservationapplication.service.ReservationService;
import org.reservationapplication.service.ReservationServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceImplTest {

    @Test
    public void testGetAllReservation() {
        ReservationServiceImpl reservationService = new ReservationServiceImpl();

        Reservation reservation1 = new Reservation();
        reservation1.setCoworkingSpaceID(1L);
        reservation1.setCustomerID(1L);
        reservation1.setStartDateTime(LocalDateTime.now().plusHours(1));
        reservation1.setEndDateTime(LocalDateTime.now().plusHours(2));

        Reservation reservation2 = new Reservation();
        reservation2.setCoworkingSpaceID(2L);
        reservation2.setCustomerID(2L);
        reservation2.setStartDateTime(LocalDateTime.now().plusHours(3));
        reservation2.setEndDateTime(LocalDateTime.now().plusHours(4));

        reservationService.addReservation(reservation1);
        reservationService.addReservation(reservation2);

        TreeSet<Reservation> allReservations = reservationService.getAllReservation();

        assertEquals(2, allReservations.size(), "There should be two reservations.");

        assertTrue(allReservations.first().getStartDateTime().isBefore(allReservations.last().getStartDateTime()),
                "Reservations should be sorted by start date and time.");
    }

    @Test
    public void testGetAllReservation_Empty() {
        ReservationServiceImpl reservationService = new ReservationServiceImpl();

        TreeSet<Reservation> allReservations = reservationService.getAllReservation();

        assertTrue(allReservations.isEmpty(), "The list should be empty.");
    }

    @Test
    public void testGetPersonalReservation() {
        // Создаем экземпляр ReservationService
        ReservationServiceImpl reservationService = new ReservationServiceImpl();

        // Создаем резервации
        Reservation reservation1 = new Reservation();
        reservation1.setCoworkingSpaceID(1L);
        reservation1.setCustomerID(1L);
        reservation1.setStartDateTime(LocalDateTime.now().plusHours(1));
        reservation1.setEndDateTime(LocalDateTime.now().plusHours(2));

        Reservation reservation2 = new Reservation();
        reservation2.setCoworkingSpaceID(2L);
        reservation2.setCustomerID(2L);
        reservation2.setStartDateTime(LocalDateTime.now().plusHours(3));
        reservation2.setEndDateTime(LocalDateTime.now().plusHours(4));

        reservationService.addReservation(reservation1);
        reservationService.addReservation(reservation2);

        User user = new Customer();
        user.setId(1L);

        TreeSet<Reservation> personalReservations = reservationService.getPersonalReservation(user);

        assertEquals(1, personalReservations.size(), "There should be one reservation for the user.");

        assertTrue(personalReservations.contains(reservation1), "The personal reservation should be the one belonging to the user.");
    }

    @Test
    public void testGetPersonalReservation_Empty() {
        // Создаем экземпляр ReservationService
        ReservationServiceImpl reservationService = new ReservationServiceImpl();

        // Создаем резервации
        Reservation reservation1 = new Reservation();
        reservation1.setCoworkingSpaceID(1L);
        reservation1.setCustomerID(1L);
        reservation1.setStartDateTime(LocalDateTime.now().plusHours(1));
        reservation1.setEndDateTime(LocalDateTime.now().plusHours(2));

        reservationService.addReservation(reservation1);

        // Создаем пользователя с другим ID
        User user = new Customer();
        user.setId(2L); // Устанавливаем ID пользователя, который не имеет резерваций

        // Получаем персональные резервации для этого пользователя
        TreeSet<Reservation> personalReservations = reservationService.getPersonalReservation(user);

        // Проверяем, что для этого пользователя нет резерваций
        assertTrue(personalReservations.isEmpty(), "There should be no personal reservations for this user.");
    }

    @Test
    public void testGetReservationsByCoworkingSpaceAndDate() {
        ReservationServiceImpl reservationService = new ReservationServiceImpl();

        LocalDate date = LocalDate.now();

        Reservation reservation1 = new Reservation();
        reservation1.setCoworkingSpaceID(1L);
        reservation1.setStartDateTime(date.atTime(10, 0));
        reservation1.setEndDateTime(date.atTime(12, 0));

        Reservation reservation2 = new Reservation();
        reservation2.setCoworkingSpaceID(1L);
        reservation2.setStartDateTime(date.atTime(14, 0));
        reservation2.setEndDateTime(date.atTime(16, 0));

        Reservation reservation3 = new Reservation();
        reservation3.setCoworkingSpaceID(2L);
        reservation3.setStartDateTime(date.atTime(9, 0));
        reservation3.setEndDateTime(date.atTime(10, 0));

        reservationService.addReservation(reservation1);
        reservationService.addReservation(reservation2);
        reservationService.addReservation(reservation3);

        TreeSet<Reservation> result = reservationService.getReservationsByCoworkingSpaceAndDate(1L, date);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(reservation1));
        assertTrue(result.contains(reservation2));
        assertFalse(result.contains(reservation3));
    }

    @Test
    public void testRemoveReservationById_Success() {
        ReservationServiceImpl reservationService = new ReservationServiceImpl();

        // Creating test reservations
        Reservation reservation1 = new Reservation();
        reservation1.setCoworkingSpaceID(1L);
        reservation1.setCustomerID(1L);
        reservation1.setStartDateTime(LocalDateTime.now());
        reservation1.setEndDateTime(LocalDateTime.now().plusHours(2));

        Reservation reservation2 = new Reservation();
        reservation2.setCoworkingSpaceID(2L);
        reservation2.setCustomerID(2L);
        reservation2.setStartDateTime(LocalDateTime.now());
        reservation2.setEndDateTime(LocalDateTime.now().plusHours(2));

        reservationService.addReservation(reservation1);
        reservationService.addReservation(reservation2);

        assertTrue(reservationService.removeReservationById(reservation1.getReservationID())); //success delete
        assertFalse(reservationService.getAllReservation().contains(reservation1)); //reservation was deleted
    }

    @Test
    public void testRemoveReservationById_Failure() {
        ReservationServiceImpl reservationService = new ReservationServiceImpl();

        // Creating test reservations
        Reservation reservation1 = new Reservation();
        reservation1.setCoworkingSpaceID(1L);
        reservation1.setCustomerID(1L);
        reservation1.setStartDateTime(LocalDateTime.now());
        reservation1.setEndDateTime(LocalDateTime.now().plusHours(2));

        Reservation reservation2 = new Reservation();
        reservation2.setCoworkingSpaceID(2L);
        reservation2.setCustomerID(2L);
        reservation2.setStartDateTime(LocalDateTime.now());
        reservation2.setEndDateTime(LocalDateTime.now().plusHours(2));

        reservationService.addReservation(reservation1);
        reservationService.addReservation(reservation2);

        long nonExistentId = 999L;
        assertFalse(reservationService.removeReservationById(nonExistentId));
    }

    @Test
    public void testAddReservation() {
        // Создаем экземпляр ReservationService
        ReservationServiceImpl reservationService = new ReservationServiceImpl();

        // Создаем резервацию для добавления
        Reservation reservation = new Reservation();
        reservation.setCoworkingSpaceID(1L);
        reservation.setCustomerID(1L);
        reservation.setStartDateTime(LocalDateTime.now());
        reservation.setEndDateTime(LocalDateTime.now().plusHours(2));

        reservationService.addReservation(reservation);

        TreeSet<Reservation> allReservations = reservationService.getAllReservation();
        assertEquals(1, allReservations.size());

        assertTrue(allReservations.contains(reservation)); // The reservation should be present in the list.
    }

    @Test
    public void testUserAddReservationSuccess() {
        // Mocking the services
        CoworkingSpaceServiceImpl coworkingSpaceService = mock(CoworkingSpaceServiceImpl.class);
        ReservationServiceImpl reservationService = mock(ReservationServiceImpl.class);

        CoworkingSpace coworkingSpace = new CoworkingSpace();
        coworkingSpace.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        when(coworkingSpaceService.getCoworkingSpaceByID(1L)).thenReturn(Optional.of(coworkingSpace));

        Customer user = new Customer();
        user.setId(1L);

        String reservationName = "Test Reservation";
        LocalDate bookingDate = LocalDate.now().plusDays(1); // Tomorrow
        LocalDateTime startDateTime = LocalDateTime.now().plusHours(1); // 1 hour from now
        LocalDateTime endDateTime = startDateTime.plusHours(1); // 2 hours from now

        //partial mock
        ReservationServiceImpl reservationServiceSpy = spy(new ReservationServiceImpl());
        doNothing().when(reservationServiceSpy).addReservation(any(Reservation.class));

        boolean result = reservationServiceSpy.userAddReservation(
                1L, reservationName, bookingDate, startDateTime, endDateTime, user, coworkingSpaceService, reservationServiceSpy);

        assertTrue(result);
        verify(reservationServiceSpy, times(1)).addReservation(any(Reservation.class));  // Verifying that the addReservation method was called
    }
    @Test
    public void testUserAddReservationWithPastDate() {
        CoworkingSpaceServiceImpl coworkingSpaceService = mock(CoworkingSpaceServiceImpl.class);
        ReservationServiceImpl reservationService = new ReservationServiceImpl();

        CoworkingSpace coworkingSpace = new CoworkingSpace();
        coworkingSpace.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        when(coworkingSpaceService.getCoworkingSpaceByID(1L)).thenReturn(Optional.of(coworkingSpace));

        Customer user = new Customer();
        user.setId(1L);

        // Creating an object with a past booking date
        String reservationName = "Test Reservation";
        LocalDate bookingDate = LocalDate.now().minusDays(1); // Yesterday
        LocalDateTime startDateTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endDateTime = startDateTime.plusHours(1);

        // Executing the method and checking for the exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.userAddReservation(1L, reservationName, bookingDate, startDateTime, endDateTime, user, coworkingSpaceService, reservationService);
        });

        assertEquals("You cannot register a past date!", exception.getMessage()); // Verifying the exception message
    }

}
