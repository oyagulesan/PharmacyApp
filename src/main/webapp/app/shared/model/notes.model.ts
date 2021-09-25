import dayjs from 'dayjs';
import { IPharmacy } from 'app/shared/model/pharmacy.model';

export interface INotes {
  id?: number;
  date?: string;
  comment?: string;
  pharmacy?: IPharmacy | null;
}

export const defaultValue: Readonly<INotes> = {};
