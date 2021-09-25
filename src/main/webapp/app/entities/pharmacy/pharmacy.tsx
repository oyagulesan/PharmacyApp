import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './pharmacy.reducer';
import { IPharmacy } from 'app/shared/model/pharmacy.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Pharmacy = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const pharmacyList = useAppSelector(state => state.pharmacy.entities);
  const loading = useAppSelector(state => state.pharmacy.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="pharmacy-heading" data-cy="PharmacyHeading">
        <Translate contentKey="pharmacyApp.pharmacy.home.title">Pharmacies</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="pharmacyApp.pharmacy.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="pharmacyApp.pharmacy.home.createLabel">Create new Pharmacy</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {pharmacyList && pharmacyList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="pharmacyApp.pharmacy.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="pharmacyApp.pharmacy.pharmacyName">Pharmacy Name</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {pharmacyList.map((pharmacy, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${pharmacy.id}`} color="link" size="sm">
                      {pharmacy.id}
                    </Button>
                  </td>
                  <td>{pharmacy.pharmacyName}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${pharmacy.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${pharmacy.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${pharmacy.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="pharmacyApp.pharmacy.home.notFound">No Pharmacies found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Pharmacy;
