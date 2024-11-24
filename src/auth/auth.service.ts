import {
  BadRequestException,
  Injectable,
  InternalServerErrorException,
  NotFoundException,
  UnauthorizedException,
} from '@nestjs/common';
import { SignupDto } from './dtos/signup.dto';
import { InjectModel } from '@nestjs/mongoose';
import { User } from './schemas/user.schema';
import mongoose, { Model } from 'mongoose';
import * as bcrypt from 'bcrypt';
import { LoginDto } from './dtos/login.dto';
import { JwtService } from '@nestjs/jwt';
import { RefreshToken } from './schemas/refresh-token.schema';
import { v4 as uuidv4 } from 'uuid';
import { nanoid } from 'nanoid';
import { ResetToken } from './schemas/reset-token.schema';
import { MailService } from 'src/services/mail.service';
import { OAuth2Client } from 'google-auth-library';
import { CompleteProfileDto } from './dtos/complete-profile.dto';

@Injectable()
export class AuthService {
  private oauthClient: OAuth2Client;
  constructor(
    @InjectModel(User.name) private UserModel: Model<User>,
    @InjectModel(RefreshToken.name)
    private RefreshTokenModel: Model<RefreshToken>,
    @InjectModel(ResetToken.name)
    private ResetTokenModel: Model<ResetToken>,
    private jwtService: JwtService,
    private mailService: MailService,
  ) {
    this.oauthClient = new OAuth2Client('883402420156-a7c8u8f588ok0no3dm5mlodseul1h0ku.apps.googleusercontent.com'); // Replace with your client ID

  }

  async signup(signupData: SignupDto) {
    const { email, password, fullName, image, phoneNumber } = signupData;

    //Check if email is in use
    const emailInUse = await this.UserModel.findOne({
      email,
    });
    if (emailInUse) {
      throw new BadRequestException('Email already in use');
    }
    //Hash password
    const hashedPassword = await bcrypt.hash(password, 10);

    // Create user document and save in mongodb
    return await this.UserModel.create({
      fullName,
      email,
      password: hashedPassword,
      image,
      phoneNumber
    });
  }

  async login(credentials: LoginDto) {
    const { email, password } = credentials;
    //Find if user exists by email
    const user = await this.UserModel.findOne({ email });
    if (!user) {
      throw new UnauthorizedException('Wrong credentials');
    }

    //Compare entered password with existing password
    const passwordMatch = await bcrypt.compare(password, user.password);
    if (!passwordMatch) {
      throw new UnauthorizedException('Wrong credentials');
    }

    //Generate JWT tokens
    const tokens = await this.generateUserTokens(user._id);
    return {
      ...tokens,
      userId: user._id,
    };
  }
  async completeProfile(profileDto: CompleteProfileDto, usrId: string) {
    try {
        // Find the user by ID
        console.log('User ID:', usrId);
const user = await this.UserModel.findById(usrId);
console.log('User:', user);

        
        if (!user) {
            throw new NotFoundException('User not found');
        }

        // Update user profile with new information
        const updatedUser = await this.UserModel.findByIdAndUpdate(
            usrId,
            {
                fitnessGoal: profileDto.fitnessGoal,
                activityLevel: profileDto.activityLevel,
                height: profileDto.height,
                weight: profileDto.weight
            },
            { new: true } // Return the updated document
        );

        return {
            success: true,
            message: 'Profile completed successfully',
            user: {
                id: updatedUser._id,
                email: updatedUser.email,
                fullName: updatedUser.fullName,
                phoneNumber: updatedUser.phoneNumber,
                fitnessGoal: updatedUser.fitnessGoal,
                activityLevel: updatedUser.activityLevel,
                height: updatedUser.height,
                weight: updatedUser.weight
            }
        };

    } catch (error) {
        if (error instanceof NotFoundException) {
            throw error;
        }
        throw new InternalServerErrorException({
            success: false,
            message: 'Error completing profile',
            error: error.message
        });
    }
}
  async changePassword(userId, oldPassword: string, newPassword: string) {
    //Find the user
    const user = await this.UserModel.findById(userId);
    if (!user) {
      throw new NotFoundException('User not found...');
    }

    //Compare the old password with the password in DB
    const passwordMatch = await bcrypt.compare(oldPassword, user.password);
    if (!passwordMatch) {
      throw new UnauthorizedException('Wrong credentials');
    }

    //Change user's password
    const newHashedPassword = await bcrypt.hash(newPassword, 10);
    user.password = newHashedPassword;
    await user.save();
  }

