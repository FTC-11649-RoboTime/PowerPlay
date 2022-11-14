package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.autonomous.TeamSleeveDetector;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;



@Autonomous
public class autono extends LinearOpMode {
    OpenCvWebcam webcam;
    robotClass robot = new robotClass();
    @Override
    public void runOpMode() throws InterruptedException {
        //init robot
        robot.init(hardwareMap);

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

            //raise the cone out of the way
            robot.liftMotor(1, 24);

            //determining where the tse is
            switch (detector.getLocation()) {
                case TOP:
                    locationOfTSE = "left";
                    break;
                case MIDDLE:
                    locationOfTSE = "middle";
                    break;
                case BOTTOM:
                    locationOfTSE = "right";
                    break;
                case NOT_FOUND:
                    locationOfTSE = "not Found";
                    break;
            }


            //moveing crane to right position
            if (locationOfTSE == "top") {

                telemetry.addLine("top");
                telemetry.update();
                sleep(500);
            } else if (locationOfTSE == "middle") {
                telemetry.addLine("Middle");
                telemetry.update();
                sleep(500);
            } else if (locationOfTSE == "bottom") {
                telemetry.addLine("bottom");
                telemetry.update();
                sleep(300);
            } else if (locationOfTSE == "not Found") {
            }
        }
        webcam.stopStreaming();
    }
}
