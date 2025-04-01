import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.reservationapplication.domain.dto.CoworkingSpaceDto;
import org.reservationapplication.domain.model.CoworkingSpace;
import org.reservationapplication.domain.repository.JPARepos.CoworkingSpaceRepositoryJPA;
import org.reservationapplication.service.CacheServiceCoworkingSpace;
import org.reservationapplication.service.CoworkingSpaceServiceImpl;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CoworkingSpaceServiceImplTest {

    @Mock
    private CacheServiceCoworkingSpace cacheServiceCoworkingSpace;

    @InjectMocks
    private CoworkingSpaceServiceImpl coworkingSpaceService;

    @Test
    public void testGetAllCoworkingSpace() {
        CoworkingSpace space1 = new CoworkingSpace();
        space1.setId(1L);
        CoworkingSpace space2 = new CoworkingSpace();
        space2.setId(2L);

        List<CoworkingSpace> coworkingSpaces = Arrays.asList(space1, space2);

        when(cacheServiceCoworkingSpace.getAllCoworkingSpaces()).thenReturn(coworkingSpaces);

        List<CoworkingSpaceDto> result = coworkingSpaceService.getAllCoworkingSpace();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(space1));
        assertTrue(result.contains(space2));

        verify(cacheServiceCoworkingSpace, times(1)).getAllCoworkingSpaces();
    }

    @Test
    void testRemoveCoworkingSpaceById() {
        long coworkingSpaceId = 1L;

        coworkingSpaceService.removeCoworkingSpaceById(coworkingSpaceId);

        // Check that the removeCoworkingSpaceByID method was called with the correct id
        verify(cacheServiceCoworkingSpace, times(1)).removeCoworkingSpaceByID(coworkingSpaceId);
    }

    @Test
    public void testGetActiveCoworkingSpace() {
        CoworkingSpace space1 = new CoworkingSpace();
        space1.setActive(true);

        CoworkingSpace space2 = new CoworkingSpace();
        space2.setActive(false);

        CoworkingSpace space3 = new CoworkingSpace();
        space3.setActive(true);

        List<CoworkingSpace> coworkingSpaces = Arrays.asList(space1, space2, space3);

        when(cacheServiceCoworkingSpace.getAllCoworkingSpaces()).thenReturn(coworkingSpaces);

        List<CoworkingSpaceDto> availableSpaces = coworkingSpaceService.getActiveCoworkingSpace();

        assertNotNull(availableSpaces);
        assertEquals(2, availableSpaces.size());  // There must be 2 free spaces

        verify(cacheServiceCoworkingSpace, times(1)).getAllCoworkingSpaces();
    }

    @Test
    public void testGetActiveCoworkingSpaceWhenNoSpaces() {

        when(cacheServiceCoworkingSpace.getAllCoworkingSpaces()).thenReturn(Arrays.asList());

        List<CoworkingSpaceDto> availableSpaces = coworkingSpaceService.getActiveCoworkingSpace();

        assertNotNull(availableSpaces);
        assertTrue(availableSpaces.isEmpty());

        verify(cacheServiceCoworkingSpace, times(1)).getAllCoworkingSpaces();
    }

    @Test
    public void testGetActiveCoworkingSpaceWhenNull() {

        when(cacheServiceCoworkingSpace.getAllCoworkingSpaces()).thenReturn(null);

        List<CoworkingSpaceDto> availableSpaces = coworkingSpaceService.getActiveCoworkingSpace();

        assertNotNull(availableSpaces);//there must be list
        assertTrue(availableSpaces.isEmpty());//but empty

        verify(cacheServiceCoworkingSpace, times(1)).getAllCoworkingSpaces();
    }
}


