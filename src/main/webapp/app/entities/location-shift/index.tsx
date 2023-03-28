import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import LocationShift from './location-shift';
import LocationShiftDetail from './location-shift-detail';
import LocationShiftUpdate from './location-shift-update';
import LocationShiftDeleteDialog from './location-shift-delete-dialog';

const LocationShiftRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<LocationShift />} />
    <Route path="new" element={<LocationShiftUpdate />} />
    <Route path=":id">
      <Route index element={<LocationShiftDetail />} />
      <Route path="edit" element={<LocationShiftUpdate />} />
      <Route path="delete" element={<LocationShiftDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LocationShiftRoutes;
