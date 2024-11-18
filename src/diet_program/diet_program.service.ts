import { Injectable } from '@nestjs/common';
import { CreateDietProgramDto } from './dto/create-diet_program.dto';
import { UpdateDietProgramDto } from './dto/update-diet_program.dto';
import { InjectModel } from '@nestjs/mongoose';
import { DietProgram } from './entities/diet_program.entity';
import { Model } from 'mongoose';

@Injectable()
export class DietProgramService {
  constructor( @InjectModel(DietProgram.name) private dietProgramModel: Model<DietProgram>,){}
  async create(createDietProgramDto: CreateDietProgramDto):Promise<DietProgram> {
    return this.dietProgramModel.create(createDietProgramDto);
  }

  async findAll():Promise<DietProgram[]> {
    return this.dietProgramModel.find().exec();
  }

  async findOne(id: number):Promise<DietProgram> {
    return this.dietProgramModel.findById(id).exec();
  }

  async update(id: number, updateDietProgramDto: UpdateDietProgramDto):Promise<DietProgram> {
    return this.dietProgramModel.findByIdAndUpdate(id,updateDietProgramDto);
  }

  async remove(id: number) {
    return this.dietProgramModel.findByIdAndDelete(id);
  }
}
