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
public class leftSide extends LinearOpMode {
    OpenCvWebcam webcam;
    Servo grabber;
    robotClass robot = new robotClass(this);

    //flags
    boolean runOpenCVDetection = true;
    boolean park = true;
    boolean dropCone = true;

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



        waitForStart();

        //Starting values for varibles
        String locationOfTSE = "none";

        if (opModeIsActive()) {
            //grabber close
            robot.close();
            telemetry.addLine("closed");
//
            Thread.sleep(1000);
//
//            //raise the cone out of the way
            robot.liftMotor(1, -1900);

            Thread.sleep(5000);

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
            robot.strafeRight(0.5, 1200);

            Thread.sleep(1000);
            //move forward
            robot.move(0.75, 450);

            Thread.sleep(500);

//            robot.strafeLeft(0.5, 250);
            Thread.sleep(500);
            robot.gyroTurning(50);
            Thread.sleep(1000);

            robot.move(0.2, 200);
            robot.liftMotor(1, -1500);
            Thread.sleep(500);
            robot.open();

            robot.stopMotors();
            Thread.sleep(1000);

            //parking

            if (locationOfTSE == "top") {
                robot.strafeRight(0.5, 600);
//                robot.gyroTurning(0);
                telemetry.addLine("top");
                telemetry.update();
                sleep(500);

                robot.move(0.5, 1500);
            } else if (locationOfTSE == "middle") {
                robot.strafeRight(0.5, 600);
//                robot.gyroTurning(0);
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
