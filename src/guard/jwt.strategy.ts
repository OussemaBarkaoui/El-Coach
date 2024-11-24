import { Injectable, UnauthorizedException } from '@nestjs/common';
import { PassportStrategy } from '@nestjs/passport';
import { Strategy, ExtractJwt } from 'passport-jwt';

@Injectable()
export class JwtStrategy extends PassportStrategy(Strategy, 'jwt') {
    constructor() {
        super({
            jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken(),
            ignoreExpiration: false,
            secretOrKey: process.env.JWT_SECRET || 'secret key', // Replace with your JWT secret
        });
    }

    async validate(payload: any) {
        console.log('JWT Payload:', payload); // Debug log
        if (!payload || !payload.userId) {
            throw new UnauthorizedException('Invalid token');
        }
        // Return a consistent object structure
        return { userId: payload.userId, email: payload.email };
    }
    
    
}
