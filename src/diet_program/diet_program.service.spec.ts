import { Test, TestingModule } from '@nestjs/testing';
import { DietProgramService } from './diet_program.service';

describe('DietProgramService', () => {
  let service: DietProgramService;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [DietProgramService],
    }).compile();

    service = module.get<DietProgramService>(DietProgramService);
  });

  it('should be defined', () => {
    expect(service).toBeDefined();
  });
});
