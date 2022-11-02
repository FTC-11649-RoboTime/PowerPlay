package org.firstinspires.ftc.teamcode.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

//designated code as driver controlled
@TeleOp
public class tankDrive extends LinearOpMode{
    //init motors
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;

    DcMotor lift;
    Servo grabber;

    public void runOpMode(){
        //assigning config values to drive motor variables
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        //assigning config values to lift variables
        lift = hardwareMap.get(DcMotor.class, "liftMotor");
        grabber = hardwareMap.get(Servo.class, "grabber");

        //assigning directions to drive motors
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

            //init grabbing variables
            double liftPowerUp;
            double liftPowerDown;
            boolean grabberPower;
            boolean grabberToggle;

            //mapping movement variables to controller
            throttleL = 0.75*gamepad1.left_stick_y;
            throttleR = 0.75*gamepad1.right_stick_y;
            strafeL = 0.75*gamepad1.left_stick_x;

            liftPowerUp = gamepad1.left_trigger;
            liftPowerDown = -1*gamepad1.right_trigger;
            grabberPower = gamepad1.x;
            grabberToggle = false;

            //motor movement
            frontLeft.setPower(throttleL);
            frontRight.setPower(throttleR);
            backLeft.setPower(throttleL);
            backRight.setPower(throttleR);

            frontLeft.setPower(-strafeL);
            frontRight.setPower(strafeL);
            backLeft.setPower(strafeL);
            backRight.setPower(-strafeL);

            //lift movement code
            lift.setPower(liftPowerUp);
            lift.setPower(liftPowerDown);

            //sets position of servo depending on button press. Not toggleable.
            if (grabberPower && !grabberToggle){
                grabberToggle = true;
            }else if (grabberPower && grabberToggle){
                grabberToggle = false;
            }

            if (grabberToggle){
                grabber.setPosition(0);
            }else if(!grabberToggle){
                grabber.setPosition(0.75);
            }
        }
    }
}
