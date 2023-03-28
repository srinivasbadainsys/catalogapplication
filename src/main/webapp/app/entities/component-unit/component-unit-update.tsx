import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IComponentUnit } from 'app/shared/model/component-unit.model';
import { getEntity, updateEntity, createEntity, reset } from './component-unit.reducer';

export const ComponentUnitUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const componentUnitEntity = useAppSelector(state => state.componentUnit.entity);
  const loading = useAppSelector(state => state.componentUnit.loading);
  const updating = useAppSelector(state => state.componentUnit.updating);
  const updateSuccess = useAppSelector(state => state.componentUnit.updateSuccess);

  const handleClose = () => {
    navigate('/component-unit' + location.search);
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
      ...componentUnitEntity,
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
          ...componentUnitEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="catalogapplicationApp.componentUnit.home.createOrEditLabel" data-cy="ComponentUnitCreateUpdateHeading">
            <Translate contentKey="catalogapplicationApp.componentUnit.home.createOrEditLabel">Create or edit a ComponentUnit</Translate>
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
                  id="component-unit-id"
                  label={translate('catalogapplicationApp.componentUnit.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('catalogapplicationApp.componentUnit.name')}
                id="component-unit-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                label={translate('catalogapplicationApp.componentUnit.description')}
                id="component-unit-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('catalogapplicationApp.componentUnit.code')}
                id="component-unit-code"
                name="code"
                data-cy="code"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/component-unit" replace color="info">
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

export default ComponentUnitUpdate;
