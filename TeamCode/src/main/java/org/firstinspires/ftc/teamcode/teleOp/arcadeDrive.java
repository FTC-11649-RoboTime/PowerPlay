package org.firstinspires.ftc.teamcode.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

//designated code as driver controlled
@TeleOp
public class arcadeDrive extends LinearOpMode {
    //init drive motors
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;

    //init intake motors
    DcMotor lift;
    Servo grabber;

    public void runOpMode(){
        //assigning config values to motor variables
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        lift = hardwareMap.get(DcMotor.class, "liftMotor");
        grabber = hardwareMap.get(Servo.class, "grabber");

        //Assigning direction to drivetrain motors
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        //on start run code
        waitForStart();
        grabber.setPosition(0);
        while (opModeIsActive()) {

            //init controller variables
            double throttle;
            double turn;
            double strafe;

            double liftPowerUp;
            double liftPowerDown;
            boolean grabberPower;

            //mapping movement variables to controller.
            throttle = gamepad1.left_stick_y;
            turn = gamepad1.right_stick_x;
            strafe = gamepad1.left_stick_x;

            liftPowerUp = gamepad1.left_trigger;
            liftPowerDown = -1*gamepad1.right_trigger;
            grabberPower = gamepad1.x;

            //movement code
            frontLeft.setPower(0.75*throttle);
            frontRight.setPower(0.75*throttle);
            backLeft.setPower(0.75*throttle);
            backRight.setPower(0.75*throttle);

            frontLeft.setPower(0.5*turn);
            frontRight.setPower(-0.5*turn);
            backLeft.setPower(0.5*turn);
            backRight.setPower(-0.5*turn);

            frontLeft.setPower(-0.75*strafe);
            frontRight.setPower(0.75*strafe);
            backLeft.setPower(0.75*strafe);
            backRight.setPower(-0.75*strafe);

            //lift movement code
            lift.setPower(liftPowerUp);
            lift.setPower(liftPowerDown);

            //sets position of servo depending on button press. Not toggleable.
            if (grabberPower){
                grabber.setPosition(0.8);
            }else{
                grabber.setPosition(0.4);
            }
//            grabber.setPosition(gamepad2.left_stick_y);
//            telemetry.addData("ServoPosition", gamepad2.left_stick_y);
//            telemetry.update();
        }
    }
}
