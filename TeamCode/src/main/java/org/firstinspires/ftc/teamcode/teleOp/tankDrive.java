package org.firstinspires.ftc.teamcode.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class tankDrive extends LinearOpMode{
    //init motors
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;

    public void runOpMode(){
        //assigning config values to motor variables
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        //on start run code
        waitForStart();
        while (opModeIsActive()) {

            //init movement variables
            double throttleL;
            double throttleR;
            double strafeL;
            double strafeR;

            throttleL = 0.75*gamepad1.left_stick_y;
            throttleR = 0.75*gamepad1.right_stick_y;
            strafeL = 0.75*gamepad1.left_stick_x;
            strafeR = 0.75*gamepad1.right_stick_x;

            //mapping movement variables to controller
            frontLeft.setPower(throttleL);
            frontRight.setPower(throttleR);
            backLeft.setPower(throttleL);
            backRight.setPower(throttleR);

            frontLeft.setPower(-strafeL);
            frontRight.setPower(strafeL);
            backLeft.setPower(strafeL);
            backRight.setPower(-strafeL);
        }
    }
}