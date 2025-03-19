import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reservationapplication.model.CoworkingSpace;
import org.reservationapplication.repository.JPARepos.CoworkingSpaceRepositoryJPA;
import org.reservationapplication.repository.oldRepos.CoworkingSpaceRepository;
import org.reservationapplication.service.CacheServiceCoworkingSpace;

import java.util.Arrays;
import java.util.List;

class CacheServiceCoworkingSpaceTest {

    private CoworkingSpaceRepositoryJPA repository;
    private CacheServiceCoworkingSpace cacheService;

    @BeforeEach
    void setUp() {
        repository = mock(CoworkingSpaceRepositoryJPA.class);
        cacheService = new CacheServiceCoworkingSpace(repository);
    }

    @Test
    void testGetAllCoworkingSpaces_CacheMiss() {
        List<CoworkingSpace> spaces = Arrays.asList(new CoworkingSpace(), new CoworkingSpace());
        when(repository.read()).thenReturn(spaces);

        List<CoworkingSpace> result = cacheService.getAllCoworkingSpaces();

        assertEquals(spaces, result);
        verify(repository, times(1)).read();
    }

    @Test
    void testGetAllCoworkingSpaces_CacheHit() {
        List<CoworkingSpace> spaces = Arrays.asList(new CoworkingSpace(), new CoworkingSpace());
        when(repository.read()).thenReturn(spaces);

        // First call is a cache miss
        cacheService.getAllCoworkingSpaces();

        // Second call - cache hit
        List<CoworkingSpace> result = cacheService.getAllCoworkingSpaces();

        assertEquals(spaces, result);
        verify(repository, times(1)).read();
    }

    @Test
    void testAddCoworkingSpace() {
        CoworkingSpace space = new CoworkingSpace();

        cacheService.addCoworkingSpace(space);

        verify(repository, times(1)).create(space);
        assertNull(cacheService.getCache().getIfPresent("coworkings"));
    }

    @Test
    void testRemoveCoworkingSpaceByID() {
        long id = 1L;

        cacheService.removeCoworkingSpaceByID(id);

        verify(repository, times(1)).makeUnavailable(id);
        assertNull(cacheService.getCache().getIfPresent("coworkings"));
    }

    @Test
    void testRemoveAllCoworkingSpaces() {
        cacheService.removeAllCoworkingSpaces();

        verify(repository, times(1)).deleteAll();
        assertTrue(cacheService.getCache().asMap().isEmpty());
    }
}
