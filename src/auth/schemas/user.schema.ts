import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document, SchemaTypes, Types } from 'mongoose';

@Schema()
export class User extends Document {
  @Prop({ required: false })
  fullName: string;

  @Prop({ required: true, unique: true })
  email: string;

  @Prop({ required: true })
  password: string;

  @Prop({ required: false, type: SchemaTypes.Buffer })
  image: Buffer;

  @Prop()
  phoneNumber: number;
  
  @Prop({ required: true })
  fitnessGoal: string; // e.g., "muscle gain", "weight loss"

  @Prop({ required: true })
  activityLevel: string; // e.g., "sedentary", "moderate", "intense"

  @Prop({ required: true })
  bodyType: string; // e.g., "ectomorph", "endomorph", "mesomorph"

  @Prop({ required: true })
  gender: string; // e.g., "male", "female"
}

export const UserSchema = SchemaFactory.createForClass(User);
