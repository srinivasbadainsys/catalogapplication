import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PayLocality from './pay-locality';
import PayLocalityDetail from './pay-locality-detail';
import PayLocalityUpdate from './pay-locality-update';
import PayLocalityDeleteDialog from './pay-locality-delete-dialog';

const PayLocalityRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PayLocality />} />
    <Route path="new" element={<PayLocalityUpdate />} />
    <Route path=":id">
      <Route index element={<PayLocalityDetail />} />
      <Route path="edit" element={<PayLocalityUpdate />} />
      <Route path="delete" element={<PayLocalityDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PayLocalityRoutes;
