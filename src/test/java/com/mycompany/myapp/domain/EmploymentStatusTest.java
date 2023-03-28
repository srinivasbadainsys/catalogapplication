package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmploymentStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmploymentStatus.class);
        EmploymentStatus employmentStatus1 = new EmploymentStatus();
        employmentStatus1.setId(1L);
        EmploymentStatus employmentStatus2 = new EmploymentStatus();
        employmentStatus2.setId(employmentStatus1.getId());
        assertThat(employmentStatus1).isEqualTo(employmentStatus2);
        employmentStatus2.setId(2L);
        assertThat(employmentStatus1).isNotEqualTo(employmentStatus2);
        employmentStatus1.setId(null);
        assertThat(employmentStatus1).isNotEqualTo(employmentStatus2);
    }
}
