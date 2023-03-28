import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ComponentUnit from './component-unit';
import ComponentUnitDetail from './component-unit-detail';
import ComponentUnitUpdate from './component-unit-update';
import ComponentUnitDeleteDialog from './component-unit-delete-dialog';

const ComponentUnitRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ComponentUnit />} />
    <Route path="new" element={<ComponentUnitUpdate />} />
    <Route path=":id">
      <Route index element={<ComponentUnitDetail />} />
      <Route path="edit" element={<ComponentUnitUpdate />} />
      <Route path="delete" element={<ComponentUnitDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ComponentUnitRoutes;
