import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { User } from 'src/auth/schemas/user.schema';
import { DietProgram } from 'src/diet_program/entities/diet_program.entity';
@Injectable()
export class RecommendationService {
  constructor(
    @InjectModel(DietProgram.name) private dietProgramModel: Model<DietProgram>,
    @InjectModel(User.name) private userModel: Model<User>,
  ) {}

  async getRecommendations(userId: string): Promise<DietProgram[]> {
    const user = await this.userModel.findById(userId);

    if (!user) {
      throw new Error('User not found');
    }

    return this.dietProgramModel.find({
      goal: user.fitnessGoal,
      activityLevel: user.activityLevel,
      compatibleBodyTypes: user.height,
      compatibleWeights: user.weight,
       
    }).exec();
  }
}
