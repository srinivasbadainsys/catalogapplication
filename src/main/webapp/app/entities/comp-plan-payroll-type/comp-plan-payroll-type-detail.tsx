import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './comp-plan-payroll-type.reducer';

export const CompPlanPayrollTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const compPlanPayrollTypeEntity = useAppSelector(state => state.compPlanPayrollType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="compPlanPayrollTypeDetailsHeading">
          <Translate contentKey="catalogapplicationApp.compPlanPayrollType.detail.title">CompPlanPayrollType</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="catalogapplicationApp.compPlanPayrollType.id">Id</Translate>
            </span>
          </dt>
          <dd>{compPlanPayrollTypeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="catalogapplicationApp.compPlanPayrollType.name">Name</Translate>
            </span>
          </dt>
          <dd>{compPlanPayrollTypeEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="catalogapplicationApp.compPlanPayrollType.description">Description</Translate>
            </span>
          </dt>
          <dd>{compPlanPayrollTypeEntity.description}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="catalogapplicationApp.compPlanPayrollType.code">Code</Translate>
            </span>
          </dt>
          <dd>{compPlanPayrollTypeEntity.code}</dd>
        </dl>
        <Button tag={Link} to="/comp-plan-payroll-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/comp-plan-payroll-type/${compPlanPayrollTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CompPlanPayrollTypeDetail;
