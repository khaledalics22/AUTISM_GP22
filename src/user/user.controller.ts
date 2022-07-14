import { Controller, UseGuards, Get, Put, Request, Body, Query } from '@nestjs/common';
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
        "correctSad": string,
        "errorSad": string,
        "correctHappy": string,
        "errorHappy": string,
        "correctNatural": string,
        "errorNatural": string,
        "correctSurprise": string,
        "errorSurprise": string
    }) {
        let data_to = {
            date: data.date, emotions: {
                "correctSad": parseInt(data.correctSad), "errorSad": parseInt(data.errorSad),
                "correctHappy": parseInt(data.correctHappy), "errorHappy": parseInt(data.errorHappy),
                "correctNatural": parseInt(data.correctNatural), "errorNatural": parseInt(data.errorNatural),
                "correctSurprise": parseInt(data.correctSurprise), "errorSurprise": parseInt(data.errorSurprise),
            }
        }
        await this.userService.UpdateScoreToUserDO(req.user._id, data_to)
    }

    @UseGuards(AuthGuard('jwt'))
    @Put('/me/recognize')
    async updateMyRecognizing(@Request() req, @Body() data: {
        date: string,
        "correctSad": string,
        "errorSad": string,
        "correctHappy": string,
        "errorHappy": string,
        "correctNatural": string,
        "errorNatural": string,
        "correctSurprise": string,
        "errorSurprise": string
    }) {
        let data_to = {
            date: data.date, emotions: {
                "correctSad": parseInt(data.correctSad), "errorSad": parseInt(data.errorSad),
                "correctHappy": parseInt(data.correctHappy), "errorHappy": parseInt(data.errorHappy),
                "correctNatural": parseInt(data.correctNatural), "errorNatural": parseInt(data.errorNatural),
                "correctSurprise": parseInt(data.correctSurprise), "errorSurprise": parseInt(data.errorSurprise),
            }
        }
        await this.userService.UpdateScoreToUserRecognize(req.user._id, data_to);
    }

    @UseGuards(AuthGuard('jwt'))
    @Get('/me/recognize')
    async getMyRecognizing(@Request() req, @Query() data: {
        emotion: string,
        all: string
    }) {
        let data_to = { emotion: data.emotion, all: data.all == "true" ? true : false }
        return await this.userService.getScoreToUserRecognize(req.user._id, data_to);
    }

    @UseGuards(AuthGuard('jwt'))
    @Get('/me/doing')
    async getMyDoing(@Request() req, @Query() data: {
        emotion: string,
        all: string
    }) {
        let data_to = { emotion: data.emotion, all: data.all == "true" ? true : false }

        return await this.userService.getScoreToUserDoing(req.user._id, data_to);
    }

    @Get('/recognize')
    async getRecognizing(@Query() data: {
        emotion: string,
        all: string
    }) {
        let data_to = { emotion: data.emotion, all: data.all == "true" ? true : false }

        return await this.userService.getStatisticsRecognize(data_to);
    }

    @Get('/doing')
    async getDoing(@Query() data: {
        emotion: string,
        all: string
    }) {
        let data_to = { emotion: data.emotion, all: data.all == "true" ? true : false }
        return await this.userService.getStatisticsDoing(data_to);
    }

    @Get('/users')
    async all(): Promise<User[] | null> {
        return await this.userService.findAllUsers();
    }


}
