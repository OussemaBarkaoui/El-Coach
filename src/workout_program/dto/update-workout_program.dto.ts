import { PartialType } from '@nestjs/mapped-types';
import { CreateWorkoutProgramDto } from './create-workout_program.dto';

export class UpdateWorkoutProgramDto extends PartialType(CreateWorkoutProgramDto) {}
