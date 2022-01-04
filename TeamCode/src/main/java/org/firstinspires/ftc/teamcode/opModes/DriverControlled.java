package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "GudCode", group = "opModes")

public class DriverControlled extends LinearOpMode {

    // Chassis motors
    private DcMotorEx bob;
    private DcMotorEx dylan;
    private DcMotorEx larry;
    private DcMotorEx jerry;

    // Arm motors/servos
    private DcMotorEx barry;
    private Servo garry;
    private Servo sherry;

    // Other stuff
    private double leftJoy_y;
    private double rightJoy_y;
    private double leftJoy_x;
    private double rightJoy_x;
    private double deadzone = 0.01;

    private double clawCurrentPosition = 0.5;
    boolean was_pressed = false;

    private static final String VUFORIA_KEY =
            "AbskhHb/////AAABmb8nKWBiYUJ9oEFmxQL9H2kC6M9FzPa1acXUaS/H5wRkeNbpNVBJjDfcrhlTV2SIGc/lxBOtq9X7doE2acyeVOPg4sP69PQQmDVQH5h62IwL8x7BS/udilLU7MyX3KEoaFN+eR1o4FKBspsYrIXA/Oth+TUyrXuAcc6bKSSblICUpDXCeUbj17KrhghgcgxU6wzl84lCDoz6IJ9egO+CG4HlsBhC/YAo0zzi82/BIUMjBLgFMc63fc6eGTGiqjCfrQPtRWHdj2sXHtsjZr9/BpLDvFwFK36vSYkRoSZCZ38Fr+g3nkdep25+oEsmx30IkTYvQVMFZKpK3WWMYUWjWgEzOSvhh+3BOg+3UoxBJSNk";

    @Override
    public void runOpMode() throws InterruptedException {

        // Hardware mapping
        bob = hardwareMap.get(DcMotorEx.class, "front_left_motor");
        dylan = hardwareMap.get(DcMotorEx.class, "front_right_motor");
        larry = hardwareMap.get(DcMotorEx.class, "back_left_motor");
        jerry = hardwareMap.get(DcMotorEx.class, "back_right_motor");
        barry = hardwareMap.get(DcMotorEx.class, "swing_arm_motor");
        garry = hardwareMap.get(Servo.class, "wrist_joint");
        sherry = hardwareMap.get(Servo.class, "claw_servo");

        // Wait for the game to begin
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();

        dylan.setDirection((DcMotorSimple.Direction.FORWARD));
        jerry.setDirection(DcMotorSimple.Direction.FORWARD);
        bob.setDirection(DcMotorSimple.Direction.REVERSE);
        larry.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {
            double rx = this.gamepad1.right_stick_x;
            double ry = -this.gamepad1.right_stick_y;
            double lx = this.gamepad1.left_stick_x;
            double ly = -this.gamepad1.left_stick_y;

            // Calculate point on the circumference of the circle to use as the joystick location
            rightJoy_x = rx / Math.sqrt(((Math.pow(rx, 2)) + (Math.pow(ry, 2))));
            rightJoy_y = ry / Math.sqrt(((Math.pow(rx, 2)) + (Math.pow(ry, 2))));
            leftJoy_x = lx / Math.sqrt(((Math.pow(lx, 2)) + (Math.pow(ly, 2))));
            leftJoy_y = ly / Math.sqrt(((Math.pow(lx, 2)) + (Math.pow(ly, 2))));

            telemetry.addData("Right stick X", rightJoy_x);
            telemetry.addData("Right stick Y", rightJoy_y);

            telemetry.addData("Left stick X", leftJoy_x);
            telemetry.addData("Left stick Y", leftJoy_y);

            if (JoyIsActive("left")) {
                TurnPlacesNew(leftJoy_x, leftJoy_y, calculatedSpeed("left"));
                telemetry.addData("Turny things", 0);
            } else if (JoyIsActive("right")) {
                DrivePlaces(calculatedDirection(rightJoy_x, rightJoy_y), calculatedSpeed("right"));
                telemetry.addData("Drivy things", 0);
            } else {
                dylan.setPower(0);
                jerry.setPower(0);
                bob.setPower(0);
                larry.setPower(0);
            }

            if (this.gamepad1.dpad_right || this.gamepad1.dpad_left) {MoveClaw();}

            MoveArm();

            if (this.gamepad1.y)
            {
                ReInit();
            }

            telemetry.update();
        }
    }

