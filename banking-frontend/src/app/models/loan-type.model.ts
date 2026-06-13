export interface LoanTypeDTO {
  id?: number;
  creditName: string;
  creditDisbursedMin: number;
  creditDisbursedMax: number;
  creditTermMin: number;
  creditTermMax: number;
  creditInterestRate: number;
}
