export interface BankAccountDTO {
  id: number;
  IBAN: string;
  balance: number;
  status: boolean;
}

export interface UpdateBankAccountDTO {
  balance: number;
  status: boolean;
}

export interface CreateBankAccountDTO {
  customerId: number;
  balance: number;
}
