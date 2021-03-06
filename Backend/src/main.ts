import 'dotenv/config';
import { NestFactory } from '@nestjs/core';
import { ValidationPipe } from '@nestjs/common';
import { AppModule } from './app.module';
const cors = require('cors');
const logger = require('morgan');

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.useGlobalPipes(new ValidationPipe({ forbidUnknownValues: true }));
  app.use(logger('dev', true));
  app.use(cors());
  app.getHttpAdapter().getInstance().set('etag', false);
  await app.listen(process.env.PORT);
}
bootstrap();
