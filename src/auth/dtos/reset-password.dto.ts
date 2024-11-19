import { IsNumber, IsString, Matches, MinLength } from 'class-validator';

export class ResetPasswordDto {
  @IsNumber()
  resetToken: number;

  @IsString()
  @MinLength(6)
  @Matches(/^(?=.*[0-9])/, { message: 'Password must contain at least one number' })
  newPassword: string;
}
