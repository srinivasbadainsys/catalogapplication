import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IStep } from 'app/shared/model/step.model';
import { getEntity, updateEntity, createEntity, reset } from './step.reducer';

export const StepUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const stepEntity = useAppSelector(state => state.step.entity);
  const loading = useAppSelector(state => state.step.loading);
  const updating = useAppSelector(state => state.step.updating);
  const updateSuccess = useAppSelector(state => state.step.updateSuccess);

  const handleClose = () => {
    navigate('/step' + location.search);
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
      ...stepEntity,
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
          ...stepEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="catalogapplicationApp.step.home.createOrEditLabel" data-cy="StepCreateUpdateHeading">
            <Translate contentKey="catalogapplicationApp.step.home.createOrEditLabel">Create or edit a Step</Translate>
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
                  id="step-id"
                  label={translate('catalogapplicationApp.step.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('catalogapplicationApp.step.name')} id="step-name" name="name" data-cy="name" type="text" />
              <ValidatedField
                label={translate('catalogapplicationApp.step.description')}
                id="step-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField label={translate('catalogapplicationApp.step.code')} id="step-code" name="code" data-cy="code" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/step" replace color="info">
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

export default StepUpdate;
