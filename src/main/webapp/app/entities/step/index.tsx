import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Step from './step';
import StepDetail from './step-detail';
import StepUpdate from './step-update';
import StepDeleteDialog from './step-delete-dialog';

const StepRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Step />} />
    <Route path="new" element={<StepUpdate />} />
    <Route path=":id">
      <Route index element={<StepDetail />} />
      <Route path="edit" element={<StepUpdate />} />
      <Route path="delete" element={<StepDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StepRoutes;
