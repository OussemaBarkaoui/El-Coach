import { IsNumber, IsString } from "class-validator"

export class CompleteProfileDto{
    @IsString()
    fitnessGoal:string
    @IsString()
    activityLevel:string
    @IsNumber()
    height:number
    @IsNumber()
    weight:number

}