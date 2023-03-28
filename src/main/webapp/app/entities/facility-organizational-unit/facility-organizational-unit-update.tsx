import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IFacilityOrganizationalUnit } from 'app/shared/model/facility-organizational-unit.model';
import { getEntity, updateEntity, createEntity, reset } from './facility-organizational-unit.reducer';

export const FacilityOrganizationalUnitUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const facilityOrganizationalUnitEntity = useAppSelector(state => state.facilityOrganizationalUnit.entity);
  const loading = useAppSelector(state => state.facilityOrganizationalUnit.loading);
  const updating = useAppSelector(state => state.facilityOrganizationalUnit.updating);
  const updateSuccess = useAppSelector(state => state.facilityOrganizationalUnit.updateSuccess);

  const handleClose = () => {
    navigate('/facility-organizational-unit' + location.search);
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
      ...facilityOrganizationalUnitEntity,
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
          ...facilityOrganizationalUnitEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2
            id="catalogapplicationApp.facilityOrganizationalUnit.home.createOrEditLabel"
            data-cy="FacilityOrganizationalUnitCreateUpdateHeading"
          >
            <Translate contentKey="catalogapplicationApp.facilityOrganizationalUnit.home.createOrEditLabel">
              Create or edit a FacilityOrganizationalUnit
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
                  id="facility-organizational-unit-id"
                  label={translate('catalogapplicationApp.facilityOrganizationalUnit.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('catalogapplicationApp.facilityOrganizationalUnit.name')}
                id="facility-organizational-unit-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                label={translate('catalogapplicationApp.facilityOrganizationalUnit.description')}
                id="facility-organizational-unit-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('catalogapplicationApp.facilityOrganizationalUnit.code')}
                id="facility-organizational-unit-code"
                name="code"
                data-cy="code"
                type="text"
              />
              <Button
                tag={Link}
                id="cancel-save"
                data-cy="entityCreateCancelButton"
                to="/facility-organizational-unit"
                replace
                color="info"
              >
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

export default FacilityOrganizationalUnitUpdate;
