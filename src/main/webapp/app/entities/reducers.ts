import country from 'app/entities/country/country.reducer';
import payLocality from 'app/entities/pay-locality/pay-locality.reducer';
import payType from 'app/entities/pay-type/pay-type.reducer';
import step from 'app/entities/step/step.reducer';
import union from 'app/entities/union/union.reducer';
import componentUnit from 'app/entities/component-unit/component-unit.reducer';
import grade from 'app/entities/grade/grade.reducer';
import employmentType from 'app/entities/employment-type/employment-type.reducer';
import employmentStatus from 'app/entities/employment-status/employment-status.reducer';
import facilityOrganizationalUnit from 'app/entities/facility-organizational-unit/facility-organizational-unit.reducer';
import compPlanPayrollType from 'app/entities/comp-plan-payroll-type/comp-plan-payroll-type.reducer';
import locationShift from 'app/entities/location-shift/location-shift.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  country,
  payLocality,
  payType,
  step,
  union,
  componentUnit,
  grade,
  employmentType,
  employmentStatus,
  facilityOrganizationalUnit,
  compPlanPayrollType,
  locationShift,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
