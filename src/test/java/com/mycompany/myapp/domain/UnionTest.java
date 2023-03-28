package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UnionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Union.class);
        Union union1 = new Union();
        union1.setId(1L);
        Union union2 = new Union();
        union2.setId(union1.getId());
        assertThat(union1).isEqualTo(union2);
        union2.setId(2L);
        assertThat(union1).isNotEqualTo(union2);
        union1.setId(null);
        assertThat(union1).isNotEqualTo(union2);
    }
}
