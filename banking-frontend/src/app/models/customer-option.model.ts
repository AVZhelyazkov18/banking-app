export interface CustomerOptionDTO {
  id: number;
  displayName: string;
  type: 'PERSON' | 'COMPANY' | 'CUSTOMER';
}
