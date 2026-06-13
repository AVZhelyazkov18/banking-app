export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  role: string;
  firstName: string;
  lastName: string;
  pin: string;
  phone: string;
  address: string;
}

export interface AuthResponse {
  email: string;
  role: string;
}

export interface UserProfileResponse {
  email: string;
  role: string;
  clientNumber: string;
  firstName: string;
  lastName: string;
  phone: string;
  address: string;
}

export interface UpdateProfileRequest {
  firstName: string;
  lastName: string;
  phone: string;
  address: string;
}
