import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './component-unit.reducer';

export const ComponentUnitDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const componentUnitEntity = useAppSelector(state => state.componentUnit.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="componentUnitDetailsHeading">
          <Translate contentKey="catalogapplicationApp.componentUnit.detail.title">ComponentUnit</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="catalogapplicationApp.componentUnit.id">Id</Translate>
            </span>
          </dt>
          <dd>{componentUnitEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="catalogapplicationApp.componentUnit.name">Name</Translate>
            </span>
          </dt>
          <dd>{componentUnitEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="catalogapplicationApp.componentUnit.description">Description</Translate>
            </span>
          </dt>
          <dd>{componentUnitEntity.description}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="catalogapplicationApp.componentUnit.code">Code</Translate>
            </span>
          </dt>
          <dd>{componentUnitEntity.code}</dd>
        </dl>
        <Button tag={Link} to="/component-unit" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/component-unit/${componentUnitEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ComponentUnitDetail;
