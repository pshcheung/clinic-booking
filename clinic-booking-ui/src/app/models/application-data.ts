export interface ApplicationData {
  title: string;
  version: string;
  organizationCode: string;
  availableServices: ApplicationService[];
}

export interface ApplicationService {
  code: string;
  name: string;
  description: string;
  clientRateCode: string;
  therapistRateCode: string;
}
