import { Length, IsEmail, IsString, IsOptional } from "class-validator";
export class LoginDto {
  @IsString()
  password: string;
  @IsString()
  @IsOptional()
  identifier: string;
}

