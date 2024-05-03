package com.retirement.apiservice;

import java.io.IOException;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import com.retirement.apiservice.entity.IncomeSource;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class IncomeSourceJsonTests {

    @Autowired
    private JacksonTester<IncomeSource[]> jsonTester;

    private IncomeSource[] incomeSourceList;

    @BeforeEach
    void setUp() {
        incomeSourceList = Arrays.array(
                new IncomeSource(1, 101, "Money Market Savings", 10000, 4.0, 12));
    }

    @Test
    void incomeSourceListSerializationTest() throws IOException {
        assertThat(jsonTester.write(incomeSourceList)).isStrictlyEqualToJson("income_source_list.json");
    }
}
