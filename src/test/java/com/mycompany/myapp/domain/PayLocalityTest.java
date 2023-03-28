package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PayLocalityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PayLocality.class);
        PayLocality payLocality1 = new PayLocality();
        payLocality1.setId(1L);
        PayLocality payLocality2 = new PayLocality();
        payLocality2.setId(payLocality1.getId());
        assertThat(payLocality1).isEqualTo(payLocality2);
        payLocality2.setId(2L);
        assertThat(payLocality1).isNotEqualTo(payLocality2);
        payLocality1.setId(null);
        assertThat(payLocality1).isNotEqualTo(payLocality2);
    }
}
