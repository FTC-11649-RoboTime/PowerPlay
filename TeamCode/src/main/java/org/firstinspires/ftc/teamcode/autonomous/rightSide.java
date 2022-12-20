package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous (name="Right Side Autonomous")
public class rightSide extends LinearOpMode {
    OpenCvWebcam webcam;
    Servo grabber;
    robotClass robot = new robotClass(this);

    @Override
    public void runOpMode() throws InterruptedException {
        //init robot
        robot.init(hardwareMap);

        grabber = hardwareMap.get(Servo.class, "grabber");
        //refrence TeamShippingElementDector
        int cameraMonitorViewId = hardwareMap.appContext
                .getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        TeamSleeveDetector detector = new TeamSleeveDetector(telemetry);
        webcam.setPipeline(detector);
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });

        //grabber close
        robot.close();
        telemetry.addLine("closed");
//
        Thread.sleep(1000);
//
//            //raise the cone out of the way
        robot.liftMotor(1, -1900);

        waitForStart();

        //Starting values for varibles
        String locationOfTSE = "none";

        if (opModeIsActive()) {
            //determining where the tse is
            switch (detector.getLocation()) {
                case TOP:
                    locationOfTSE = "top";
                    break;
                case MIDDLE:
                    locationOfTSE = "middle";
                    break;
                case BOTTOM:
                    locationOfTSE = "bottom";
                    break;
                case NOT_FOUND:
                    locationOfTSE = "not Found";
                    break;
            }

            //strafe right
            robot.strafeLeft(0.5, 1200);

            Thread.sleep(1000);
            //move forward
            robot.move(0.8, 80\0);

            Thread.sleep(500);

//            robot.strafeLeft(0.5, 250);
            Thread.sleep(500);
            robot.gyroTurning(-55);
            Thread.sleep(1000);

            robot.move(0.25, 150);
            robot.open();

            robot.stopMotors();
            Thread.sleep(1000);

            //parking
            robot.strafeLeft(0.5, 700);

            if (locationOfTSE == "top") {

                telemetry.addLine("top");
                telemetry.update();
                sleep(500);

                robot.move(0.5, 1500);
            } else if (locationOfTSE == "middle") {
                telemetry.addLine("Middle");
                telemetry.update();
                sleep(500);

                robot.move(0.3, 1500);
            } else if (locationOfTSE == "bottom") {
                telemetry.addLine("bottom");
                telemetry.update();
                sleep(300);

//                robot.move(0.3, 1500);
            } else if (locationOfTSE == "not Found") {
                robot.move(0.5, 1500);

            }
        }
        webcam.stopStreaming();
    }
}
