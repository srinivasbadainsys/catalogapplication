export interface IFacilityOrganizationalUnit {
  id?: number;
  name?: string | null;
  description?: string | null;
  code?: string | null;
}

export const defaultValue: Readonly<IFacilityOrganizationalUnit> = {};
