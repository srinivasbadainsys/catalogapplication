import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './facility-organizational-unit.reducer';

export const FacilityOrganizationalUnitDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const facilityOrganizationalUnitEntity = useAppSelector(state => state.facilityOrganizationalUnit.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="facilityOrganizationalUnitDetailsHeading">
          <Translate contentKey="catalogapplicationApp.facilityOrganizationalUnit.detail.title">FacilityOrganizationalUnit</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="catalogapplicationApp.facilityOrganizationalUnit.id">Id</Translate>
            </span>
          </dt>
          <dd>{facilityOrganizationalUnitEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="catalogapplicationApp.facilityOrganizationalUnit.name">Name</Translate>
            </span>
          </dt>
          <dd>{facilityOrganizationalUnitEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="catalogapplicationApp.facilityOrganizationalUnit.description">Description</Translate>
            </span>
          </dt>
          <dd>{facilityOrganizationalUnitEntity.description}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="catalogapplicationApp.facilityOrganizationalUnit.code">Code</Translate>
            </span>
          </dt>
          <dd>{facilityOrganizationalUnitEntity.code}</dd>
        </dl>
        <Button tag={Link} to="/facility-organizational-unit" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/facility-organizational-unit/${facilityOrganizationalUnitEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FacilityOrganizationalUnitDetail;