  async forgotPassword(email: string) {
    //Check that user exists
    const user = await this.UserModel.findOne({ email });

    if (user) {
      //If user exists, generate password reset link
      const expiryDate = new Date();
      expiryDate.setHours(expiryDate.getHours() + 6);

      const resetToken = Math.floor(1000 + Math.random() * 9000);
      await this.ResetTokenModel.create({
        token: resetToken,
        userId: user._id,
        expiryDate: expiryDate, // Use the previously calculated expiry date
      });
      
      //Send the link to the user by email
      this.mailService.sendPasswordResetEmail(email, resetToken);
    }

    return { message: 'If this user exists, they will receive an email' };
  }

  async resetPassword(newPassword: string, resetToken: number) {
    console.log('Received reset token:', resetToken);
  
    // Find a valid reset token document
    const token = await this.ResetTokenModel.findOneAndDelete({
      token: resetToken,
      expiryDate: { $gte: new Date() },
    });
  
    console.log('Token from DB:', token);
    
    if (!token) {
      throw new UnauthorizedException('Invalid or expired reset token');
    }
  
    // Change user password (MAKE SURE TO HASH!!)
    const user = await this.UserModel.findById(token.userId);
    if (!user) {
      throw new InternalServerErrorException();
    }
  
    user.password = await bcrypt.hash(newPassword, 10);
    await user.save();
  }
  
  

  async refreshTokens(refreshToken: string) {
    const token = await this.RefreshTokenModel.findOne({
      token: refreshToken,
      expiryDate: { $gte: new Date() },
    });

    if (!token) {
      throw new UnauthorizedException('Refresh Token is invalid');
    }
    return this.generateUserTokens(token.userId);
  }

  async generateUserTokens(userId) {
    const accessToken = this.jwtService.sign({ userId }, { expiresIn: '10h' });
    const refreshToken = uuidv4();

    await this.storeRefreshToken(refreshToken, userId);
    return {
      accessToken,
      refreshToken,
    };
  }

  async storeRefreshToken(token: string, userId: string) {
    // Calculate expiry date 3 days from now
    const expiryDate = new Date();
    expiryDate.setDate(expiryDate.getDate() + 3);

    await this.RefreshTokenModel.updateOne(
      { userId },
      { $set: { expiryDate, token } },
      {
        upsert: true,
      },
    );
  }
   async verifyGoogleToken(idToken: string) {
    try {
      const ticket = await this.oauthClient.verifyIdToken({
        idToken,
        audience: process.env.GOOGLE_CLIENT_ID,
      });
      const payload = ticket.getPayload();

      if (!payload) {
        throw new UnauthorizedException('Invalid Google Token');
      }
      return payload;
    } catch (error) {
      throw new UnauthorizedException('Google token verification failed');
    }
  }

  /**
   * Handles Google Sign-In and user creation.
   */
  async handleGoogleSignIn(idToken: string) {
    const payload = await this.verifyGoogleToken(idToken);
    console.log('Received Google ID Token:', idToken);

    // Check if user exists
    let user: User = await this.UserModel.findOne({ email: payload.email });

    if (!user) {
      // Create new user if not found
      user = await this.UserModel.create({
        email: payload.email,
        fullName: payload.name+payload.family_name,
        imageUrl: payload.picture,
      });
    }

    // Generate JWT tokens
    const accessToken = this.jwtService.sign({ userId: user.id });
    const refreshToken = this.jwtService.sign(
      { userId: user.id },
      { expiresIn: '7d' },
    );

    return {
      accessToken,
      refreshToken,
      user,
    };
  }
  
}
