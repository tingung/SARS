package com.aurea.sars.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.google.common.cache.LoadingCache;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.junit.jupiter.api.Test;

class ActivityRepositoryMemoryCacheImplTest {

    private ActivityRepositoryMemoryCacheImpl activityRepositoryMemoryCacheImpl = spy(new ActivityRepositoryMemoryCacheImpl());

    @Test
    void givenActivityWhenSaveThenReturnActivity() {
        // Arrange
        final Activity activity = new Activity("activity3", 1);
        final Activity expected = new Activity("activity3", 1);
        final Long id = Long.valueOf(1);
        expected.setId(id);
        doReturn(id).when(activityRepositoryMemoryCacheImpl).getId();

        // Act
        activityRepositoryMemoryCacheImpl.save(activity);

        // Assert
        assertEquals(expected, activity);
    }

    @Test
    void givenExistingKeyFoundWhenFindByIdThenReturnList() {
        // Arrange
        final String key = "activity2";
        LoadingCache<Long, Activity> cache = mock(LoadingCache.class);
        when(activityRepositoryMemoryCacheImpl.getCache(key)).thenReturn(cache);
        ConcurrentMap<Long, Activity> values = new ConcurrentHashMap<>();
        values.put(Long.valueOf(1), new Activity(key, 1));
        when(cache.asMap()).thenReturn(values);

        // Act
        List<Activity> activities = activityRepositoryMemoryCacheImpl.findByKey(key);

        // Assert
        assertTrue(!activities.isEmpty());
    }

    @Test
    void givenKeyNotFoundWhenFindByIdThenReturnEmptyList() {
        // Arrange
        final String key = "activity1";

        // Act
        List<Activity> activities = activityRepositoryMemoryCacheImpl.findByKey(key);

        // Assert
        assertTrue(activities.isEmpty());
    }

    @Test
    void givenNoExistingCacheWhenGetCacheThenReturnNewCache() {
        final String key = "plan";

        assertNotNull(activityRepositoryMemoryCacheImpl.getCache(key));
    }

    @Test
    void givenExistingCacheWhenGetCacheThenReturnExistingCache() {
        final String key = "plan1";
        LoadingCache<Long, Activity> cache = activityRepositoryMemoryCacheImpl.getCache(key);

        assertEquals(cache, activityRepositoryMemoryCacheImpl.getCache(key));
    }

    @Test
    void givenWaitUntilCacheExpiredWhenGetActivityThenReturnEmpty() throws InterruptedException {
        int timeout = 1;
        doReturn(Long.valueOf(timeout)).when(activityRepositoryMemoryCacheImpl).getTimeToExpired();
        final String key = "activity";
        Activity activity = new Activity(key, 1);
        activityRepositoryMemoryCacheImpl.save(activity);

        final List<Activity> activities = activityRepositoryMemoryCacheImpl.findByKey(key);
        // sleep to wait for cache expired
        Thread.sleep(timeout * 6000);
        final List<Activity> activitiesAfterCacheExpired = activityRepositoryMemoryCacheImpl.findByKey(key);

        assertFalse(activities.isEmpty());
        assertTrue(activitiesAfterCacheExpired.isEmpty());
    }
}