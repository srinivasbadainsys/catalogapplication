import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './step.reducer';

export const StepDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const stepEntity = useAppSelector(state => state.step.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="stepDetailsHeading">
          <Translate contentKey="catalogapplicationApp.step.detail.title">Step</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="catalogapplicationApp.step.id">Id</Translate>
            </span>
          </dt>
          <dd>{stepEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="catalogapplicationApp.step.name">Name</Translate>
            </span>
          </dt>
          <dd>{stepEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="catalogapplicationApp.step.description">Description</Translate>
            </span>
          </dt>
          <dd>{stepEntity.description}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="catalogapplicationApp.step.code">Code</Translate>
            </span>
          </dt>
          <dd>{stepEntity.code}</dd>
        </dl>
        <Button tag={Link} to="/step" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/step/${stepEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StepDetail;
