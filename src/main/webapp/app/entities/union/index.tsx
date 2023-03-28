import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Union from './union';
import UnionDetail from './union-detail';
import UnionUpdate from './union-update';
import UnionDeleteDialog from './union-delete-dialog';

const UnionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Union />} />
    <Route path="new" element={<UnionUpdate />} />
    <Route path=":id">
      <Route index element={<UnionDetail />} />
      <Route path="edit" element={<UnionUpdate />} />
      <Route path="delete" element={<UnionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UnionRoutes;
