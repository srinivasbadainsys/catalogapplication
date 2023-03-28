import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Country from './country';
import PayLocality from './pay-locality';
import PayType from './pay-type';
import Step from './step';
import Union from './union';
import ComponentUnit from './component-unit';
import Grade from './grade';
import EmploymentType from './employment-type';
import EmploymentStatus from './employment-status';
import FacilityOrganizationalUnit from './facility-organizational-unit';
import CompPlanPayrollType from './comp-plan-payroll-type';
import LocationShift from './location-shift';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="country/*" element={<Country />} />
        <Route path="pay-locality/*" element={<PayLocality />} />
        <Route path="pay-type/*" element={<PayType />} />
        <Route path="step/*" element={<Step />} />
        <Route path="union/*" element={<Union />} />
        <Route path="component-unit/*" element={<ComponentUnit />} />
        <Route path="grade/*" element={<Grade />} />
        <Route path="employment-type/*" element={<EmploymentType />} />
        <Route path="employment-status/*" element={<EmploymentStatus />} />
        <Route path="facility-organizational-unit/*" element={<FacilityOrganizationalUnit />} />
        <Route path="comp-plan-payroll-type/*" element={<CompPlanPayrollType />} />
        <Route path="location-shift/*" element={<LocationShift />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
