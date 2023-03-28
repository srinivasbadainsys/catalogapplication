package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PayTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PayType.class);
        PayType payType1 = new PayType();
        payType1.setId(1L);
        PayType payType2 = new PayType();
        payType2.setId(payType1.getId());
        assertThat(payType1).isEqualTo(payType2);
        payType2.setId(2L);
        assertThat(payType1).isNotEqualTo(payType2);
        payType1.setId(null);
        assertThat(payType1).isNotEqualTo(payType2);
    }
}
