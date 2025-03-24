import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.reservationapplication.domain.model.CoworkingSpace;
import org.reservationapplication.domain.model.CoworkingSpaceType;
import org.reservationapplication.domain.model.Customer;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.reservationapplication.service.CoworkingSpaceServiceImpl;
import org.reservationapplication.service.MenuService;
import org.reservationapplication.service.ReservationServiceImpl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Test
    public void testAddCoworkingSpace_Success() {
        CoworkingSpaceServiceImpl coworkingSpaceService = mock(CoworkingSpaceServiceImpl.class);

        MenuService menuService = new MenuService();

        int typeChoice = 1; // OPENSPACE
        double price = 500.0;
        int availabilityChoice = 1; // AVAILABLE

        menuService.addCoworkingSpace(coworkingSpaceService, typeChoice, price, availabilityChoice);

        // Check that the addCoworkingSpace method was called by the corresponding object
        ArgumentCaptor<CoworkingSpace> captor = ArgumentCaptor.forClass(CoworkingSpace.class);
        verify(coworkingSpaceService).addCoworkingSpace(captor.capture());

        CoworkingSpace capturedSpace = captor.getValue();

        // Checking that coworking was created correctly
        assertNotNull(capturedSpace);
        assertEquals(CoworkingSpaceType.OPENSPACE, capturedSpace.getType());
        assertEquals(500.0, capturedSpace.getPrice());
        assertEquals(AvailabilityStatus.AVAILABLE, capturedSpace.getActive());
    }

    @Test
    public void testAddCoworkingSpace_FailureType() {
        CoworkingSpaceServiceImpl coworkingSpaceService = mock(CoworkingSpaceServiceImpl.class);

        MenuService menuService = new MenuService();

        int typeChoice = 9; // Wrong input
        double price = 500.0;
        int availabilityChoice = 1; // AVAILABLE

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            menuService.addCoworkingSpace(coworkingSpaceService, typeChoice, price, availabilityChoice);        });

        assertEquals("Invalid coworking space type choice: " + typeChoice, exception.getMessage()); // Verifying the exception message

    }

    @Test
    public void testAddCoworkingSpace_FailureAvailability() {
        CoworkingSpaceServiceImpl coworkingSpaceService = mock(CoworkingSpaceServiceImpl.class);

        MenuService menuService = new MenuService();

        int typeChoice = 1; // OPENSPACE
        double price = 500.0;
        int availabilityChoice = 9; // Wrong input

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            menuService.addCoworkingSpace(coworkingSpaceService, typeChoice, price, availabilityChoice);        });

        assertEquals("Invalid availability choice: " + availabilityChoice, exception.getMessage()); // Verifying the exception message

    }

    @Test
    public void testGetAllCoworkingSpaces() {
        CoworkingSpaceServiceImpl coworkingSpaceService = mock(CoworkingSpaceServiceImpl.class);
        MenuService menuService = new MenuService();

        CoworkingSpace space1 = new CoworkingSpace();
        CoworkingSpace space2 = new CoworkingSpace();

        List<CoworkingSpace> list = Arrays.asList(space1, space2);
        when(coworkingSpaceService.getAllCoworkingSpace()).thenReturn(list);

        List<CoworkingSpace> result = menuService.getAllCoworkingSpaces(coworkingSpaceService);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(space1));
        assertTrue(result.contains(space2));
    }

    @Test
    public void testRemoveCoworkingSpace() {
        CoworkingSpaceServiceImpl coworkingSpaceService = mock(CoworkingSpaceServiceImpl.class);
        MenuService menuService = new MenuService();

        long coworkingSpaceId = 1L;

        menuService.removeCoworkingSpace(coworkingSpaceService, coworkingSpaceId);

        // Checking that the removeCoworkingSpace method was called with the correct id
        verify(coworkingSpaceService).removeCoworkingSpace(coworkingSpaceId);
    }
    @Test
    public void testMakeReservation_Success() {
        ReservationServiceImpl reservationService = mock(ReservationServiceImpl.class);
        CoworkingSpaceServiceImpl coworkingSpaceService = mock(CoworkingSpaceServiceImpl.class);
        Customer customer = mock(Customer.class);
        MenuService menuService = new MenuService();

        long coworkingSpaceId = 1L;
        String reservationName = "Meeting Room";
        LocalDate bookingDate = LocalDate.now();
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(11, 0);

        // If userAddReservation is called return true
        when(reservationService.userAddReservation(anyLong(), anyString(), any(), any(), any(), any(), any(), any()))
                .thenReturn(true);

        boolean result = menuService.makeReservation(customer, coworkingSpaceService, reservationService,
                coworkingSpaceId, reservationName, bookingDate, startTime, endTime);

        assertTrue(result); // Checking that the booking was successful
        verify(reservationService).userAddReservation(anyLong(), anyString(), any(), any(), any(), any(), any(), any());
    }
}
