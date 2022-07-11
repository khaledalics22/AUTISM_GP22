import { Module } from '@nestjs/common';
import { AuthService } from './auth.service';
import { UserService } from '../user/user.service';
import { AuthController } from './auth.controller';
import { TypegooseModule } from "nestjs-typegoose";
import { User } from "../models/user.schema";
import { JwtStrategy } from './jwt.strategy';
import { SharedModule } from '../shared/shared.module';
import { UserRepository } from '../user/user-repository.service';


@Module({
  imports: [SharedModule,
    TypegooseModule.forFeature([User])

  ],
  providers: [
    AuthService,
    JwtStrategy,
    UserService,
    UserRepository
  ],
  controllers: [AuthController]
})
export class AuthModule { }

