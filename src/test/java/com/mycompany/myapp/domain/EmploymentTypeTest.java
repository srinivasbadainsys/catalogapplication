package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmploymentTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmploymentType.class);
        EmploymentType employmentType1 = new EmploymentType();
        employmentType1.setId(1L);
        EmploymentType employmentType2 = new EmploymentType();
        employmentType2.setId(employmentType1.getId());
        assertThat(employmentType1).isEqualTo(employmentType2);
        employmentType2.setId(2L);
        assertThat(employmentType1).isNotEqualTo(employmentType2);
        employmentType1.setId(null);
        assertThat(employmentType1).isNotEqualTo(employmentType2);
    }
}
