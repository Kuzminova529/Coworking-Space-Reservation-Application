import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.reservationapplication.model.AvailabilityStatus;
import org.reservationapplication.model.CoworkingSpace;
import org.reservationapplication.repository.oldRepos.CoworkingSpaceRepository;
import org.reservationapplication.service.CacheServiceCoworkingSpace;
import org.reservationapplication.service.CoworkingSpaceServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CoworkingSpaceServiceImplTest {

    @Mock
    private CoworkingSpaceRepository coworkingSpaceRepository;

    @Mock
    private CacheServiceCoworkingSpace cacheServiceCoworkingSpace;

    @InjectMocks
    private CoworkingSpaceServiceImpl coworkingSpaceService;

    @Test
    public void testGetCoworkingSpaceByID() {
        CoworkingSpace space = new CoworkingSpace();
        space.setID(1L);

        //mock (coworkingSpaceRepository) behavior settings
        when(coworkingSpaceRepository.getById(1L)).thenReturn(Optional.of(space));

        Optional<CoworkingSpace> result = coworkingSpaceService.getCoworkingSpaceByID(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getID());
        verify(coworkingSpaceRepository, times(1)).getById(1L);
    }

    @Test
    public void testGetAllCoworkingSpace() {
        CoworkingSpace space1 = new CoworkingSpace();
        space1.setID(1L);
        CoworkingSpace space2 = new CoworkingSpace();
        space2.setID(2L);

        List<CoworkingSpace> coworkingSpaces = Arrays.asList(space1, space2);

        when(cacheServiceCoworkingSpace.getAllCoworkingSpaces()).thenReturn(coworkingSpaces);

        List<CoworkingSpace> result = coworkingSpaceService.getAllCoworkingSpace();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(space1));
        assertTrue(result.contains(space2));

        verify(cacheServiceCoworkingSpace, times(1)).getAllCoworkingSpaces();
    }

    @Test
    void testAddCoworkingSpace() {
        CoworkingSpace coworkingSpace = new CoworkingSpace();
        coworkingSpace.setID(1L); // Устанавливаем ID

        coworkingSpaceService.addCoworkingSpace(coworkingSpace);

        // Check that the addCoworkingSpace method was called on cacheServiceCoworkingSpace with the correct object
        verify(cacheServiceCoworkingSpace, times(1)).addCoworkingSpace(coworkingSpace);
    }

    @Test
    void testRemoveCoworkingSpace() {
        long coworkingSpaceId = 1L;

        coworkingSpaceService.removeCoworkingSpace(coworkingSpaceId);

        // Check that the removeCoworkingSpaceByID method was called with the correct id
        verify(cacheServiceCoworkingSpace, times(1)).removeCoworkingSpaceByID(coworkingSpaceId);
    }

    @Test
    public void testGetAvailableCoworkingSpace() {
        CoworkingSpace space1 = new CoworkingSpace();
        space1.setID(1L);
        space1.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);

        CoworkingSpace space2 = new CoworkingSpace();
        space2.setID(2L);
        space2.setAvailabilityStatus(AvailabilityStatus.UNAVAILABLE);

        CoworkingSpace space3 = new CoworkingSpace();
        space3.setID(3L);
        space3.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);

        List<CoworkingSpace> coworkingSpaces = Arrays.asList(space1, space2, space3);

        when(cacheServiceCoworkingSpace.getAllCoworkingSpaces()).thenReturn(coworkingSpaces);

        List<CoworkingSpace> availableSpaces = coworkingSpaceService.getAvailableCoworkingSpace();

        assertNotNull(availableSpaces);
        assertEquals(2, availableSpaces.size());  // There must be 2 free spaces
        assertTrue(availableSpaces.contains(space1));  // spase1 must be available
        assertTrue(availableSpaces.contains(space3));  // spase3 must be available
        assertFalse(availableSpaces.contains(space2));  // spase2 must be unavailable

        verify(cacheServiceCoworkingSpace, times(1)).getAllCoworkingSpaces();
    }

    @Test
    public void testGetAvailableCoworkingSpaceWhenNoSpaces() {

        when(cacheServiceCoworkingSpace.getAllCoworkingSpaces()).thenReturn(Arrays.asList());

        List<CoworkingSpace> availableSpaces = coworkingSpaceService.getAvailableCoworkingSpace();

        assertNotNull(availableSpaces);
        assertTrue(availableSpaces.isEmpty());

        verify(cacheServiceCoworkingSpace, times(1)).getAllCoworkingSpaces();
    }

    @Test
    public void testGetAvailableCoworkingSpaceWhenNull() {

        when(cacheServiceCoworkingSpace.getAllCoworkingSpaces()).thenReturn(null);

        List<CoworkingSpace> availableSpaces = coworkingSpaceService.getAvailableCoworkingSpace();

        assertNotNull(availableSpaces);//there must be list
        assertTrue(availableSpaces.isEmpty());//but empty

        verify(cacheServiceCoworkingSpace, times(1)).getAllCoworkingSpaces();
    }
}


