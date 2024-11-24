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
  fitnessGoal: string;

  @Prop({ required: false })
  activityLevel: string;

  @Prop({ required: false })
  height: number; 

  @Prop({ required: false })
  weight: number; 
}

export const UserSchema = SchemaFactory.createForClass(User);
