import { User } from "../models/user.schema";
import { Injectable, HttpException, HttpStatus } from '@nestjs/common';
import { RegisterDto } from '../auth/dto/register.dto';
import { ModelType } from 'typegoose';
import { InjectModel } from "nestjs-typegoose";
import { BaseRepository } from "../shared/repository/base.service";
import * as bcrypt from 'bcrypt';

const ObjectId = require('mongoose').Types.ObjectId;

@Injectable()
export class UserRepository extends BaseRepository<User>  {
    constructor(
        @InjectModel(User) private readonly _userModel: ModelType<User>
    ) {
        super();
        this._Model = _userModel;
    }

    async findByEmailORUserName(identifier: string) {
        return await this.findOne({ $or: [{ email: identifier }, { userName: identifier }] });
    }
    async findByUserName(userName: string) {
        return await this.findOne({ userName: userName });
    }

    async createUser(userData: RegisterDto) {
        return await this.create(userData);
    }



    async updateUserData(id: any, updateInfo: {
        userName?: string;
        password?: string;
        firstName?: string;
        lastName?: string;
        oldPassword?: string;
        brithDay?: string;
    }): Promise<boolean> {
        const user = await this.findByID(id);
        if (updateInfo.userName && updateInfo.userName != user.userName)
            if (updateInfo.userName && await this.findByUserName(updateInfo.userName)) throw new HttpException('This userName exists', HttpStatus.BAD_REQUEST);
        if (updateInfo.password && !updateInfo.oldPassword) throw new HttpException('To update password should enter password', HttpStatus.BAD_REQUEST);
        else if (updateInfo.password && updateInfo.oldPassword)
            if (! await bcrypt.compare(updateInfo.oldPassword, user.password)) throw new HttpException('old password is not correct', HttpStatus.FORBIDDEN);
        updateInfo.oldPassword = undefined
        return await this.update(id, updateInfo)
    }

    async getUserById(userId) {
        const user = await this.findByID(userId);
        if (!user)
            throw new HttpException('Not user', HttpStatus.BAD_REQUEST);
        return user;
    }

    async updateScoreInDoing(userId, data: {
        date: string,
        Score: number,
        emotions: {  // TODO add all emotions we will recogonized
            "correctSad": number, "errorSad": number,
            "correctHappy": number, "errorHappy": number,
            "correctNatural": number, "errorNatural": number,
            "correctFraid": number, "errorFraid": number,
        }
    }) {
        const user = await this.getUserById(userId);
        if (!user.ScoreInDoing)
            await this.update(userId, { ScoreInDoing: {} })
        if (!user.ScoreInDoing[data.date]) {
            user.ScoreInDoing[data.date] = { Score: data.Score, emotions: data.emotions }
            await this.update(userId, { ScoreInDoing: user.ScoreInDoing })
        }
        else if (user.ScoreInDoing[data.date]) {
            user.ScoreInDoing[data.date].Score += data.Score;
            user.ScoreInDoing[data.date].emotions.correctSad += data.emotions.correctSad;
            user.ScoreInDoing[data.date].emotions.errorSad += data.emotions.errorSad;

            user.ScoreInDoing[data.date].emotions.correctHappy += data.emotions.correctHappy;
            user.ScoreInDoing[data.date].emotions.correctHappy += data.emotions.errorHappy;

            user.ScoreInDoing[data.date].emotions.correctNatural += data.emotions.correctNatural;
            user.ScoreInDoing[data.date].emotions.errorNatural += data.emotions.errorNatural;

            user.ScoreInDoing[data.date].emotions.correctFraid += data.emotions.correctFraid;
            user.ScoreInDoing[data.date].emotions.errorFraid += data.emotions.errorFraid;

            await this.update(userId, { ScoreInDoing: user.ScoreInDoing })
        }

    }
    async updateScoreInRecognize(userId, data: {
        date: string,
        Score: number,
        emotions: {  // TODO add all emotions we will recogonized
            "correctSad": number, "errorSad": number,
            "correctHappy": number, "errorHappy": number,
            "correctNatural": number, "errorNatural": number,
            "correctFraid": number, "errorFraid": number,
        }
    }) {
        const user = await this.getUserById(userId);
        if (!user.ScoreInRecognize)
            await this.update(userId, { ScoreInRecognize: {} })
        if (!user.ScoreInRecognize[data.date]) {
            user.ScoreInRecognize[data.date] = { Score: data.Score, emotions: data.emotions }
            await this.update(userId, { ScoreInRecognize: user.ScoreInRecognize })
        }
        else if (user.ScoreInRecognize[data.date]) {
            user.ScoreInRecognize[data.date].Score += data.Score;
            user.ScoreInRecognize[data.date].emotions.correctSad += data.emotions.correctSad;
            user.ScoreInRecognize[data.date].emotions.errorSad += data.emotions.errorSad;

            user.ScoreInRecognize[data.date].emotions.correctHappy += data.emotions.correctHappy;
            user.ScoreInRecognize[data.date].emotions.correctHappy += data.emotions.errorHappy;

            user.ScoreInRecognize[data.date].emotions.correctNatural += data.emotions.correctNatural;
            user.ScoreInRecognize[data.date].emotions.errorNatural += data.emotions.errorNatural;

            user.ScoreInRecognize[data.date].emotions.correctFraid += data.emotions.correctFraid;
            user.ScoreInRecognize[data.date].emotions.errorFraid += data.emotions.errorFraid;

            await this.update(userId, { ScoreInRecognize: user.ScoreInRecognize })
        }
    }
}