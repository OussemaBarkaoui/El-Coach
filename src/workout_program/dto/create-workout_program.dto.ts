import { IsArray, IsMongoId, IsNumber, IsOptional, IsString } from 'class-validator';
export class CreateWorkoutProgramDto {
    @IsString()
     name: string;
  
    @IsOptional()
    @IsString()
     description?: string;
  
    @IsArray()
    @IsString({ each: true })
     targetMuscleGroups: string[];
  
    @IsString()
     level: string;
  
    @IsNumber()
     duration: number;
  
    @IsString()
     goal: string;
  
    @IsArray()
    @IsString({ each: true })
     targetGenders: string[];
  
    @IsOptional()
    @IsMongoId()
     dietPlan?: string;

}
