export interface PersonDTO {
  id?: number;
  firstName: string;
  lastName: string;
  pin?: string;
  address?: string;
  clientNumber?: string;
}

export interface UpdatePersonDTO {
  firstName: string;
  lastName: string;
}
