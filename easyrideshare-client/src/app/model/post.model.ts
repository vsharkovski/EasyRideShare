import { Location } from './location.model';
import { PostStatus } from './post-status.enum';
import { TransportType } from './transport-type.enum';
import { User } from './user.model';

export interface Post {
  id: number;
  creationTime: number;
  creator: User;
  status: PostStatus;
  transportType: TransportType;
  startLocation: Location;
  endLocation: Location;
  description: string | null;
  intendedTravelTime?: number;
}
