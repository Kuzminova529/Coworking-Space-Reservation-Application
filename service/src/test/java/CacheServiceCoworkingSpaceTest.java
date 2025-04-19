import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reservationapplication.domain.model.CoworkingSpace;
import org.reservationapplication.domain.repository.SpringDataJPARepos.CoworkingSpaceRepositorySpring;
import org.reservationapplication.domain.repository.CacheServiceCoworkingSpace;

import java.util.Arrays;
import java.util.List;

class CacheServiceCoworkingSpaceTest {

    private CoworkingSpaceRepositorySpring repository;
    private CacheServiceCoworkingSpace cacheService;

    @BeforeEach
    void setUp() {
        repository = mock(CoworkingSpaceRepositorySpring.class);
        cacheService = new CacheServiceCoworkingSpace(repository);
    }

    @Test
    void testFindAll_CacheMiss() {
        List<CoworkingSpace> spaces = Arrays.asList(new CoworkingSpace(), new CoworkingSpace());
        when(repository.getCoworkingSpaces()).thenReturn(spaces);

        List<CoworkingSpace> result = cacheService.findAll();

        assertEquals(spaces, result);
        verify(repository, times(1)).getCoworkingSpaces();
    }

    @Test
    void testFindAll_CacheHit() {
        List<CoworkingSpace> spaces = Arrays.asList(new CoworkingSpace(), new CoworkingSpace());
        when(repository.getCoworkingSpaces()).thenReturn(spaces);

        // First call is a cache miss
        cacheService.findAll();

        // Second call - cache hit
        List<CoworkingSpace> result = cacheService.findAll();

        assertEquals(spaces, result);
        verify(repository, times(1)).getCoworkingSpaces();
    }

    @Test
    void testSave() {
        CoworkingSpace space = new CoworkingSpace();

        cacheService.save(space);

        verify(repository, times(1)).save(space);
        assertNull(cacheService.getCache().getIfPresent("coworkings"));
    }

    @Test
    void testRemoveCoworkingSpaceByID() {
        long id = 1L;

        cacheService.updateStatus(id);

        verify(repository, times(1)).updateStatus(id);
        assertNull(cacheService.getCache().getIfPresent("coworkings"));
    }
}
