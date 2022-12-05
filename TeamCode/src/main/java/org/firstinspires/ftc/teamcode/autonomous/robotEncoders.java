package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class robotEncoders extends LinearOpMode {
    private DcMotor lift;

    public void runOpMode() {
        lift = hardwareMap.get(DcMotor.class, "liftMotor");

        waitForStart();
        int target = 24;

        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        while (opModeIsActive()) {
            lift.setTargetPosition(target);
            lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            lift.setPower(0.5);
            while (lift.isBusy()){
                telemetry.addData("Lift is busy", lift.isBusy());
            }
            lift.setPower(0);
        }
    }
}
