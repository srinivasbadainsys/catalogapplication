package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompPlanPayrollTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompPlanPayrollType.class);
        CompPlanPayrollType compPlanPayrollType1 = new CompPlanPayrollType();
        compPlanPayrollType1.setId(1L);
        CompPlanPayrollType compPlanPayrollType2 = new CompPlanPayrollType();
        compPlanPayrollType2.setId(compPlanPayrollType1.getId());
        assertThat(compPlanPayrollType1).isEqualTo(compPlanPayrollType2);
        compPlanPayrollType2.setId(2L);
        assertThat(compPlanPayrollType1).isNotEqualTo(compPlanPayrollType2);
        compPlanPayrollType1.setId(null);
        assertThat(compPlanPayrollType1).isNotEqualTo(compPlanPayrollType2);
    }
}
