package com.aurea.sars.activity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ActivityRepository {

    Activity save(Activity activity);
    List<Activity> findByKey(String key);
}
