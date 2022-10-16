package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class testOpMode extends LinearOpMode {

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
            double throttle;
            double turn;

            double strafe;

            //mapping movement variables to controller.
            throttle = gamepad1.left_stick_y;
            turn = gamepad1.right_stick_x;

            strafe = gamepad1.left_stick_x;

            //movement code
            frontLeft.setPower(throttle);
            frontRight.setPower(throttle);
            backLeft.setPower(throttle);
            backRight.setPower(throttle);

            frontLeft.setPower(-turn);
            frontRight.setPower(turn);
            backLeft.setPower(-turn);
            backRight.setPower(turn);

            frontLeft.setPower(-strafe);
            frontRight.setPower(-strafe);
            backLeft.setPower(strafe);
            backRight.setPower(strafe);
        }
    }
}
