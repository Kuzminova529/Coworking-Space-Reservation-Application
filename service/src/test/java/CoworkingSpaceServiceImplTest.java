import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.reservationapplication.domain.model.CoworkingSpace;
import org.reservationapplication.domain.repository.CacheCoworkingSpaceRepository;
import org.reservationapplication.service.CoworkingSpaceServiceImpl;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CoworkingSpaceServiceImplTest {

    @Mock
    private CacheCoworkingSpaceRepository cacheCoworkingSpaceRepository;

    @InjectMocks
    private CoworkingSpaceServiceImpl coworkingSpaceService;

    @Test
    public void testGetAllCoworkingSpace() {
        CoworkingSpace space1 = new CoworkingSpace();
        space1.setId(1L);
        CoworkingSpace space2 = new CoworkingSpace();
        space2.setId(2L);

        List<CoworkingSpace> coworkingSpaces = Arrays.asList(space1, space2);

        when(cacheCoworkingSpaceRepository.findAll()).thenReturn(coworkingSpaces);

        List<CoworkingSpace> result = coworkingSpaceService.getAllCoworkingSpace();

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(cacheCoworkingSpaceRepository, times(1)).findAll();
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

        when(cacheCoworkingSpaceRepository.findAll()).thenReturn(coworkingSpaces);

        List<CoworkingSpace> availableSpaces = coworkingSpaceService.getActiveCoworkingSpace();

        assertNotNull(availableSpaces);
        assertEquals(2, availableSpaces.size());  // There must be 2 free spaces

        verify(cacheCoworkingSpaceRepository, times(1)).findAll();
    }

    @Test
    public void testGetActiveCoworkingSpaceWhenNoSpaces() {

        when(cacheCoworkingSpaceRepository.findAll()).thenReturn(Arrays.asList());

        List<CoworkingSpace> availableSpaces = coworkingSpaceService.getActiveCoworkingSpace();

        assertNotNull(availableSpaces);
        assertTrue(availableSpaces.isEmpty());

        verify(cacheCoworkingSpaceRepository, times(1)).findAll();
    }

    @Test
    public void testGetActiveCoworkingSpaceWhenNull() {

        when(cacheCoworkingSpaceRepository.findAll()).thenReturn(null);

        List<CoworkingSpace> availableSpaces = coworkingSpaceService.getActiveCoworkingSpace();

        assertNotNull(availableSpaces);//there must be list
        assertTrue(availableSpaces.isEmpty());//but empty

        verify(cacheCoworkingSpaceRepository, times(1)).findAll();
    }
}


