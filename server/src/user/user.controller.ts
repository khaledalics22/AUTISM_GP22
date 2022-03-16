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
        user.password = undefined;
        return { user };
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
        Score: number,
        emotions: {  // TODO add all emotions we will recogonized
            "correctSad": number, "errorSad": number,
            "correctHappy": number, "errorHappy": number,
            "correctNatural": number, "errorNatural": number,
            "correctFraid": number, "errorFraid": number,
        }
    }) {

        await this.userService.UpdateScoreToUserDO(req.user._id, data)
    }

    @UseGuards(AuthGuard('jwt'))
    @Put('/me/recognize')
    async updateMyRecognizing(@Request() req, @Body() data: {
        date: string,
        Score: number,
        emotions: {  // TODO add all emotions we will recogonized
            "correctSad": number, "errorSad": number,
            "correctHappy": number, "errorHappy": number,
            "correctNatural": number, "errorNatural": number,
            "correctFraid": number, "errorFraid": number,
        }
    }) {

        await this.userService.UpdateScoreToUserRecognize(req.user._id, data)
    }

    @Get('/users')
    async all(): Promise<User[] | null> {
        return await this.userService.findAllUsers();
    }


}
