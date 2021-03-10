package com.aurea.sars.activity;

import lombok.Data;

@Data
public class Activity {

    private Long id;
    private String key;
    private Integer value;

    public Activity(String key, Integer value) {
        this.key = key;
        this.value = value;
    }
}
