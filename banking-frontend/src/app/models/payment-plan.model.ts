export interface PaymentPlanDTO {
  id?: number;
  contributionAmount: number;
  principalPortion: number;
  interestPortion: number;
  date: string;
  paid?: boolean;
  paidDate?: string | null;
}
