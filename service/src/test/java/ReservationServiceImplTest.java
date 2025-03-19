import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.reservationapplication.model.*;
import org.reservationapplication.repository.ReservationRepository;
import org.reservationapplication.service.CoworkingSpaceServiceImpl;
import org.reservationapplication.service.ReservationServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceImplTest {


    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Test
    public void testGetAllReservation() {
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

        TreeSet<Reservation> reservations = new TreeSet<>();

        reservations.add(reservation1);
        reservations.add(reservation2);

        when(reservationRepository.read()).thenReturn(reservations);

        TreeSet<Reservation> allReservations = reservationService.getAllReservation();

        assertEquals(2, allReservations.size());
        assertTrue(allReservations.contains(reservation1));
        assertTrue(allReservations.contains(reservation2));
    }

    @Test
    public void testGetAllReservation_Empty() {
        ReservationServiceImpl reservationService = new ReservationServiceImpl(reservationRepository);

        when(reservationRepository.read()).thenReturn(new TreeSet<>());
        assertTrue(reservationService.getAllReservation().isEmpty());
    }

    @Test
    public void testGetPersonalReservation() {
        // Создаем экземпляр ReservationService
        ReservationServiceImpl reservationService = new ReservationServiceImpl(reservationRepository);

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

        User user = new Customer();
        user.setId(1L);

        TreeSet<Reservation> personalReservations = new TreeSet<>();
        personalReservations.add(reservation1);

        when(reservationRepository.readPersonalReservations(user.getId())).thenReturn(personalReservations);

        TreeSet<Reservation> result = reservationService.getPersonalReservation(user);

        assertEquals(1, result.size());
        assertTrue(result.contains(reservation1));
    }

    @Test
    public void testGetPersonalReservation_Empty() {
        // Создаем экземпляр ReservationService
        ReservationServiceImpl reservationService = new ReservationServiceImpl(reservationRepository);

        // Создаем резервации
        Reservation reservation1 = new Reservation();
        reservation1.setCoworkingSpaceID(1L);
        reservation1.setCustomerID(1L);
        reservation1.setStartDateTime(LocalDateTime.now().plusHours(1));
        reservation1.setEndDateTime(LocalDateTime.now().plusHours(2));
        User user = new Customer();
        user.setId(99L);

        when(reservationRepository.readPersonalReservations(user.getId())).thenReturn(new TreeSet<>());

        TreeSet<Reservation> result = reservationService.getPersonalReservation(user);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetReservationsByCoworkingSpaceAndDate() {
        ReservationServiceImpl reservationService = new ReservationServiceImpl(reservationRepository);

        LocalDate date = LocalDate.now();

        Reservation reservation1 = new Reservation();
        reservation1.setCoworkingSpaceID(1L);
        reservation1.setStartDateTime(LocalDateTime.now().plusHours(1));
        reservation1.setEndDateTime(LocalDateTime.now().plusHours(2));

        Reservation reservation2 = new Reservation();
        reservation2.setCoworkingSpaceID(1L);
        reservation2.setStartDateTime(LocalDateTime.now().plusHours(24));
        reservation2.setEndDateTime(LocalDateTime.now().plusHours(25));


        TreeSet<Reservation> reservations = new TreeSet<>();
        reservations.add(reservation1);
        reservations.add(reservation2);

        when(reservationRepository.read()).thenReturn(reservations);

        TreeSet<Reservation> result = reservationService.getReservationsByCoworkingSpaceAndDate(1L, date);

        assertNotNull(result);
        assertTrue(result.contains(reservation1));
        assertFalse(result.contains(reservation2));
    }

    @Test
    public void testRemoveReservationById_Success() {
        ReservationServiceImpl reservationService = new ReservationServiceImpl(reservationRepository);

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

        long reservationId = 1L;
        doNothing().when(reservationRepository).makeUnactive(reservationId);

        reservationService.removeReservationById(reservationId);

        verify(reservationRepository, times(1)).makeUnactive(reservationId);
    }

    @Test
    public void testAddReservation() {
        ReservationServiceImpl reservationService = new ReservationServiceImpl(reservationRepository);

        Reservation reservation = new Reservation();
        reservation.setCoworkingSpaceID(1L);
        reservation.setCustomerID(1L);
        reservation.setStartDateTime(LocalDateTime.now());
        reservation.setEndDateTime(LocalDateTime.now().plusHours(2));

        doNothing().when(reservationRepository).create(any(Reservation.class));
        reservationService.addReservation(reservation);

        TreeSet<Reservation> reservations = new TreeSet<>();
        reservations.add(reservation);
        when(reservationRepository.read()).thenReturn(reservations);

        TreeSet<Reservation> allReservations = reservationService.getAllReservation();

        assertEquals(1, allReservations.size());
        assertTrue(allReservations.contains(reservation));
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
        ReservationServiceImpl reservationServiceSpy = spy(new ReservationServiceImpl(reservationRepository));
        doNothing().when(reservationServiceSpy).addReservation(any(Reservation.class));

        boolean result = reservationServiceSpy.userAddReservation(
                1L, reservationName, bookingDate, startDateTime, endDateTime, user, coworkingSpaceService, reservationServiceSpy);

        assertTrue(result);
        verify(reservationServiceSpy, times(1)).addReservation(any(Reservation.class));  // Verifying that the addReservation method was called
    }
    @Test
    public void testUserAddReservationWithPastDate() {
        CoworkingSpaceServiceImpl coworkingSpaceService = mock(CoworkingSpaceServiceImpl.class);
        ReservationServiceImpl reservationService = new ReservationServiceImpl(reservationRepository);

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
