package com.aurea.sars.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class ActivityServiceTest {

    private ActivityRepository activityRepository = mock(ActivityRepository.class);
    private ActivityService activityService = new ActivityService(activityRepository);

    @Test
    void givenActivityWithValueWillRoundDownWhenGetTotalThenReturnTotal() {
        // Arrange
        final String key = "activity";
        List<Activity> activities = List
                .of(createActivity(1), createActivity(10), createActivity(3));
        when(activityRepository.findByKey(key)).thenReturn(activities);

        Integer total = activityService.getTotalByKey(key);

        assertEquals(14, total);
    }

    private static Activity createActivity(Integer value) {
        return new Activity("activity", value);
    }
}