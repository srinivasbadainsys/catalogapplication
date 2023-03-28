package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ComponentUnitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComponentUnit.class);
        ComponentUnit componentUnit1 = new ComponentUnit();
        componentUnit1.setId(1L);
        ComponentUnit componentUnit2 = new ComponentUnit();
        componentUnit2.setId(componentUnit1.getId());
        assertThat(componentUnit1).isEqualTo(componentUnit2);
        componentUnit2.setId(2L);
        assertThat(componentUnit1).isNotEqualTo(componentUnit2);
        componentUnit1.setId(null);
        assertThat(componentUnit1).isNotEqualTo(componentUnit2);
    }
}
