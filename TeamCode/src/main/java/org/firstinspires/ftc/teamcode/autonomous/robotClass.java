package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import static java.lang.Thread.sleep;

public class robotClass {

/*
INITIALIZATION CODE
 */
    private LinearOpMode myOpMode = null;

    public DcMotor frontLeft = null;
    public DcMotor backLeft = null;
    public DcMotor frontRight = null;
    public DcMotor backRight = null;
    public DcMotor crane = null;
    public DcMotor carousel = null;
    public DcMotor redCarousel = null;
    public Servo arm = null;

    public BNO055IMU imu = null;

    public Orientation angles;

    public robotClass (LinearOpMode opmode) {
        myOpMode = opmode;
    }

    public void init(HardwareMap ahsMap) {
        frontLeft = ahsMap.get(DcMotor.class, "frontLeft");
        frontRight = ahsMap.get(DcMotor.class, "frontRight");
        backLeft = ahsMap.get(DcMotor.class, "backLeft");
        backRight = ahsMap.get(DcMotor.class, "backRight");

        //Setting direction of motors.
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        crane = ahsMap.get(DcMotor.class, "liftMotor");
        arm = ahsMap.get(Servo.class, "grabber");

        crane.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        crane.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //gyroscope init
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu = ahsMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);


    }

/*
    MOVEMENT CODE
        No encoders are being used.
 */
    public boolean gyroTurning(double targetAngle) throws InterruptedException {
        boolean foundAngle = false;
        //while (opModeIsActive()) {
        while (foundAngle==false) {
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            double currentAngle = angles.thirdAngle;
            myOpMode.telemetry.addData("angle", angles.firstAngle);
            if (angles.firstAngle >= targetAngle - 0.5 && angles.firstAngle <= targetAngle + 0.5) {
                frontLeft.setPower(0);
                frontRight.setPower(0);
                backLeft.setPower(0);
                backRight.setPower(0);

                foundAngle=true;
                sleep(1000);
                //breaks out of loop once at 90 degrees
                myOpMode.telemetry.addData("angle", angles.firstAngle);

                break;
            } else if (angles.firstAngle >= targetAngle + 0.5) {
                if (angles.firstAngle <= targetAngle + 10) {
                    frontLeft.setPower(-0.15);
                    frontRight.setPower(0.15);
                    backLeft.setPower(-0.15);
                    backRight.setPower(0.15);
                    break;
                } else {
                    frontLeft.setPower(-0.5);
                    frontRight.setPower(0.5);
                    backLeft.setPower(-0.5);
                    backRight.setPower(0.5);
                }
            } else if (angles.firstAngle <= targetAngle - 0.5) {
                if (angles.firstAngle >= targetAngle - 10) {
                    frontLeft.setPower(0.15);
                    frontRight.setPower(-0.15);
                    backLeft.setPower(0.15);
                    backRight.setPower(-0.15);
                    break;
                } else {
                    frontLeft.setPower(0.5);
                    frontRight.setPower(-0.5);
                    backLeft.setPower(0.5);
                    backRight.setPower(-0.5);
                }
            }
        }
        return foundAngle;
    }
    public void stopMotors() throws InterruptedException {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }

    public void move(double power, int time) throws InterruptedException {
        frontLeft.setPower(-power);
        backLeft.setPower(-power);
        frontRight.setPower(-power);
        backRight.setPower(-power);
        sleep(time);
        stopMotors();
    }

    public void strafeLeft(double power, int time) throws InterruptedException {
        frontLeft.setPower(power);
        frontRight.setPower(-power);
        backLeft.setPower(-power);
        backRight.setPower(power);
        sleep(time);
        stopMotors();
    }

    public void strafeRight(double power, int time) throws InterruptedException {
        frontLeft.setPower(-power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(-power);
        sleep(time);
        stopMotors();
    }

    //Other methods
    public void liftMotor(double power, int ticks) throws InterruptedException {
        crane.setTargetPosition(ticks);
        crane.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        crane.setPower(-1 * power);
    }
    public void close() throws InterruptedException {
        arm.setPosition(1);
    }
    public void open() throws InterruptedException {
        arm.setPosition(-0.4);
    }
}