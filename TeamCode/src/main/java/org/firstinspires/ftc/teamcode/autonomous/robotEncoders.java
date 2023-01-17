package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
@Disabled
public class robotEncoders extends LinearOpMode {
    private DcMotor lift;

    public void runOpMode() {
        lift = hardwareMap.get(DcMotor.class, "liftMotor");

        waitForStart();
        int target = 24;

//        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        double speed = gamepad1.left_stick_y;

        while (opModeIsActive()) {
            lift.setPower(speed);
            telemetry.addData("encoder value", lift.getCurrentPosition());
        }
    }
}
