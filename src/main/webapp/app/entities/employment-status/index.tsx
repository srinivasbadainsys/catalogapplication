import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import EmploymentStatus from './employment-status';
import EmploymentStatusDetail from './employment-status-detail';
import EmploymentStatusUpdate from './employment-status-update';
import EmploymentStatusDeleteDialog from './employment-status-delete-dialog';

const EmploymentStatusRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<EmploymentStatus />} />
    <Route path="new" element={<EmploymentStatusUpdate />} />
    <Route path=":id">
      <Route index element={<EmploymentStatusDetail />} />
      <Route path="edit" element={<EmploymentStatusUpdate />} />
      <Route path="delete" element={<EmploymentStatusDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EmploymentStatusRoutes;
