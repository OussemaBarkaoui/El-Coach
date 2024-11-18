import { Module } from '@nestjs/common';
import { WorkoutProgramService } from './workout_program.service';
import { WorkoutProgramController } from './workout_program.controller';
import { MongooseModule } from '@nestjs/mongoose';
import { WorkoutProgram, WorkoutProgramSchema } from './entities/workout_program.entity';

@Module({
  imports: [
    MongooseModule.forFeature([
      { name: WorkoutProgram.name, schema: WorkoutProgramSchema },
    ]),
  ],
  controllers: [WorkoutProgramController],
  providers: [WorkoutProgramService],
  exports: [WorkoutProgramService,MongooseModule,], 
})
export class WorkoutProgramModule {}
