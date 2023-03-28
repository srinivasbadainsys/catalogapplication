import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './pay-locality.reducer';

export const PayLocalityDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const payLocalityEntity = useAppSelector(state => state.payLocality.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="payLocalityDetailsHeading">
          <Translate contentKey="catalogapplicationApp.payLocality.detail.title">PayLocality</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="catalogapplicationApp.payLocality.id">Id</Translate>
            </span>
          </dt>
          <dd>{payLocalityEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="catalogapplicationApp.payLocality.name">Name</Translate>
            </span>
          </dt>
          <dd>{payLocalityEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="catalogapplicationApp.payLocality.description">Description</Translate>
            </span>
          </dt>
          <dd>{payLocalityEntity.description}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="catalogapplicationApp.payLocality.code">Code</Translate>
            </span>
          </dt>
          <dd>{payLocalityEntity.code}</dd>
        </dl>
        <Button tag={Link} to="/pay-locality" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pay-locality/${payLocalityEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PayLocalityDetail;
