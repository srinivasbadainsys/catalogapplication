import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import EmploymentType from './employment-type';
import EmploymentTypeDetail from './employment-type-detail';
import EmploymentTypeUpdate from './employment-type-update';
import EmploymentTypeDeleteDialog from './employment-type-delete-dialog';

const EmploymentTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<EmploymentType />} />
    <Route path="new" element={<EmploymentTypeUpdate />} />
    <Route path=":id">
      <Route index element={<EmploymentTypeDetail />} />
      <Route path="edit" element={<EmploymentTypeUpdate />} />
      <Route path="delete" element={<EmploymentTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EmploymentTypeRoutes;
