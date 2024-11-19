import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document, SchemaTypes, Types } from 'mongoose';
import { User } from './user.schema'; // Import the User schema
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

  @Prop({ type: SchemaTypes.ObjectId, ref: 'User', required: true }) // Reference to User
  userId: Types.ObjectId;

  @Prop({ required: true, type: Date })
  expiryDate: Date;
}

export const RefreshTokenSchema = SchemaFactory.createForClass(RefreshToken);
