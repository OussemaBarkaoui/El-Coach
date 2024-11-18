import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { DietProgramService } from './diet_program.service';
import { DietProgramController } from './diet_program.controller';
import { DietProgram, DietProgramSchema } from './entities/diet_program.entity';

@Module({
  imports: [
    MongooseModule.forFeature([
      { name: DietProgram.name, schema: DietProgramSchema },
    ]),
  ],
  controllers: [DietProgramController],
  providers: [DietProgramService],
  exports: [DietProgramService,MongooseModule,], 
})
export class DietProgramModule {}
