import { Test, TestingModule } from '@nestjs/testing';
import { DietProgramController } from './diet_program.controller';
import { DietProgramService } from './diet_program.service';

describe('DietProgramController', () => {
  let controller: DietProgramController;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      controllers: [DietProgramController],
      providers: [DietProgramService],
    }).compile();

    controller = module.get<DietProgramController>(DietProgramController);
  });

  it('should be defined', () => {
    expect(controller).toBeDefined();
  });
});
