import { Pipe, PipeTransform } from '@angular/core';
import { TransportType } from '../model/transport-type.enum';

@Pipe({
  name: 'transportType',
})
export class TransportTypePipe implements PipeTransform {
  transform(value: TransportType): string {
    switch (value) {
      case TransportType.HIRED_VEHICLE:
        return 'Taxi or similar';
      case TransportType.OWNED_VEHICLE:
        return 'Personal car or similar';
      default:
        return '';
    }
  }
}
