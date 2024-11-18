import { Controller, Get, Post, Body, Patch, Param, Delete } from '@nestjs/common';
import { DietProgramService } from './diet_program.service';
import { CreateDietProgramDto } from './dto/create-diet_program.dto';
import { UpdateDietProgramDto } from './dto/update-diet_program.dto';

@Controller('diet-program')
export class DietProgramController {
  constructor(private readonly dietProgramService: DietProgramService) {}

  @Post()
  create(@Body() createDietProgramDto: CreateDietProgramDto) {
    return this.dietProgramService.create(createDietProgramDto);
  }

  @Get()
  findAll() {
    return this.dietProgramService.findAll();
  }

  @Get(':id')
  findOne(@Param('id') id: string) {
    return this.dietProgramService.findOne(+id);
  }

  @Patch(':id')
  update(@Param('id') id: string, @Body() updateDietProgramDto: UpdateDietProgramDto) {
    return this.dietProgramService.update(+id, updateDietProgramDto);
  }

  @Delete(':id')
  remove(@Param('id') id: string) {
    return this.dietProgramService.remove(+id);
  }
}
