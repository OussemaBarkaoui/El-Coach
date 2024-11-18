import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document, Types } from 'mongoose';

@Schema()
export class WorkoutProgram extends Document {
  @Prop({ required: true })
  name: string;

  @Prop()
  description: string;

  @Prop({ type: [String], required: true })
  targetMuscleGroups: string[]; // Example: ['chest', 'legs', 'back']

  @Prop({ required: true })
  level: string; // Example: 'beginner', 'intermediate', 'advanced'

  @Prop({ required: true })
  duration: number; // Duration in weeks

  @Prop({ required: true })
  goal: string; // Example: 'muscle gain', 'fat loss'

  @Prop({ type: [String], required: true })
  targetGenders: string[]; // Example: ['male', 'female']

  @Prop({ type: Types.ObjectId, ref: 'DietProgram' })
  dietPlan?: Types.ObjectId; // Reference to a diet program

}

export const WorkoutProgramSchema = SchemaFactory.createForClass(WorkoutProgram);
