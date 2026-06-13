export interface LoanTypeRef {
  id: number;
  creditName?: string;
}

export interface LoanDTO {
  id: number;
  amountDisbursed: number;
  paymentTerm: number;
  loanType: LoanTypeRef;
  currentPayment: number | null;
  nextPaymentDate: string | null;
}

export interface CreateLoanDTO {
  amountDisbursed: number;
  paymentTerm: number;
  loanType: { id: number };
}
