package com.aurea.sars.activity;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class ActivityRepositoryMemoryCacheImpl implements ActivityRepository {

    // Time to expire in seconds - 12 hours
    private static final long TIME_TO_EXPIRED = 12 * 60 * 60;
    private static final Map<String, LoadingCache> ACTIVITIES = new HashMap<>();
    private static final AtomicLong ID = new AtomicLong(1);

    @Override public Activity save(Activity activity) {
        activity.setId(getId());
        getCache(activity.getKey()).put(activity.getId(), activity);
        return activity;
    }

    @Override public List<Activity> findByKey(String key) {
        return new ArrayList<Activity>(getCache(key).asMap().values());
    }

    @VisibleForTesting
    synchronized LoadingCache<Long, Activity> getCache(String key) {
        final Map<String, LoadingCache> activities = getActivities();
        if (activities.containsKey(key)) {
            return activities.get(key);
        }

        final CacheLoader<Long, Activity> loader = new CacheLoader<Long, Activity>() {
            @Override public Activity load(Long key) throws Exception {
                return null;
            }
        };
        final LoadingCache<Long, Activity> cache = CacheBuilder.newBuilder()
                .expireAfterWrite(getTimeToExpired(), TimeUnit.SECONDS)
                .build(loader);
        activities.put(key, cache);
        return cache;
    }

    synchronized long getId() {
        return ID.getAndIncrement();
    }

    @VisibleForTesting
    long getTimeToExpired() {
        return TIME_TO_EXPIRED;
    }

    @VisibleForTesting
    Map<String, LoadingCache> getActivities() {
        return ACTIVITIES;
    }

}
