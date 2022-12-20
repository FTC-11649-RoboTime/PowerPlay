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
        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        //on start run code
        waitForStart();
        grabber.setPosition(0);
        while (opModeIsActive()) {

            //init controller variables
            double throttle;
            double turn;
            boolean strafeLeft;
            boolean strafeRight;

            boolean liftPowerUp;
            boolean liftPowerDown;
            boolean grabberPower;

            //mapping movement variables to controller.
            throttle = gamepad1.left_stick_y;
            turn = gamepad1.right_stick_x;
            strafeLeft = gamepad1.left_bumper;
            strafeRight = gamepad1.right_bumper;

            liftPowerUp = gamepad2.left_bumper;
            liftPowerDown = gamepad2.right_bumper;
            grabberPower = gamepad2.x;

            //movement code
            frontLeft.setPower(0.75*throttle);
            frontRight.setPower(0.75*throttle);
            backLeft.setPower(0.75*throttle);
            backRight.setPower(0.75*throttle);

            frontLeft.setPower(-0.5*turn);
            frontRight.setPower(0.5*turn);//tobey is the best
            backLeft.setPower(-0.5*turn);
            backRight.setPower(0.5*turn);

            //lift movement code

            if (liftPowerUp) {
                lift.setPower(-0.75);
            }else if (liftPowerDown) {
                lift.setPower(0.75);
            }else {
                lift.setPower(0);
            }


            //sets position of servo depending on button press. Not toggleable.
            if (grabberPower){
                grabber.setPosition(1);
            }else{
                grabber.setPosition(0.4);
            }

            if (strafeLeft) {
                frontLeft.setPower(0.75);
                frontRight.setPower(-0.75);
                backLeft.setPower(-0.75);
                backRight.setPower(0.75);
            }else if (strafeRight) {
                frontLeft.setPower(-0.75);
                frontRight.setPower(0.75);
                backLeft.setPower(0.75);
                backRight.setPower(-0.75);
            }
//            grabber.setPosition(gamepad2.left_stick_y);
//            telemetry.addData("ServoPosition", gamepad2.left_stick_y);
//            telemetry.update();
        }
    }
}
