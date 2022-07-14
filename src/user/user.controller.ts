import { Controller, UseGuards, Get, Put, Request, Body } from '@nestjs/common';
import { AuthGuard } from '@nestjs/passport';
import { UserService } from './user.service';
import { User } from "../models/user.schema";


@Controller('')
export class UserController {
    constructor(
        private userService: UserService
    ) { }

    @UseGuards(AuthGuard('jwt'))
    @Get('/me')
    async me(@Request() req) {
        const user = await this.userService.getUserByID(req.user._id);
        return user;
    }


    @UseGuards(AuthGuard('jwt'))
    @Put('/me')
    async updateMyData(@Request() req, @Body() updateData: {
        userName?: string;
        password?: string;
        firstName?: string;
        lastName?: string;
        oldPassword?: string;
        brithDay?: string;
    }) {

        await this.userService.updateData(req.user._id, updateData)
    }


    @UseGuards(AuthGuard('jwt'))
    @Put('/me/doing')
    async updateMyDoing(@Request() req, @Body() data: {
        date: string,
        correctSad: number,
        "errorSad": number,
        "correctHappy": number,
        "errorHappy": number,
        "correctNatural": number,
        "errorNatural": number,
        "correctSurprise": number,
        "errorSurprise": number
    }) {
        let data_to = {
            date: data.date, emotions: {
                "correctSad": data.correctSad, "errorSad": data.errorSad,
                "correctHappy": data.correctHappy, "errorHappy": data.errorHappy,
                "correctNatural": data.correctNatural, "errorNatural": data.errorNatural,
                "correctSurprise": data.correctSurprise, "errorSurprise": data.errorSurprise,
            }
        }

        await this.userService.UpdateScoreToUserDO(req.user._id, data_to)
    }

    @UseGuards(AuthGuard('jwt'))
    @Put('/me/recognize')
    async updateMyRecognizing(@Request() req, @Body() data: {
        date: string,
        correctSad: number,
        "errorSad": number,
        "correctHappy": number,
        "errorHappy": number,
        "correctNatural": number,
        "errorNatural": number,
        "correctSurprise": number,
        "errorSurprise": number
    }) {
        let data_to = {
            date: data.date, emotions: {
                "correctSad": data.correctSad, "errorSad": data.errorSad,
                "correctHappy": data.correctHappy, "errorHappy": data.errorHappy,
                "correctNatural": data.correctNatural, "errorNatural": data.errorNatural,
                "correctSurprise": data.correctSurprise, "errorSurprise": data.errorSurprise,
            }
        }
        await this.userService.UpdateScoreToUserRecognize(req.user._id, data_to);
    }

    @UseGuards(AuthGuard('jwt'))
    @Get('/me/recognize')
    async getMyRecognizing(@Request() req, @Body() data: {
        emotion: string,
        all: boolean
    }) {

        return await this.userService.getScoreToUserRecognize(req.user._id, data);
    }

    @UseGuards(AuthGuard('jwt'))
    @Get('/me/doing')
    async getMyDoing(@Request() req, @Body() data: {
        emotion: string,
        all: boolean
    }) {

        return await this.userService.getScoreToUserDoing(req.user._id, data);
    }

    @Get('/recognize')
    async getRecognizing(@Body() data: {
        emotion: string,
        all: boolean
    }) {

        return await this.userService.getStatisticsRecognize(data);
    }

    @Get('/doing')
    async getDoing(@Body() data: {
        emotion: string,
        all: boolean
    }) {

        return await this.userService.getStatisticsDoing(data);
    }

    @Get('/users')
    async all(): Promise<User[] | null> {
        return await this.userService.findAllUsers();
    }


}
