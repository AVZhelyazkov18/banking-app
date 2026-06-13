export interface CompanyDTO {
  id?: number;
  companyName: string;
  eik: string;
  representative: string;
}

export interface UpdateCompanyDTO {
  companyName: string;
  representative: string;
}
