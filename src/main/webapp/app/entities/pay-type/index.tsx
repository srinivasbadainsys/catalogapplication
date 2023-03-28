import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PayType from './pay-type';
import PayTypeDetail from './pay-type-detail';
import PayTypeUpdate from './pay-type-update';
import PayTypeDeleteDialog from './pay-type-delete-dialog';

const PayTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PayType />} />
    <Route path="new" element={<PayTypeUpdate />} />
    <Route path=":id">
      <Route index element={<PayTypeDetail />} />
      <Route path="edit" element={<PayTypeUpdate />} />
      <Route path="delete" element={<PayTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PayTypeRoutes;
