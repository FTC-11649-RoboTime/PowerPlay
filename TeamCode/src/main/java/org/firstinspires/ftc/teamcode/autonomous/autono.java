package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
//import org.openftc.easyopencv.OpenCvCamera;
//import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous
public class autono extends LinearOpMode {

    OpenCvCamera webcam;

    @Override
    public void runOpMode() throws InterruptedException {
        //init robot
        robotClass robot = new robotClass(this);
        robot.init();

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
        String locationToPark = "none";

        if (opModeIsActive()) {
            //grabber close
            robot.close();

            //raise the cone out of the way
                //ticks may be wrong
            robot.liftMotor(1, 24);

            //determine where to park and save the location of th green band in a varible.
            switch (detector.getLocation()) {
                case TOP:
                    locationToPark = "top";
                    break;
                case MIDDLE:
                    locationToPark = "middle";
                    break;
                case BOTTOM:
                    locationToPark = "bottom";
                    break;
                case NOT_FOUND:
                    locationToPark = "not Found";
                    break;
            }

            //strafe over to corner -5in

            //move forward and line up

            //rotate 90 degrees to

            //moving into position by checking the variable
            if (locationToPark == "top") {

                telemetry.addLine("top");
                telemetry.update();
                sleep(500);
            } else if (locationToPark == "middle") {
                telemetry.addLine("Middle");
                telemetry.update();
                sleep(500);
            } else if (locationToPark == "bottom") {
                telemetry.addLine("bottom");
                telemetry.update();
                sleep(300);
            } else if (locationToPark == "not Found") {
                //if not found park in substation
            }
        }
        webcam.stopStreaming();
    }
}
