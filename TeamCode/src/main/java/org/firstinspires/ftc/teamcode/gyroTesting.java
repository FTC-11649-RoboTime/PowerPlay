/*
    This code will turn the robot 90 degrees.
    You must initialize the gyroscope in the configuration on the driver hub
    To do this you must:
        * Go to the configuration on the driver hub
        * On the config go to the control hub I2C bus 0
        * Set name to imu
*/
package org.firstinspires.ftc.teamcode;

import android.graphics.drawable.GradientDrawable;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@TeleOp
@Disabled
public class gyroTesting extends LinearOpMode {

    //init motor variables
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;

    //init gyroscope
    BNO055IMU gyro;
    Orientation angles;

    public void runOpMode(){
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit            = BNO055IMU.AngleUnit.DEGREES;
        parameters.calibrationDataFile  = "BNO055IMUCalibration.json";
        parameters.loggingEnabled       = true;
        parameters.loggingTag           = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        gyro = hardwareMap.get(BNO055IMU.class, "imu");
        gyro.initialize(parameters);

        //assigning config values to motor variables
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        //setting motor direction
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        waitForStart();
        while (opModeIsActive()) {
            angles = gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            double targetAngle = -90;
            double currentAngle = angles.thirdAngle;
            if (angles.firstAngle >= targetAngle - 0.5 && angles.firstAngle <= targetAngle + 0.5) {
                frontLeft.setPower(0);
                frontRight.setPower(0);
                backLeft.setPower(0);
                backRight.setPower(0);
                //breaks out of loop once at 90 degrees

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
    }
}
