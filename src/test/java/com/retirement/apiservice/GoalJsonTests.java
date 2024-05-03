package com.retirement.apiservice;

import java.io.IOException;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import com.retirement.apiservice.entity.Goal;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class GoalJsonTests {

    @Autowired
    private JacksonTester<Goal[]> jsonTester;

    private Goal[] goalList;

    @BeforeEach
    void setUp() {
        goalList = Arrays.array(
                new Goal(1, 101, "Primary Goal", 2000, 12, true));
    }

    @Test
    void incomeSourceListSerializationTest() throws IOException {
        assertThat(jsonTester.write(goalList)).isStrictlyEqualToJson("goal_list.json");
    }
}