    private void DrivePlaces (String direction, double speed)
    {
        switch (direction)
        {
            case "STOP":
                // Stop
                dylan.setPower(0);
                jerry.setPower(0);
                bob.setPower(0);
                larry.setPower(0);
                break;
            case "FORWARD":
                // Drive forward
                dylan.setPower(speed);
                jerry.setPower(speed);
                bob.setPower(speed);
                larry.setPower(speed);
                break;
            case "FORWARD/RIGHT":
                // Drive forward/right
                dylan.setPower(speed);
                jerry.setPower(0);
                bob.setPower(0);
                larry.setPower(speed);
                break;
            case "RIGHT":
                // Drive right
                dylan.setPower(speed);
                jerry.setPower(-speed);
                bob.setPower(-speed);
                larry.setPower(speed);
                break;
            case "BACKWARD/RIGHT":
                // Drive backward/right
                dylan.setPower(0);
                jerry.setPower(-speed);
                bob.setPower(-speed);
                larry.setPower(0);
                break;
            case "BACKWARD":
                // Drive backward
                dylan.setPower(-speed);
                jerry.setPower(-speed);
                bob.setPower(-speed);
                larry.setPower(-speed);
                break;
            case "BACKWARD/LEFT":
                // Drive backward/left
                dylan.setPower(-speed);
                jerry.setPower(0);
                bob.setPower(0);
                larry.setPower(-speed);
                break;
            case "LEFT":
                // Drive left
                dylan.setPower(-speed);
                jerry.setPower(speed);
                bob.setPower(speed);
                larry.setPower(-speed);
                break;
            case "FORWARD/LEFT":
                // Drive forward/left
                dylan.setPower(0);
                jerry.setPower(speed);
                bob.setPower(speed);
                larry.setPower(0);
                break;
        }
    }

    private void TurnPlacesNew (double Joy_x, double Joy_y, double speed)
    {
        //Forward
        if (Joy_x == 0 && Joy_y > 0)
        {
            dylan.setPower(speed);
            jerry.setPower(speed);
            bob.setPower(speed);
            larry.setPower(speed);
        }

        //Backward
        else if (Joy_x == 0 && Joy_y < 0)
        {
            dylan.setPower(-speed);
            jerry.setPower(-speed);
            bob.setPower(-speed);
            larry.setPower(-speed);
        }

        //Forward to the right
        else if (Joy_x >= 0 && Joy_y >= 0)
        {
            dylan.setPower(0);
            jerry.setPower(0);
            bob.setPower(speed);
            larry.setPower(speed);
        }
        //Backward to the right
        else if (Joy_x > 0 && Joy_y < 0)
        {
            dylan.setPower(0);
            jerry.setPower(0);
            bob.setPower(-speed);
            larry.setPower(-speed);
        }
        //Forward to the left
        else if (Joy_x <= 0 && Joy_y >= 0)
        {
            dylan.setPower(speed);
            jerry.setPower(speed);
            bob.setPower(0);
            larry.setPower(0);
        }
        //Backward to the left
        else if (Joy_x < 0 && Joy_y < 0)
        {
            dylan.setPower(-speed);
            jerry.setPower(-speed);
            bob.setPower(0);
            larry.setPower(0);
        }
    }

