package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacilityOrganizationalUnitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacilityOrganizationalUnit.class);
        FacilityOrganizationalUnit facilityOrganizationalUnit1 = new FacilityOrganizationalUnit();
        facilityOrganizationalUnit1.setId(1L);
        FacilityOrganizationalUnit facilityOrganizationalUnit2 = new FacilityOrganizationalUnit();
        facilityOrganizationalUnit2.setId(facilityOrganizationalUnit1.getId());
        assertThat(facilityOrganizationalUnit1).isEqualTo(facilityOrganizationalUnit2);
        facilityOrganizationalUnit2.setId(2L);
        assertThat(facilityOrganizationalUnit1).isNotEqualTo(facilityOrganizationalUnit2);
        facilityOrganizationalUnit1.setId(null);
        assertThat(facilityOrganizationalUnit1).isNotEqualTo(facilityOrganizationalUnit2);
    }
}
