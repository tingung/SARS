package com.aurea.sars.activity;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping("/activity/{key}")
    public ResponseEntity<ActivityResponse> create(@PathVariable String key, @RequestBody ActivityDto activityDto) {
        Activity activity = new Activity(key, (int) Math.round(activityDto.getValue()));
        activityService.create(activity);
        return ResponseEntity.ok(new ActivityResponse());
    }

    @RequestMapping("/activity/{key}/total")
    public ActivityTotal getTotalByKey(@PathVariable String key) {
        return new ActivityTotal(activityService.getTotalByKey(key));
    }
}
