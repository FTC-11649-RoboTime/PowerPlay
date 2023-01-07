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
        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        //on start run code
        boolean grabberToggle;
        grabberToggle = false;
        waitForStart();
//        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        while (opModeIsActive()) {

            //init movement variables
            double throttleL;
            double throttleR;
            double strafeL;

            //init grabbing variables
            boolean liftPowerUp;
            boolean liftPowerDown;
            boolean grabberPower;

            //mapping movement variables to controller
            throttleL = 0.75*gamepad1.left_stick_y;
            throttleR = 0.75*gamepad1.right_stick_y;
            strafeL = 0.75*gamepad1.left_stick_x;

            liftPowerUp = gamepad2.left_bumper;
            liftPowerDown = gamepad2.right_bumper;
            grabberPower = gamepad2.x;

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
            if (liftPowerUp) {
                lift.setPower(1);
            }else if (liftPowerDown) {
                lift.setPower(-1);
            }else {
                lift.setPower(0);
            }

            //stopping lift if it is at min
//            if (lift.getCurrentPosition() <= -1){
//                lift.setPower(0);
//            }/*else if (lift.getCurrentPosition())*/

            //sets position of servo depending on button press. Not toggleable.
            if (grabberPower){
                grabber.setPosition(1);
            }else{
                grabber.setPosition(0.4);
            }
            telemetry.addData("Grabber toggle", grabberToggle);
            telemetry.addData("Grabber button", grabberPower);
            telemetry.addData("Lift motor ticks", lift.getCurrentPosition());
            telemetry.update();
        }
    }
}
