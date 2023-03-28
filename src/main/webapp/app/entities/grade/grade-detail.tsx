import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './grade.reducer';

export const GradeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const gradeEntity = useAppSelector(state => state.grade.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="gradeDetailsHeading">
          <Translate contentKey="catalogapplicationApp.grade.detail.title">Grade</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="catalogapplicationApp.grade.id">Id</Translate>
            </span>
          </dt>
          <dd>{gradeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="catalogapplicationApp.grade.name">Name</Translate>
            </span>
          </dt>
          <dd>{gradeEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="catalogapplicationApp.grade.description">Description</Translate>
            </span>
          </dt>
          <dd>{gradeEntity.description}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="catalogapplicationApp.grade.code">Code</Translate>
            </span>
          </dt>
          <dd>{gradeEntity.code}</dd>
        </dl>
        <Button tag={Link} to="/grade" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/grade/${gradeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default GradeDetail;
