import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import mongoose, { Document } from 'mongoose';
import { v4 as uuidv4 } from 'uuid';

@Schema({ versionKey: false, timestamps: true })
export class RefreshToken extends Document {
  @Prop({
    type: String,
    default: uuidv4, // Generates a unique UUID for the id
  })
  id: string;

  @Prop({ required: true, maxlength: 255 })
  token: string;

  @Prop({ required: true, type: Number })
  userId: number;

  @Prop({ required: true, type: Date })
  expiryDate: Date;
}

export const RefreshTokenSchema = SchemaFactory.createForClass(RefreshToken);
