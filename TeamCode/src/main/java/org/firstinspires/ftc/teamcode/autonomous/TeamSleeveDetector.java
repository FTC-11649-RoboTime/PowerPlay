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

    //Setting the Regions Of Interest/ where the control hub is looking for pixels
    static final Rect TOP_ROI = new Rect(
            new Point(100, 150),
            new Point(200, 170));

    static final Rect MID_ROI = new Rect(
            new Point(100, 170),
            new Point(200, 190));

    static final Rect BOTTOM_ROI = new Rect(
            new Point(100, 190),
            new Point(200, 240));

    static double percentThreshold = 0.1;

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
        Scalar greenLowHSV = new Scalar(40, 30, 100);
        Scalar greenHighHSV = new Scalar(255, 255, 130);

        Core.inRange(mat, greenLowHSV, greenHighHSV, mat);

        Mat top = mat.submat(TOP_ROI);
        Mat mid = mat.submat(MID_ROI);
        Mat bottom = mat.submat(BOTTOM_ROI);

        double topValue = Core.sumElems(top).val[0] / TOP_ROI.area() / 100;
        double midValue = Core.sumElems(mid).val[0] / MID_ROI.area() / 100;
        double bottomValue = Core.sumElems(bottom).val[0] / BOTTOM_ROI.area() / 100;

        mid.release();

        boolean tsTop = topValue > percentThreshold;
        boolean tsMid = midValue > percentThreshold;
        boolean tsBottom = bottomValue > percentThreshold;

        if (tsTop){
            location = Location.TOP;
            telemetry.addData("Location", "Top");
        }else if (tsMid){
            location = Location.MIDDLE;
            telemetry.addData("Location", "Middle");
        }else if (tsBottom) {
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