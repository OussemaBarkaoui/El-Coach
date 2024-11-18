import { PartialType } from '@nestjs/mapped-types';
import { CreateDietProgramDto } from './create-diet_program.dto';

export class UpdateDietProgramDto extends PartialType(CreateDietProgramDto) {}
