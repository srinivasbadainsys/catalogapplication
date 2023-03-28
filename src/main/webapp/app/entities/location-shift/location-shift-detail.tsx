import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './location-shift.reducer';

export const LocationShiftDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const locationShiftEntity = useAppSelector(state => state.locationShift.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="locationShiftDetailsHeading">
          <Translate contentKey="catalogapplicationApp.locationShift.detail.title">LocationShift</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="catalogapplicationApp.locationShift.id">Id</Translate>
            </span>
          </dt>
          <dd>{locationShiftEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="catalogapplicationApp.locationShift.name">Name</Translate>
            </span>
          </dt>
          <dd>{locationShiftEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="catalogapplicationApp.locationShift.description">Description</Translate>
            </span>
          </dt>
          <dd>{locationShiftEntity.description}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="catalogapplicationApp.locationShift.code">Code</Translate>
            </span>
          </dt>
          <dd>{locationShiftEntity.code}</dd>
        </dl>
        <Button tag={Link} to="/location-shift" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/location-shift/${locationShiftEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LocationShiftDetail;
