import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document, SchemaTypes, Types } from 'mongoose';
@Schema()
export class DietProgram extends Document{
  @Prop()
  name: string;
  @Prop()
  description: string;
  @Prop()
  goal: string;
  @Prop()
  level: string;
  @Prop()
  calories: number;
  @Prop({ type: [String], required: true })
  targetGenders: string[]; // e.g., ["male", "female"]

  @Prop({ required: true })
  activityLevel: string; // e.g., "moderate", "intense"


}
export const DietProgramSchema = SchemaFactory.createForClass(DietProgram);