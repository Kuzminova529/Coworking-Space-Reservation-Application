//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.reservationapplication.model.AvailabilityStatus;
//import org.reservationapplication.domain.model.CoworkingSpace;
//import org.reservationapplication.domain.repository.JDBCRepos.CoworkingSpaceRepositoryJDBC;
//import org.reservationapplication.service.CacheServiceCoworkingSpace;
//import org.reservationapplication.service.CoworkingSpaceServiceImpl;
//
//import java.util.Arrays;
//import java.util.List;
//
//@ExtendWith(MockitoExtension.class)
//public class CoworkingSpaceServiceImplTest {
//
//    @Mock
//    private CoworkingSpaceRepositoryJDBC coworkingSpaceRepositoryJDBC;
//
//    @Mock
//    private CacheServiceCoworkingSpace cacheServiceCoworkingSpace;
//
//    @InjectMocks
//    private CoworkingSpaceServiceImpl coworkingSpaceService;
//
//    @Test
//    public void testGetAllCoworkingSpace() {
//        CoworkingSpace space1 = new CoworkingSpace();
//        space1.setID(1L);
//        CoworkingSpace space2 = new CoworkingSpace();
//        space2.setID(2L);
//
//        List<CoworkingSpace> coworkingSpaces = Arrays.asList(space1, space2);
//
//        when(cacheServiceCoworkingSpace.getAllCoworkingSpaces()).thenReturn(coworkingSpaces);
//
//        List<CoworkingSpace> result = coworkingSpaceService.getAllCoworkingSpace();
//
//        assertNotNull(result);
//        assertEquals(2, result.size());
//        assertTrue(result.contains(space1));
//        assertTrue(result.contains(space2));
//
//        verify(cacheServiceCoworkingSpace, times(1)).getAllCoworkingSpaces();
//    }
//
//    @Test
//    void testAddCoworkingSpace() {
//        CoworkingSpace coworkingSpace = new CoworkingSpace();
//        coworkingSpace.setID(1L); // Устанавливаем ID
//
//        coworkingSpaceService.addCoworkingSpace(coworkingSpace);
//
//        // Check that the addCoworkingSpace method was called on cacheServiceCoworkingSpace with the correct object
//        verify(cacheServiceCoworkingSpace, times(1)).addCoworkingSpace(coworkingSpace);
//    }
//
//    @Test
//    void testRemoveCoworkingSpace() {
//        long coworkingSpaceId = 1L;
//
//        coworkingSpaceService.removeCoworkingSpace(coworkingSpaceId);
//
//        // Check that the removeCoworkingSpaceByID method was called with the correct id
//        verify(cacheServiceCoworkingSpace, times(1)).removeCoworkingSpaceByID(coworkingSpaceId);
//    }
//
//    @Test
//    public void testGetActiveCoworkingSpace() {
//        CoworkingSpace space1 = new CoworkingSpace();
//        space1.setID(1L);
//        space1.setActive(AvailabilityStatus.AVAILABLE);
//
//        CoworkingSpace space2 = new CoworkingSpace();
//        space2.setID(2L);
//        space2.setActive(AvailabilityStatus.UNAVAILABLE);
//
//        CoworkingSpace space3 = new CoworkingSpace();
//        space3.setID(3L);
//        space3.setActive(AvailabilityStatus.AVAILABLE);
//
//        List<CoworkingSpace> coworkingSpaces = Arrays.asList(space1, space2, space3);
//
//        when(cacheServiceCoworkingSpace.getAllCoworkingSpaces()).thenReturn(coworkingSpaces);
//
//        List<CoworkingSpace> availableSpaces = coworkingSpaceService.getAvailableCoworkingSpace();
//
//        assertNotNull(availableSpaces);
//        assertEquals(2, availableSpaces.size());  // There must be 2 free spaces
//        assertTrue(availableSpaces.contains(space1));  // spase1 must be available
//        assertTrue(availableSpaces.contains(space3));  // spase3 must be available
//        assertFalse(availableSpaces.contains(space2));  // spase2 must be unavailable
//
//        verify(cacheServiceCoworkingSpace, times(1)).getAllCoworkingSpaces();
//    }
//
//    @Test
//    public void testGetActiveCoworkingSpaceWhenNoSpaces() {
//
//        when(cacheServiceCoworkingSpace.getAllCoworkingSpaces()).thenReturn(Arrays.asList());
//
//        List<CoworkingSpace> availableSpaces = coworkingSpaceService.getAvailableCoworkingSpace();
//
//        assertNotNull(availableSpaces);
//        assertTrue(availableSpaces.isEmpty());
//
//        verify(cacheServiceCoworkingSpace, times(1)).getAllCoworkingSpaces();
//    }
//
//    @Test
//    public void testGetActiveCoworkingSpaceWhenNull() {
//
//        when(cacheServiceCoworkingSpace.getAllCoworkingSpaces()).thenReturn(null);
//
//        List<CoworkingSpace> availableSpaces = coworkingSpaceService.getAvailableCoworkingSpace();
//
//        assertNotNull(availableSpaces);//there must be list
//        assertTrue(availableSpaces.isEmpty());//but empty
//
//        verify(cacheServiceCoworkingSpace, times(1)).getAllCoworkingSpaces();
//    }
//}
//
//
