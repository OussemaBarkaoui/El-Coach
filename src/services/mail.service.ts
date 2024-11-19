// mail.service.ts
import * as nodemailer from 'nodemailer';
import { Injectable } from '@nestjs/common';

@Injectable()
export class MailService {
  private transporter: nodemailer.Transporter;

  constructor() {
    this.transporter = nodemailer.createTransport({
      host: 'smtp.ethereal.email',
      port: 587,
      auth: {
        user: 'katharina.willms@ethereal.email',
        pass: 'Pb4CT53UmkewBeeZuM',
      },
    });
  }

  async sendPasswordResetEmail(email: string, resetCode: number): Promise<void> {
    const mailOptions = {
      from: 'Auth-backend service <bethel.fahey52@ethereal.email>',
      to: email,
      subject: 'Password Reset Code',
      text: `Your password reset code is: ${resetCode}`,
    };

    await this.transporter.sendMail(mailOptions);
  }
}
