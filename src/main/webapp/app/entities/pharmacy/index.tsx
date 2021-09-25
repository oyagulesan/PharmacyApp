import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Pharmacy from './pharmacy';
import PharmacyDetail from './pharmacy-detail';
import PharmacyUpdate from './pharmacy-update';
import PharmacyDeleteDialog from './pharmacy-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PharmacyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PharmacyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PharmacyDetail} />
      <ErrorBoundaryRoute path={match.url} component={Pharmacy} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PharmacyDeleteDialog} />
  </>
);

export default Routes;
