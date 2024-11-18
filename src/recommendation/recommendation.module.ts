import { Module } from '@nestjs/common';
import { RecommendationService } from './recommendation.service';
import { DietProgramModule } from 'src/diet_program/diet_program.module'; // Import DietProgramModule
import { User, UserSchema } from 'src/auth/schemas/user.schema';
import { MongooseModule } from '@nestjs/mongoose';
import { RecommendationController } from './recommendation.controller';

@Module({
  imports: [
    DietProgramModule, // Import DietProgramModule directly
    MongooseModule.forFeature([
      { name: User.name, schema: UserSchema },  // Register User schema
    ]),
  ],
  controllers: [RecommendationController],
  providers: [RecommendationService],
  exports: [RecommendationModule]
})
export class RecommendationModule {}
