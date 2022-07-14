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
        emotions: {  // TODO add all emotions we will recogonized
            "correctSad": number, "errorSad": number,
            "correctHappy": number, "errorHappy": number,
            "correctNatural": number, "errorNatural": number,
            "correctSurprise": number, "errorSurprise": number,
        }
    }) {

        const user = await this.getUserById(userId);
        if (!user.ScoreInDoing)
            await this.update(userId, { ScoreInDoing: {} })
        if (!user.ScoreInDoing[data.date]) {
            let correct = (data.emotions.correctSad + data.emotions.correctHappy + data.emotions.correctNatural + data.emotions.correctSurprise);
            let error = (data.emotions.errorSad + data.emotions.errorHappy + data.emotions.errorNatural + data.emotions.errorSurprise);
            let score = 0;
            if (correct != 0 || error != 0)
                score = correct / (error + correct);
            user.ScoreInDoing[data.date] = {
                Score: score
                , emotions: data.emotions
            }
            await this.update(userId, { ScoreInDoing: user.ScoreInDoing })
        }
        else if (user.ScoreInDoing[data.date]) {
            user.ScoreInDoing[data.date].emotions.correctSad += data.emotions.correctSad;
            user.ScoreInDoing[data.date].emotions.errorSad += data.emotions.errorSad;

            user.ScoreInDoing[data.date].emotions.correctHappy += data.emotions.correctHappy;
            user.ScoreInDoing[data.date].emotions.errorHappy += data.emotions.errorHappy;

            user.ScoreInDoing[data.date].emotions.correctNatural += data.emotions.correctNatural;
            user.ScoreInDoing[data.date].emotions.errorNatural += data.emotions.errorNatural;

            user.ScoreInDoing[data.date].emotions.correctSurprise += data.emotions.correctSurprise;
            user.ScoreInDoing[data.date].emotions.errorSurprise += data.emotions.errorSurprise;

            let correct = (user.ScoreInDoing[data.date].emotions.correctSad + user.ScoreInDoing[data.date].emotions.correctHappy + user.ScoreInDoing[data.date].emotions.correctNatural + user.ScoreInDoing[data.date].emotions.correctSurprise);
            let error = (user.ScoreInDoing[data.date].emotions.errorSad + user.ScoreInDoing[data.date].emotions.errorHappy + user.ScoreInDoing[data.date].emotions.errorNatural + user.ScoreInDoing[data.date].emotions.errorSurprise);
            let score = 0;
            if (correct != 0 || error != 0)
                score = correct / (error + correct);
            user.ScoreInDoing[data.date].Score += score;
            await this.update(userId, { ScoreInDoing: user.ScoreInDoing })
        }

    }
    async updateScoreInRecognize(userId, data: {
        date: string,
        emotions: {  // TODO add all emotions we will recogonized
            "correctSad": number, "errorSad": number,
            "correctHappy": number, "errorHappy": number,
            "correctNatural": number, "errorNatural": number,
            "correctSurprise": number, "errorSurprise": number,
        }
    }) {
        const user = await this.getUserById(userId);
        if (!user.ScoreInRecognize)
            await this.update(userId, { ScoreInRecognize: {} })
        if (!user.ScoreInRecognize[data.date]) {
            let correct = (data.emotions.correctSad + data.emotions.correctHappy + data.emotions.correctNatural + data.emotions.correctSurprise);
            let error = (data.emotions.errorSad + data.emotions.errorHappy + data.emotions.errorNatural + data.emotions.errorSurprise);
            let score = 0;
            if (correct != 0 || error != 0)
                score = correct / (error + correct);
            user.ScoreInRecognize[data.date] = {
                Score: score
                , emotions: data.emotions
            }
            await this.update(userId, { ScoreInRecognize: user.ScoreInRecognize })
        }
        else if (user.ScoreInRecognize[data.date]) {
            user.ScoreInRecognize[data.date].emotions.correctSad += data.emotions.correctSad;
            user.ScoreInRecognize[data.date].emotions.errorSad += data.emotions.errorSad;

            user.ScoreInRecognize[data.date].emotions.correctHappy += data.emotions.correctHappy;
            user.ScoreInRecognize[data.date].emotions.errorHappy += data.emotions.errorHappy;

            user.ScoreInRecognize[data.date].emotions.correctNatural += data.emotions.correctNatural;
            user.ScoreInRecognize[data.date].emotions.errorNatural += data.emotions.errorNatural;

            user.ScoreInRecognize[data.date].emotions.correctSurprise += data.emotions.correctSurprise;
            user.ScoreInRecognize[data.date].emotions.errorSurprise += data.emotions.errorSurprise;

            let correct = (user.ScoreInRecognize[data.date].emotions.correctSad + user.ScoreInRecognize[data.date].emotions.correctHappy + user.ScoreInRecognize[data.date].emotions.correctNatural + user.ScoreInRecognize[data.date].emotions.correctSurprise);
            let error = (user.ScoreInRecognize[data.date].emotions.errorSad + user.ScoreInRecognize[data.date].emotions.errorHappy + user.ScoreInRecognize[data.date].emotions.errorNatural + user.ScoreInRecognize[data.date].emotions.errorSurprise);
            let score = 0;
            if (correct != 0 || error != 0)
                score = correct / (error + correct);
            user.ScoreInRecognize[data.date].Score += score;
            await this.update(userId, { ScoreInRecognize: user.ScoreInRecognize })
        }
    }

    async getStatisticsInRecognize(userId, data: {
        all: boolean,
        emotion: string
    }) {
        const user = await this.getUserById(userId);
        if (!user.ScoreInRecognize) {
            await this.update(userId, { ScoreInRecognize: {} })
            return { results: [] };
        }
        if (user.ScoreInRecognize == {})
            return { results: [] };
        if (data.emotion == "happy") {
            var results = []
            var test = 1
            for (let i in user.ScoreInRecognize) {
                let correct = user.ScoreInRecognize[i].emotions.correctHappy;
                let error = user.ScoreInRecognize[i].emotions.errorHappy;
                if (correct != 0 || error != 0)
                    results.push({ "test_number": test, "score": (correct / (correct + error)) });
                test++;
            }
            return { results: results };
        }
        if (data.emotion == "sad") {
            var results = []
            var test = 1
            for (let i in user.ScoreInRecognize) {
                let correct = user.ScoreInRecognize[i].emotions.correctSad;
                let error = user.ScoreInRecognize[i].emotions.errorSad;
                if (correct != 0 || error != 0)
                    results.push({ "test_number": test, "score": (correct / (correct + error)) });
                test++;
            }
            return { results: results };
        }
        if (data.emotion == "natural") {
            var results = []
            var test = 1
            for (let i in user.ScoreInRecognize) {
                let correct = user.ScoreInRecognize[i].emotions.correctNatural;
                let error = user.ScoreInRecognize[i].emotions.errorNatural;
                if (correct != 0 || error != 0)
                    results.push({ "test_number": test, "score": (correct / (correct + error)) });
                test++;
            }
            return { results: results };
        }
        if (data.emotion == "surprise") {
            var results = []
            var test = 1
            for (let i in user.ScoreInRecognize) {
                let correct = user.ScoreInRecognize[i].emotions.correctSurprise;
                let error = user.ScoreInRecognize[i].emotions.errorSurprise;
                if (correct != 0 || error != 0)
                    results.push({ "test_number": test, "score": (correct / (correct + error)) });
                test++;
            }
            return { results: results };
        }
        if (data.all == true) {
            var results = []
            var test = 1
            for (let i in user.ScoreInRecognize) {
                let score = user.ScoreInRecognize[i].Score;
                results.push({ "test_number": test, "score": score });
                test++;
            }
            return { results: results };
        }
    }

    async getStatisticsInDoing(userId, data: {
        all: boolean,
        emotion: string
    }) {
        const user = await this.getUserById(userId);
        if (!user.ScoreInDoing) {
            await this.update(userId, { ScoreInDoing: {} })
            return { results: [] };
        }
        if (user.ScoreInDoing == {})
            return { results: [] };
        if (data.emotion == "happy") {
            var results = []
            var test = 1
            for (let i in user.ScoreInDoing) {
                let correct = user.ScoreInDoing[i].emotions.correctHappy;
                let error = user.ScoreInDoing[i].emotions.errorHappy;
                if (correct != 0 || error != 0)
                    results.push({ "test_number": test, "score": (correct / (correct + error)) });
                test++;
            }
            return { results: results };
        }
        if (data.emotion == "sad") {
            var results = []
            var test = 1
            for (let i in user.ScoreInDoing) {
                let correct = user.ScoreInDoing[i].emotions.correctSad;
                let error = user.ScoreInDoing[i].emotions.errorSad;
                if (correct != 0 || error != 0)
                    results.push({ "test_number": test, "score": (correct / (correct + error)) });
                test++;
            }
            return { results: results };
        }
        if (data.emotion == "natural") {
            var results = []
            var test = 1
            for (let i in user.ScoreInDoing) {
                let correct = user.ScoreInDoing[i].emotions.correctNatural;
                let error = user.ScoreInDoing[i].emotions.errorNatural;
                if (correct != 0 || error != 0)
                    results.push({ "test_number": test, "score": (correct / (correct + error)) });
                test++;
            }
            return { results: results };
        }
        if (data.emotion == "surprise") {
            var results = []
            var test = 1
            for (let i in user.ScoreInDoing) {
                let correct = user.ScoreInDoing[i].emotions.correctSurprise;
                let error = user.ScoreInDoing[i].emotions.errorSurprise;
                if (correct != 0 || error != 0)
                    results.push({ "test_number": test, "score": (correct / (correct + error)) });
                test++;
            }
            return { results: results };
        }
        if (data.all == true) {
            var results = []
            var test = 1
            for (let i in user.ScoreInDoing) {
                let score = user.ScoreInDoing[i].Score;
                results.push({ "test_number": test, "score": score });
                test++;
            }
            return { results: results };
        }
    }

    async getOfALLStatisticsInDoing(data: {
        all: boolean,
        emotion: string
    }) {
        var Total_result = [];
        var count = [];
        const users = await this.findAll();
        for (let j = 0; j < users.length; j++) {
            var user = users[j];
            let result_ = (await this.getStatisticsInDoing(user._id, data));
            let result = result_.results;
            for (let i = 0; i < result.length; i++) {
                if (Total_result.length <= i) {
                    Total_result.push(result[i]);
                    count.push(1);
                }
                else {
                    Total_result[i].score += result[i].score;
                    count[i]++;
                }
            }
        }
        for (let i = 0; i < Total_result.length; i++)
            Total_result[i].score /= count[i];

        return { results: Total_result };
    }

    async getOfALLStatisticsInRecognize(data: {
        all: boolean,
        emotion: string
    }) {
        var Total_result = [];
        var count = [];
        const users = await this.findAll();
        for (let j = 0; j < users.length; j++) {
            var user = users[j];
            let result_ = await this.getStatisticsInRecognize(user._id, data);
            let result = result_.results;
            for (let i = 0; i < result.length; i++) {
                if (Total_result.length <= i) {
                    Total_result.push(result[i]);
                    count.push(1);
                }
                else {
                    Total_result[i].score += result[i].score;
                    count[i]++;
                }
            }
        }
        for (let i = 0; i < Total_result.length; i++)
            Total_result[i].score /= count[i];
        return { results: Total_result };
    }




}