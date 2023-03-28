import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './employment-type.reducer';

export const EmploymentTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const employmentTypeEntity = useAppSelector(state => state.employmentType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="employmentTypeDetailsHeading">
          <Translate contentKey="catalogapplicationApp.employmentType.detail.title">EmploymentType</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="catalogapplicationApp.employmentType.id">Id</Translate>
            </span>
          </dt>
          <dd>{employmentTypeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="catalogapplicationApp.employmentType.name">Name</Translate>
            </span>
          </dt>
          <dd>{employmentTypeEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="catalogapplicationApp.employmentType.description">Description</Translate>
            </span>
          </dt>
          <dd>{employmentTypeEntity.description}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="catalogapplicationApp.employmentType.code">Code</Translate>
            </span>
          </dt>
          <dd>{employmentTypeEntity.code}</dd>
        </dl>
        <Button tag={Link} to="/employment-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/employment-type/${employmentTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EmploymentTypeDetail;
