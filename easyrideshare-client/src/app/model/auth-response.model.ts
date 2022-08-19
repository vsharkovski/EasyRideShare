import { Response } from './response.model';

export interface AuthUserInfoResponse extends Response {
  id: number;
  username: string;
  roles: string[];
}
