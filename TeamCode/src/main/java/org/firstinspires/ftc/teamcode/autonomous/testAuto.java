package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous
public class testAuto extends LinearOpMode {
    //init webcam
    OpenCvCamera webcam;
    //init drive motors
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;

    //init lift motor and servo
    DcMotor lift;
    Servo grabber;

    //init gyroscope
    BNO055IMU gyro;
    Orientation angles;

    public void runOpMode(){
        //assigning drive motor variables to config names
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        //setting direction to motors
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        //assigning lift motor and servo variables to config names
        lift = hardwareMap.get(DcMotor.class, "liftMotor");
        grabber = hardwareMap.get(Servo.class, "grabber");

        //assigning imu variable to config name
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit            = BNO055IMU.AngleUnit.DEGREES;
        parameters.calibrationDataFile  = "BNO055IMUCalibration.json";
        parameters.loggingEnabled       = true;
        parameters.loggingTag           = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        gyro = hardwareMap.get(BNO055IMU.class, "imu");
        gyro.initialize(parameters);

        //init OpenCv
        int cameraMonitorViewId = hardwareMap.appContext
                            .getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        TeamSleeveDetector detector = new TeamSleeveDetector(telemetry);
        webcam.setPipeline(detector);
    }

    /*
    Methods
    ------------------------------------------------------------------------------------------------------
     */
    //Movement Methods
    private void stopMotors(){
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }
    private void move(double power, int time){
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);
        sleep(time);
        stopMotors();
    }
    private void strafeLeft(int time){
        frontLeft.setPower(-0.5);
        backLeft.setPower(0.5);
        frontRight.setPower(0.5);
        backRight.setPower(-0.5);
    }
    private void strafeRight(int time){
        frontLeft.setPower(0.5);
        backLeft.setPower(-0.5);
        frontRight.setPower(-0.5);
        backRight.setPower(0.5);
    }
    private boolean gyroturn(double targetAngle){
        boolean angleReached = false;
        while (true) {
            angles = gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            double currentAngle = angles.thirdAngle;
            if (angles.firstAngle >= targetAngle - 0.5 && angles.firstAngle <= targetAngle + 0.5) {
                frontLeft.setPower(0);
                frontRight.setPower(0);
                backLeft.setPower(0);
                backRight.setPower(0);
                //breaks out of loop once at 90 degrees
                sleep(500);
                angleReached = true;
                return angleReached;
            } else if (angles.firstAngle >= targetAngle + 0.5) {
                if (angles.firstAngle <= targetAngle + 10) {
                    frontLeft.setPower(-0.15);
                    frontRight.setPower(0.15);
                    backLeft.setPower(-0.15);
                    backRight.setPower(0.15);
                    angleReached = false;
                } else {
                    frontLeft.setPower(-0.5);
                    frontRight.setPower(0.5);
                    backLeft.setPower(-0.5);
                    backRight.setPower(0.5);
                    angleReached = false;
                }
            } else if (angles.firstAngle <= targetAngle - 0.5) {
                if (angles.firstAngle >= targetAngle - 10) {
                    frontLeft.setPower(0.15);
                    frontRight.setPower(-0.15);
                    backLeft.setPower(0.15);
                    backRight.setPower(-0.15);
                    angleReached = false;
                } else {
                    frontLeft.setPower(0.5);
                    frontRight.setPower(-0.5);
                    backLeft.setPower(0.5);
                    backRight.setPower(-0.5);
                    angleReached = false;
                }
            }
            if (angleReached == true){
                return angleReached;
            }
        }
    }
    private void liftMotor(double power, int ticks){
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lift.setTargetPosition(ticks);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lift.setPower(power);
    }
}
