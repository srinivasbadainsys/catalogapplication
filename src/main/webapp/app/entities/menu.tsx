import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/country">
        <Translate contentKey="global.menu.entities.country" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/pay-locality">
        <Translate contentKey="global.menu.entities.payLocality" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/pay-type">
        <Translate contentKey="global.menu.entities.payType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/step">
        <Translate contentKey="global.menu.entities.step" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/union">
        <Translate contentKey="global.menu.entities.union" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/component-unit">
        <Translate contentKey="global.menu.entities.componentUnit" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/grade">
        <Translate contentKey="global.menu.entities.grade" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/employment-type">
        <Translate contentKey="global.menu.entities.employmentType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/employment-status">
        <Translate contentKey="global.menu.entities.employmentStatus" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/facility-organizational-unit">
        <Translate contentKey="global.menu.entities.facilityOrganizationalUnit" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/comp-plan-payroll-type">
        <Translate contentKey="global.menu.entities.compPlanPayrollType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/location-shift">
        <Translate contentKey="global.menu.entities.locationShift" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
