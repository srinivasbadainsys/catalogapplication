package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocationShiftTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocationShift.class);
        LocationShift locationShift1 = new LocationShift();
        locationShift1.setId(1L);
        LocationShift locationShift2 = new LocationShift();
        locationShift2.setId(locationShift1.getId());
        assertThat(locationShift1).isEqualTo(locationShift2);
        locationShift2.setId(2L);
        assertThat(locationShift1).isNotEqualTo(locationShift2);
        locationShift1.setId(null);
        assertThat(locationShift1).isNotEqualTo(locationShift2);
    }
}
