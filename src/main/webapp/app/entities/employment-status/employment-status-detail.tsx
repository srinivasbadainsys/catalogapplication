import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './employment-status.reducer';

export const EmploymentStatusDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const employmentStatusEntity = useAppSelector(state => state.employmentStatus.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="employmentStatusDetailsHeading">
          <Translate contentKey="catalogapplicationApp.employmentStatus.detail.title">EmploymentStatus</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="catalogapplicationApp.employmentStatus.id">Id</Translate>
            </span>
          </dt>
          <dd>{employmentStatusEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="catalogapplicationApp.employmentStatus.name">Name</Translate>
            </span>
          </dt>
          <dd>{employmentStatusEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="catalogapplicationApp.employmentStatus.description">Description</Translate>
            </span>
          </dt>
          <dd>{employmentStatusEntity.description}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="catalogapplicationApp.employmentStatus.code">Code</Translate>
            </span>
          </dt>
          <dd>{employmentStatusEntity.code}</dd>
        </dl>
        <Button tag={Link} to="/employment-status" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/employment-status/${employmentStatusEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EmploymentStatusDetail;
