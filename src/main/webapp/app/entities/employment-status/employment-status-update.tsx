import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IEmploymentStatus } from 'app/shared/model/employment-status.model';
import { getEntity, updateEntity, createEntity, reset } from './employment-status.reducer';

export const EmploymentStatusUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const employmentStatusEntity = useAppSelector(state => state.employmentStatus.entity);
  const loading = useAppSelector(state => state.employmentStatus.loading);
  const updating = useAppSelector(state => state.employmentStatus.updating);
  const updateSuccess = useAppSelector(state => state.employmentStatus.updateSuccess);

  const handleClose = () => {
    navigate('/employment-status' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...employmentStatusEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...employmentStatusEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="catalogapplicationApp.employmentStatus.home.createOrEditLabel" data-cy="EmploymentStatusCreateUpdateHeading">
            <Translate contentKey="catalogapplicationApp.employmentStatus.home.createOrEditLabel">
              Create or edit a EmploymentStatus
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="employment-status-id"
                  label={translate('catalogapplicationApp.employmentStatus.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('catalogapplicationApp.employmentStatus.name')}
                id="employment-status-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                label={translate('catalogapplicationApp.employmentStatus.description')}
                id="employment-status-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('catalogapplicationApp.employmentStatus.code')}
                id="employment-status-code"
                name="code"
                data-cy="code"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/employment-status" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default EmploymentStatusUpdate;
