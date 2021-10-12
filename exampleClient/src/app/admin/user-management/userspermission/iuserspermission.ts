export interface IUserspermission {
  permissionId: number;
  revoked?: boolean;
  usersUsername: string;

  permissionDescriptiveField?: string;
  usersDescriptiveField?: string;
}
