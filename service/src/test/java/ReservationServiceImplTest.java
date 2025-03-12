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
