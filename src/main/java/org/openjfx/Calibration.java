package org.openjfx;


import externalThings.Jama.*;
import java.util.Random;
import java.util.Arrays;

public class Calibration {

    private Robot robot;
    private Tracking tracking;
    private Matrix[] robotMatricesM;
    private Matrix[] trackingMatricesN;

    /**
     *
     *
     * @param robot
     * @param tracking
     */
    public Calibration(Robot robot, Tracking tracking){
        this.robot = robot;
        this.tracking = tracking;
    }

    private void constructLinearEquation(int m){
        gatherDate(m);


    }


    /**
     * Takes the translational part of M_i and zeros to creat bi
     * bi is used to creat b for the Aw=b equation
     * @param m How many times the measurements are taken
     * @return b
     */
    private Matrix creatB(int m){
        Matrix zeroNineByOne = new Matrix(9, 1);
        double[][] bi = new double[12][1];
        Matrix[] bees =new Matrix[m];
        double[][] b = new double[12*m][1];

        for(int j = 0; j < m; j++) {
            for (int i = 0; i < 12; i++) {
                if (i < 9) {
                    bi[i][0] = zeroNineByOne.get(i, 0);
                } else {
                    bi[i][0] = getTranslationalPart(m)[j].get(i - 9, 0);
                }
            }
            bees[j] =  new Matrix(bi);
        }


        for(int j = 1; j <= m; j++ ) {
            for (int i = 1; i <= 12; i++) {
                b[j*i][0] = bees[j-1].get(i,0);
            }
        }
        b[0][0] = bees[0].get(0,0);

        return new Matrix(b);
    }
    /**
     * Gets the translational part of M_i, inverts it and saves it in an array
     * @param m How many times the measurements are taken
     * @return Array of translational part matrices
     */
    private Matrix[] getTranslationalPart(int m){
        Matrix[] TM = new Matrix[m];
        for(int i = 0; i < m; i++){
            TM[i] = robotMatricesM[i].getMatrix(0,3,4,4).inverse();
        }
        return TM;
    }

    /**
     * Gets the rotational part of M_i, inverts it and saves it in an array
     * @param m How many times the measurements are taken
     * @return Array of rotational part matrices
     */
    private Matrix[] getRotationPart(int m){
        Matrix[] RM = new Matrix[m];
        for(int i = 0; i < m; i++){
            RM[i] = robotMatricesM[i].getMatrix(0,3,0,3).inverse();
        }
        return RM;
    }

    /**
     * Moves the robot randomly and takes measurements while doing so
     * @param measurements How many times the measurements are taken
     */
    private void gatherDate(int measurements){
        robotMatricesM = new Matrix[measurements];
        trackingMatricesN =  new Matrix[measurements];
        for(int i = 0 ;i < measurements; i++){
            moveRobotPTP();
            robot.sendAndReceive("EnableAlter");
            robot.send("GetPositionHomRowWise");
            robotMatricesM[i] = splicer(robot.received());
            robot.sendAndReceive("DisableAlter");
            tracking.send("CM_NEXTVALUE");
            trackingMatricesN[i] = splicer(tracking.received());

        }

    }

    /**
     * Moves the Robot randomly for the purpose of getting data
     * Only works in PTP-mode (not real time)
     * TODO: change from PTP to RT
     */
    public void moveRobotPTP(){
        int[] joints = new int[6];
        for(int i = 0; i <= 4; i++){
            joints[i] = new Random().nextInt(75);
            if (new Random().nextInt(2) == 1){
                joints[i] *= -1;
            }
        }
        joints[4] = 0; joints[5] = 0;
        String robotCom = Arrays.toString(joints).replaceAll("\\[|\\]|\\,", "");
        System.out.println(robotCom);
        robot.sendAndReceive("MovePTPJoints " + robotCom);
    }

    /**
     * Splices the strings received by the serves into Matrix
     * @param input Values got by the servers
     * @return A double that can be put into a matrix
     */
    private Matrix splicer(String input){
        double[][] matrix = new double[4][4];
        String[] rowWise = input.split(" ");
        if(input.contains("y")){
            int i = 2;
            for(int j= 0; j <3; j++ ){
                for(int l =0; l<4; l++){
                    //System.out.println(rowWise[i]);
                    matrix[j][l] = Float.parseFloat(rowWise[i]);
                    //System.out.println(matrix[j][l]);
                    i ++;
                }
            }

        } else if(!input.contains("n")) {
            int i = 0;
            for(int j= 0; j <3; j++ ){
                for(int l =0; l<4; l++){
                    //System.out.println(rowWise[i]);
                    matrix[j][l] = Float.parseFloat(rowWise[i]);
                    i ++;
                }
            }
        }
        for(int l = 0; l < 3; l++){
            matrix[3][l] = 0;
        }
        matrix[3][3] = 1;

        return new Matrix(matrix);
    }




}
