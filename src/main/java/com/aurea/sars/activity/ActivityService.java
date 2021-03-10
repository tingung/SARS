package com.aurea.sars.activity;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;

    @Autowired
    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public Activity create(Activity activity) {
        return activityRepository.save(activity);
    }

    public Integer getTotalByKey(String key) {
        List<Activity> activities = activityRepository.findByKey(key);
        Integer total = 0;
        for(Activity activity : activities) {
            total += activity.getValue();
        }
        return total;
    }
}
