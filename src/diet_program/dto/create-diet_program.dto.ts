import { IsNumber, IsString } from "class-validator";

export class CreateDietProgramDto {
    @IsString()
    name: string;
    @IsString()
    description: string;
    @IsString()
    goal: string;
    @IsString()
    level: string;
    @IsNumber()
    calories: number;
  
}
