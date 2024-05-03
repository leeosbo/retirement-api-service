package com.retirement.apiservice;

import java.io.IOException;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import com.retirement.apiservice.entity.Expense;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ExpenseJsonTests {

    @Autowired
    private JacksonTester<Expense[]> jsonTester;

    private Expense[] expenseList;

    @BeforeEach
    void setUp() {
        expenseList = Arrays.array(
                new Expense(1, 101, "Electricity", 200, 12));
    }

    @Test
    void incomeSourceListSerializationTest() throws IOException {
        assertThat(jsonTester.write(expenseList)).isStrictlyEqualToJson("expense_list.json");
    }
}
