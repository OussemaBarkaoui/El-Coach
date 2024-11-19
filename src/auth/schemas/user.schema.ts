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
  
  @Prop({ required: false })
  fitnessGoal: string; // e.g., "muscle gain", "weight loss"

  @Prop({ required: false })
  activityLevel: string; // e.g., "sedentary", "moderate", "intense"

  @Prop({ required: false })
  bodyType: string; // e.g., "ectomorph", "endomorph", "mesomorph"

  @Prop({ required: false })
  gender: string; // e.g., "male", "female"
}

export const UserSchema = SchemaFactory.createForClass(User);