    private String calculatedDirection (double Joy_x, double Joy_y)
    {
        String directionToTravel;

        if (Joy_x > Math.cos(1.96) && Joy_x < Math.cos(1.18) && Joy_y > 0)
        {
            directionToTravel = "FORWARD"; // Forwards direction
        }
        else if (Joy_x > Math.cos(1.18) && Joy_x < Math.cos(0.393)  && Joy_y > 0)
        {
            directionToTravel = "FORWARD/RIGHT"; // Forward/right direction
        }
        else if (Joy_x > 0 && Joy_y > Math.sin(-0.393) && Joy_y < Math.sin(0.393))
        {
            directionToTravel = "RIGHT"; // Right direction
        }
        else if (Joy_x > Math.cos(1.18) && Joy_x < Math.cos(0.393)  && Joy_y < 0)
        {
            directionToTravel = "BACKWARD/RIGHT"; // Backward/right direction
        }
        else if (Joy_x > Math.cos(1.96) && Joy_x < Math.cos(1.18) && Joy_y < 0)
        {
            directionToTravel = "BACKWARD"; // Backwards direction
        }
        else if (Joy_x < Math.cos(1.96) && Joy_x > Math.cos(2.75) && Joy_y < 0)
        {
            directionToTravel = "BACKWARD/LEFT"; // Backward/left direction
        }
        else if (Joy_x < 0 && Joy_y > Math.sin(-0.393) && Joy_y < Math.sin(0.393))
        {
            directionToTravel = "LEFT"; // Left direction
        }
        else if (Joy_x < Math.cos(1.96) && Joy_x > Math.cos(2.75) && Joy_y > 0)
        {
            directionToTravel = "FORWARD/LEFT"; // Forward/left direction
        }
        else
        {
            directionToTravel = "STOP";
        }

        return directionToTravel;
    }

    private double calculatedSpeed(String joyStick)
    {
        double speed;

        // Repeat of initial values, but only applies to this method
        double rx = this.gamepad1.right_stick_x;
        double ry = -this.gamepad1.right_stick_y;
        double lx = this.gamepad1.left_stick_x;
        double ly = -this.gamepad1.left_stick_y;

        joyStick.toLowerCase();

        if (joyStick == "right")
        {
            speed = Math.sqrt(Math.pow(rx, 2) + Math.pow(ry, 2)) * 0.75;;
        }
        else if(joyStick == "left")
        {
            speed = Math.sqrt(Math.pow(lx, 2) + Math.pow(ly, 2)) * 0.75;;
        }
        else
        {
            speed = 0;
        }

        return speed;
    }

    private boolean JoyIsActive (String joyStick)
    {
        boolean isActive = false;
        double rx = this.gamepad1.right_stick_x;
        double ry = -this.gamepad1.right_stick_y;
        double lx = this.gamepad1.left_stick_x;
        double ly = -this.gamepad1.left_stick_y;

        joyStick.toLowerCase();

        if (joyStick == "right")
        {
            if (Math.abs(rx) > deadzone || Math.abs(ry) > deadzone)
            {
                isActive = true;
            }
            else
            {
                isActive = false;
            }
        }
        else if (joyStick == "left")
        {
            if (Math.abs(lx) > deadzone || Math.abs(ly) > deadzone)
            {
                isActive = true;
            }
            else
            {
                isActive = false;
            }
        }

        return isActive;
    }

    private void MoveClaw ()
    {
        if (this.gamepad1.dpad_left)
        {
            if (clawCurrentPosition > 1) {clawCurrentPosition = 1;}
            else
            {
                clawCurrentPosition += 0.0075;
                sherry.setPosition(clawCurrentPosition);
            }
        }
        else if (this.gamepad1.dpad_right)
        {
            if (clawCurrentPosition < 0) {clawCurrentPosition = 0;}
            else
            {
                clawCurrentPosition -= 0.0075;
                sherry.setPosition(clawCurrentPosition);
            }
        }
    }

    private void MoveArm()
    {
        double wrist_position;

        if (this.gamepad1.right_trigger > 0) {
            barry.setTargetPosition(1700);
            barry.setPower(this.gamepad1.right_trigger);
            was_pressed = true;
        } else if (this.gamepad1.left_trigger > 0) {
            barry.setTargetPosition(0);
            barry.setPower(this.gamepad1.left_trigger / 2);
            was_pressed = true;
        } else if (was_pressed) {
            barry.setTargetPosition(barry.getCurrentPosition());
            barry.setPower(1);
            was_pressed = false;
        }

        if (barry.getCurrentPosition() < 100)
        {
            wrist_position = 0.3069;
        }
        else
        {
            wrist_position = barry.getCurrentPosition() / 1700.0; //Ratio between servo movement and motor movement
        }

        barry.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        garry.setPosition(wrist_position);

        telemetry.addData("Arm Position: ", barry.getCurrentPosition());
        telemetry.addData("Wrist Position: ", garry.getPosition());
    }

    private void ReInit()
    {
        bob.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        dylan.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        larry.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        jerry.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        barry.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
}