package org.firstinspires.ftc.teamcode.autonomous;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;
import org.opencv.core.Rect;

public class TeamSleeveDetector extends OpenCvPipeline {
    Telemetry telemetry;
    Mat mat = new Mat();

    public enum Location {
        TOP,
        MIDDLE,
        BOTTOM,
        NOT_FOUND
    }
    private Location location;

    static final Rect TOP_ROI = new Rect(
            new Point(0, 0),
            new Point(320, 50));

    static final Rect MID_ROI = new Rect(
            new Point(0, 50),
            new Point(320, 80));

    static final Rect BOTTOM_ROI = new Rect(
            new Point(0, 80),
            new Point(320, 110));

    static double percentThreshold = 0.05;

    public TeamSleeveDetector(Telemetry t){
        telemetry = t;
    }

    @Override
    public Mat processFrame(Mat input){
        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);

        // Yellowish
        Scalar yellowLowHSV = new Scalar(23,70,70);
        Scalar yellowHighHSV = new Scalar(32, 255, 255);

        // Orangish
        Scalar orangeLowHSV = new Scalar(0, 59, 107);
        Scalar orangeHighHSV = new Scalar(71, 183, 255);

        //green
        Scalar greenLowHSV = new Scalar(40,40,40);
        Scalar greenHighHSV = new Scalar(70,255,255);

        Core.inRange(mat, greenLowHSV, greenHighHSV, mat);

        Mat top = mat.submat(TOP_ROI);
        Mat mid = mat.submat(MID_ROI);
        Mat bottom = mat.submat(BOTTOM_ROI);

        double leftValue = Core.sumElems(top).val[0] / TOP_ROI.area() / 100;
        double midValue = Core.sumElems(mid).val[0] / MID_ROI.area() / 100;
        double rightValue = Core.sumElems(bottom).val[0] / BOTTOM_ROI.area() / 100;

        mid.release();

        telemetry.addData("raw Mid value", (int) Core.sumElems(mid).val[0]);
        telemetry.addData("Mid percentage", Math.round(midValue * 100) + "%");

        boolean tseLeft = leftValue > percentThreshold;
        boolean tseMid = midValue > percentThreshold;
        boolean tseRight = rightValue > percentThreshold;

        if (tseLeft){
            location = Location.TOP;
            telemetry.addData("Location", "Top");
        }else if (tseMid){
            location = Location.MIDDLE;
            telemetry.addData("Location", "Middle");
        }else if (tseRight) {
            location = Location.BOTTOM;
            telemetry.addData("Location", "Bottom");
        }else{
            location = Location.NOT_FOUND;
            telemetry.addData("Location", "not found");
        }
        telemetry.update();

        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2RGB);

        Scalar tseColor = new Scalar(255,0,0);

        Imgproc.rectangle(mat, TOP_ROI, tseColor);
        Imgproc.rectangle(mat, MID_ROI, tseColor);
        Imgproc.rectangle(mat, BOTTOM_ROI, tseColor);
        return mat;
    }
    public Location getLocation() {
        return location;
    }
}