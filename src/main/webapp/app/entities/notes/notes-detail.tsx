import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './notes.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const NotesDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const notesEntity = useAppSelector(state => state.notes.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="notesDetailsHeading">
          <Translate contentKey="pharmacyApp.notes.detail.title">Notes</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{notesEntity.id}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="pharmacyApp.notes.date">Date</Translate>
            </span>
          </dt>
          <dd>{notesEntity.date ? <TextFormat value={notesEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="comment">
              <Translate contentKey="pharmacyApp.notes.comment">Comment</Translate>
            </span>
          </dt>
          <dd>{notesEntity.comment}</dd>
          <dt>
            <Translate contentKey="pharmacyApp.notes.pharmacy">Pharmacy</Translate>
          </dt>
          <dd>{notesEntity.pharmacy ? notesEntity.pharmacy.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/notes" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/notes/${notesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default NotesDetail;
