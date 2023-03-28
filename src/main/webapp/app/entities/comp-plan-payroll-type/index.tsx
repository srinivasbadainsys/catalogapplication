import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CompPlanPayrollType from './comp-plan-payroll-type';
import CompPlanPayrollTypeDetail from './comp-plan-payroll-type-detail';
import CompPlanPayrollTypeUpdate from './comp-plan-payroll-type-update';
import CompPlanPayrollTypeDeleteDialog from './comp-plan-payroll-type-delete-dialog';

const CompPlanPayrollTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CompPlanPayrollType />} />
    <Route path="new" element={<CompPlanPayrollTypeUpdate />} />
    <Route path=":id">
      <Route index element={<CompPlanPayrollTypeDetail />} />
      <Route path="edit" element={<CompPlanPayrollTypeUpdate />} />
      <Route path="delete" element={<CompPlanPayrollTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CompPlanPayrollTypeRoutes;
