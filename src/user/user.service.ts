import { Injectable, HttpException, HttpStatus } from '@nestjs/common';
import { User } from "../models/user.schema";
import * as bcrypt from 'bcrypt';
import { UserRepository } from './user-repository.service';
import { LoginDto } from '../auth/dto/login.dto';

@Injectable()
export class UserService {
    constructor(
        private readonly UserRepository: UserRepository,
    ) {

    }
    async getUserByID(userID): Promise<User | null> {
        const user = await this.UserRepository.findOne({ _id: userID });
        if (!user)
            throw new HttpException('Unauthorized access', HttpStatus.UNAUTHORIZED);
        return user;
    }

    async createUser(createUserDto: {
        userName: string;
        password: string;
        email: string;
        firstName: string;
        lastName: string;
        brithDay: string;
        gander: string;
    }) {
        // validations
        if (await this.UserRepository.findByEmailORUserName(createUserDto.email))
            throw new HttpException('"email" should not have acount', HttpStatus.FORBIDDEN,);
        if (await this.UserRepository.findByEmailORUserName(createUserDto.userName))
            throw new HttpException('"userName"  is used before', HttpStatus.FORBIDDEN,);
        if (!createUserDto.email.includes('@') || !createUserDto.email.includes('.'))
            throw new HttpException('"email" should be in correct format', HttpStatus.BAD_REQUEST,);
        if (createUserDto.gander != 'f' && createUserDto.gander != 'm')
            throw new HttpException('"gander" should be in correct format f or m ', HttpStatus.BAD_REQUEST,);
        if (!createUserDto.userName || createUserDto.userName == 'f')
            throw new HttpException('"userName" required ', HttpStatus.BAD_REQUEST,);
        if (!createUserDto.firstName || createUserDto.firstName == 'f')
            throw new HttpException('"firstName" required ', HttpStatus.BAD_REQUEST,);
        if (!createUserDto.lastName || createUserDto.lastName == 'f')
            throw new HttpException('"lastName" required ', HttpStatus.BAD_REQUEST,);
        if (!createUserDto.brithDay || createUserDto.brithDay == 'f')
            throw new HttpException('"brithDay" required ', HttpStatus.BAD_REQUEST,);

        // save user
        const salt = await bcrypt.genSalt(10);
        let hash = await bcrypt.hash(createUserDto.password, salt);
        createUserDto.password = hash;
        return await this.UserRepository.createUser(createUserDto);
    }

    async updateData(userId: String, updateData: {
        userName?: string;
        password?: string;
        firstName?: string;
        lastName?: string;
        oldPassword?: string;
        brithDay?: string;
    }) {
        if (updateData.password) {
            const salt = await bcrypt.genSalt(10);
            let hash = await bcrypt.hash(updateData.password, salt);
            updateData.password = hash;
        }
        return await this.UserRepository.updateUserData(userId, updateData);
    }

    async findByLogin(loginDto: LoginDto) {
        const user = await this.UserRepository.findByEmailORUserName(loginDto.identifier);
        if (!user)
            throw new HttpException('not user by this identifier', HttpStatus.FORBIDDEN);
        if (await bcrypt.compare(loginDto.password, user.password)) return user;
        throw new HttpException('password is not correct', HttpStatus.FORBIDDEN);

    }
    async UpdateScoreToUserDO(userId, data: {
        date: string,
        emotions: {  // TODO add all emotions we will recogonized
            "correctSad": number, "errorSad": number,
            "correctHappy": number, "errorHappy": number,
            "correctNatural": number, "errorNatural": number,
            "correctSurprise": number, "errorSurprise": number,
        }

    }) {

        return this.UserRepository.updateScoreInDoing(userId, data)
    }

    async UpdateScoreToUserRecognize(userId, data: {
        date: string,
        emotions: {  // TODO add all emotions we will recogonized
            "correctSad": number, "errorSad": number,
            "correctHappy": number, "errorHappy": number,
            "correctNatural": number, "errorNatural": number,
            "correctSurprise": number, "errorSurprise": number,
        }

    }) {
        return this.UserRepository.updateScoreInRecognize(userId, data)
    }

    async getScoreToUserRecognize(userId, data: {
        emotion: string,
        all: boolean
    }) {
        return await this.UserRepository.getStatisticsInRecognize(userId, data);
    }

    async getStatisticsDoing(data: {
        emotion: string,
        all: boolean
    }) {
        return await this.UserRepository.getOfALLStatisticsInDoing(data);
    }

    async getStatisticsRecognize(data: {
        emotion: string,
        all: boolean
    }) {
        return await this.UserRepository.getOfALLStatisticsInRecognize(data);
    }

    async getScoreToUserDoing(userId, data: {
        emotion: string,
        all: boolean
    }) {
        return await this.UserRepository.getStatisticsInDoing(userId, data);
    }

    async findAllUsers(): Promise<User[] | null> {
        return await this.UserRepository.findAll();
    }

    async deleteUser(userID) {
        const user = await this.getUserByID(userID);
        if (!user)
            throw new HttpException('Unauthorized access', HttpStatus.UNAUTHORIZED);
        await this.UserRepository.delete(userID);
    }


}