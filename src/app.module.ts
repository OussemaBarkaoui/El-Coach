import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { AuthModule } from './auth/auth.module';
import { MongooseModule } from '@nestjs/mongoose';
import { JwtModule } from '@nestjs/jwt';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { DietProgramModule } from './diet_program/diet_program.module';
import { RecommendationService } from './recommendation/recommendation.service';
import { RecommendationModule } from './recommendation/recommendation.module';
import config from './config/config';
import { RecommendationController } from './recommendation/recommendation.controller';
import { DietProgramService } from './diet_program/diet_program.service';
import { DietProgramController } from './diet_program/diet_program.controller';
import { WorkoutProgramModule } from './workout_program/workout_program.module';

@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true,
      cache: true,
      load: [config],
    }),
    JwtModule.registerAsync({
      imports: [ConfigModule],
      useFactory: async (config) => ({
       secret: config.get('jwt.secret'),
      }),
      global: true,
      inject: [ConfigService],
    }),
    MongooseModule.forRootAsync({
      imports: [ConfigModule],
      useFactory: async (config) => ({
        uri: config.get('database.connectionString'),
      }),
      inject: [ConfigService],
    }),
    AuthModule,
    DietProgramModule,
    RecommendationModule,
    WorkoutProgramModule,
    
  ],
  controllers: [AppController],
  providers: [AppService,],
})
export class AppModule {}
