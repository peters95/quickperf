/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2019-2019 the original author or authors.
 */

package org.quickperf.sql;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.results.PrintableResult;
import org.junit.runner.RunWith;
import org.quickperf.junit4.QuickPerfJUnitRunner;
import org.quickperf.jvm.allocation.AllocationUnit;
import org.quickperf.jvm.annotations.HeapSize;
import org.quickperf.sql.annotation.ExpectMaxSelectedColumn;
import org.quickperf.sql.annotation.ExpectSelectedColumn;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class SqlSelectedColumnsJUnit4Test {

    @RunWith(QuickPerfJUnitRunner.class)
    public static class AClassHavingAMethodAnnotatedWithExpectSelectedColumn extends SqlTestBaseJUnit4 {

        @ExpectSelectedColumn(2)
        @Test
        public void select_with_three_columns() {
            EntityManager em = emf.createEntityManager();
            Query query = em.createQuery("FROM " + Book.class.getCanonicalName());
            query.getResultList();
        }

    }

    @Test public void
    should_fail_if_the_max_number_of_selected_columns_is_not_this_expected() {

        // GIVEN
        Class<AClassHavingAMethodAnnotatedWithExpectSelectedColumn> testClass = AClassHavingAMethodAnnotatedWithExpectSelectedColumn.class;

        // WHEN
        PrintableResult printableResult = PrintableResult.testResult(testClass);

        // THEN
        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(printableResult.failureCount())
                      .isEqualTo(1);

        softAssertions.assertThat(printableResult.toString())
                      .contains("You may think that <2> columns were selected")
                      .contains("But in fact <3>...");

        softAssertions.assertAll();

    }

    @RunWith(QuickPerfJUnitRunner.class)
    public static class AClassHavingAMethodAnnotatedWithExpectMaxSelectedColumn extends SqlTestBaseJUnit4 {

        @ExpectMaxSelectedColumn(2)
        @Test
        public void select_with_three_columns() {
            EntityManager em = emf.createEntityManager();
            Query query = em.createQuery("FROM " + Book.class.getCanonicalName());
            query.getResultList();
        }

    }

    @Test public void
    should_fail_if_the_max_number_of_selected_columns_is_greater_than_expected() {

        // GIVEN
        Class<?> testClass = AClassHavingAMethodAnnotatedWithExpectMaxSelectedColumn.class;

        // WHEN
        PrintableResult printableResult = PrintableResult.testResult(testClass);

        // THEN
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(printableResult.failureCount())
                      .isEqualTo(1);
        softAssertions.assertThat(printableResult.toString())
                      .contains("Maximum expected number of selected columns <2> but is <3>.");
        softAssertions.assertAll();

    }

    @RunWith(QuickPerfJUnitRunner.class)
    public static class AClassHavingAMethodAnnotatedWithExpectMaxSelectedColumnAndWithHeapSize extends SqlTestBaseJUnit4 {

        @HeapSize(value = 20, unit = AllocationUnit.MEGA_BYTE)
        @ExpectMaxSelectedColumn(2)
        @Test
        public void select_with_three_columns() {
            EntityManager em = emf.createEntityManager();
            Query query = em.createQuery("FROM " + Book.class.getCanonicalName());
            query.getResultList();
        }

    }

    @Test public void
    should_fail_if_the_max_number_of_selected_columns_is_greater_than_expected_in_a_specific_jvm() {

        // GIVEN
        Class<?> testClass = AClassHavingAMethodAnnotatedWithExpectMaxSelectedColumnAndWithHeapSize.class;

        // WHEN
        PrintableResult printableResult = PrintableResult.testResult(testClass);

        // THEN
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(printableResult.failureCount())
                      .isEqualTo(1);
        softAssertions.assertThat(printableResult.toString())
                      .contains("Maximum expected number of selected columns <2> but is <3>.");
        softAssertions.assertAll();

    }

}