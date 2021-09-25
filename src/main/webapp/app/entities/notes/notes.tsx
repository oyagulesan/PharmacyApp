import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './notes.reducer';
import { INotes } from 'app/shared/model/notes.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Notes = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const notesList = useAppSelector(state => state.notes.entities);
  const loading = useAppSelector(state => state.notes.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="notes-heading" data-cy="NotesHeading">
        <Translate contentKey="pharmacyApp.notes.home.title">Notes</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="pharmacyApp.notes.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="pharmacyApp.notes.home.createLabel">Create new Notes</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {notesList && notesList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="pharmacyApp.notes.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="pharmacyApp.notes.date">Date</Translate>
                </th>
                <th>
                  <Translate contentKey="pharmacyApp.notes.comment">Comment</Translate>
                </th>
                <th>
                  <Translate contentKey="pharmacyApp.notes.pharmacy">Pharmacy</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {notesList.map((notes, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${notes.id}`} color="link" size="sm">
                      {notes.id}
                    </Button>
                  </td>
                  <td>{notes.date ? <TextFormat type="date" value={notes.date} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{notes.comment}</td>
                  <td>{notes.pharmacy ? <Link to={`pharmacy/${notes.pharmacy.id}`}>{notes.pharmacy.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${notes.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${notes.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${notes.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="pharmacyApp.notes.home.notFound">No Notes found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Notes;
