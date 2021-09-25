import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Notes from './notes';
import NotesDetail from './notes-detail';
import NotesUpdate from './notes-update';
import NotesDeleteDialog from './notes-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={NotesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={NotesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={NotesDetail} />
      <ErrorBoundaryRoute path={match.url} component={Notes} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={NotesDeleteDialog} />
  </>
);

export default Routes;
