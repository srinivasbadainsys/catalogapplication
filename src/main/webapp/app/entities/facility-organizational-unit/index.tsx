import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import FacilityOrganizationalUnit from './facility-organizational-unit';
import FacilityOrganizationalUnitDetail from './facility-organizational-unit-detail';
import FacilityOrganizationalUnitUpdate from './facility-organizational-unit-update';
import FacilityOrganizationalUnitDeleteDialog from './facility-organizational-unit-delete-dialog';

const FacilityOrganizationalUnitRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<FacilityOrganizationalUnit />} />
    <Route path="new" element={<FacilityOrganizationalUnitUpdate />} />
    <Route path=":id">
      <Route index element={<FacilityOrganizationalUnitDetail />} />
      <Route path="edit" element={<FacilityOrganizationalUnitUpdate />} />
      <Route path="delete" element={<FacilityOrganizationalUnitDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FacilityOrganizationalUnitRoutes;
