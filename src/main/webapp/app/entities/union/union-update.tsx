import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUnion } from 'app/shared/model/union.model';
import { getEntity, updateEntity, createEntity, reset } from './union.reducer';

export const UnionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const unionEntity = useAppSelector(state => state.union.entity);
  const loading = useAppSelector(state => state.union.loading);
  const updating = useAppSelector(state => state.union.updating);
  const updateSuccess = useAppSelector(state => state.union.updateSuccess);

  const handleClose = () => {
    navigate('/union' + location.search);
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
      ...unionEntity,
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
          ...unionEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="catalogapplicationApp.union.home.createOrEditLabel" data-cy="UnionCreateUpdateHeading">
            <Translate contentKey="catalogapplicationApp.union.home.createOrEditLabel">Create or edit a Union</Translate>
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
                  id="union-id"
                  label={translate('catalogapplicationApp.union.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('catalogapplicationApp.union.name')}
                id="union-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                label={translate('catalogapplicationApp.union.description')}
                id="union-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('catalogapplicationApp.union.code')}
                id="union-code"
                name="code"
                data-cy="code"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/union" replace color="info">
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

export default UnionUpdate;
