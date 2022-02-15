package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Bot {

    // Motors
    public DcMotorEx frontLeftMotor = null;
    public DcMotorEx frontRightMotor = null;
    public DcMotorEx backLeftMotor = null;
    public DcMotorEx backRightMotor = null;
    public DcMotorEx armMotor = null;
    public DcMotorEx spinMotor = null;
    // Servos
    public Servo wristServo = null;
    public Servo clawServo = null;
    public Servo cornerTech = null;

    HardwareMap hwMap = null;

    Bot(){
        initializeHWMap(hwMap);
    }

    protected void initializeHWMap (HardwareMap hwMap){
        //#################### HARDWARE MAPPING ####################\\
        frontLeftMotor = hwMap.get(DcMotorEx.class, "front_left_motor");
        frontRightMotor = hwMap.get(DcMotorEx.class, "front_right_motor");
        backLeftMotor = hwMap.get(DcMotorEx.class, "back_left_motor");
        backRightMotor = hwMap.get(DcMotorEx.class, "back_right_motor");
        armMotor = hwMap.get(DcMotorEx.class, "swing_arm_motor");
        spinMotor = hwMap.get(DcMotorEx.class, "spin_motor");

        wristServo = hwMap.get(Servo.class, "wrist_joint");
        clawServo = hwMap.get(Servo.class, "claw_servo");
        cornerTech = hwMap.get(Servo.class, "bulldozer");
        //#################### HARDWARE MAPPING END ####################\\

        //#################### SETTING RUNMODES ####################\\
        frontLeftMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        frontRightMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        spinMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        //#################### SETTING RUNMODES END ####################\\

        //#################### SETTING DIRECTIONS ####################\\
        frontLeftMotor.setDirection((DcMotorSimple.Direction.REVERSE));
        frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        //#################### SETTING DIRECTIONS END ####################\\

        //#################### HALT BEHAVIOR ####################\\
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //#################### HALT BEHAVIOR END ####################\\

        //#################### STOP DURING INITIALIZATION ####################\\
        frontLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backLeftMotor.setPower(0);
        backRightMotor.setPower(0);
        //#################### STOP DURING INITIALIZATION END ####################\\

    }

    public static void main(String[] args)
    {

    }
}