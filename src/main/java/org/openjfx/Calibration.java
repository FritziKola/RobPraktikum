package org.openjfx;


import externalThings.Jama.*;
import java.util.Random;
import java.util.Arrays;

public class Calibration {

    private Robot robot;
    private Tracking tracking;
    private Matrix[] robotMatricesM;
    private Matrix[] trackingMatricesN;
    private int measurements;

    /**
     *
     *
     * @param robot
     * @param tracking
     */
    public Calibration(Robot robot, Tracking tracking, int measurements){
        this.robot = robot;
        this.tracking = tracking;
        this.measurements =measurements;
    }

    public void constructLinearEquation(){
        robot.setSpeed("20");
        gatherDate();
        Matrix b = creatB(getTranslationalPart());
        Matrix A = creatA(creatAi(getRotationPart()));
        QRDecomposition QR = new QRDecomposition(A);
        Matrix qr = QR.solve(b);
        qr.print(10,5);
        getXY(qr);


    }

    /**
     * TODO: Die  Methode soll die x und Y Matrizen ausgeben, funktiuoniert aber noch nicht
     * @param w
     * @return
     */
    private Matrix getXY(Matrix w){
        Matrix XY = new Matrix(4,4);

        for(int i = 0; i < 12; i++){
            for(int j = 0; j < 4; j++){
                if(j < 3) {XY.set(j, i %4 , w.get(i, 0));}
                else{ XY.set(j,i %4,0);}
            }
        }
        XY.set(3,3,1);
        XY.print(10, 5);
        return XY;
    }

    /**
     * Creates one matrix 12*measurements X 24 out of an array of matrices
     * @param ai Array of matrices
     * @return Matrix A
     */
    private Matrix creatA(Matrix[] ai){
        double[][] a = new double[measurements*12][24];
        for(int i = 0; i < 24; i++){
            for(int j = 0, k =-1; j< measurements*12;j ++){
                if( j % 12 == 0){k++;};
                a[j][i]= ai[k].get(j%12, i);
            }
        }
        return new Matrix(a);
    }

    /**
     * Creates an array of matrices neede for the QR-factorisation
     * @param rotationalPart rotational Part of M
     * @return An array of matrices ai that can be used to creat A
     */
    private Matrix[] creatAi(Matrix[] rotationalPart) {
        Matrix[] aees = new Matrix[measurements];
        int x,y;
        for(int k = 0; k < measurements; k++) {
            double[][] ai = new double[12][24];
            for (int j = 0; j < 12; j++) {
                for (int i = 0; i < 24; i++) {

                    if(i>=9 && i % 12 == j){
                        ai[j][i] = -1;
                    }
                    else if (i>=9){
                        ai[j][i] = 0;
                    }
                    else {
                        if(i<3){x=0;} else if(i<6){x=1;} else {x=2;}
                        if(j<3){y=0;} else if(j<6){y=1;} else if(j<9){y=2;} else {y=3;}
                        ai[j][i] = rotationalPart[k].get(j % 3, i % 3)*trackingMatricesN[k].get(x,y);;
                    }
                    if(i >= 9  && i <12 && j >=9){
                        ai[j][i] = rotationalPart[k].get(j % 3, i % 3);
                    }
                }
            }
            aees[k] = new Matrix(ai);
        }
        return aees;
    }

    /**
     * Takes the translational part of M_i and zeros to creat bi
     * bi is used to creat b for the Aw=b equation
     * @return b
     */
    private Matrix creatB(Matrix[] translationalPart){
        Matrix zeroNineByOne = new Matrix(9, 1);
        Matrix[] bees =new Matrix[measurements];
        double[][] b = new double[12*measurements][1];

        for(int j = 0; j < measurements; j++) {
            double[][] bi = new double[12][1];
            for (int i = 0; i < 12; i++) {
                if (i < 9) {
                    bi[i][0] = zeroNineByOne.get(i, 0);
                } else {
                    bi[i][0] = translationalPart[j].get(i - 9, 0)*(-1);
                }
            }
            bees[j] =  new Matrix(bi);
        }

        for(int j = 0, i= -1; j < 12*measurements; j++ ) {
            if( j % 12 == 0){ i++;}
            b[j][0] = bees [i].get(j % 12, 0);
        }
        b[0][0] = bees[0].get(0,0);

        return new Matrix(b);
    }
    /**
     * Gets the translational part of M_i, inverts it and saves it in an array
     * @return Array of translational part matrices
     */
    private Matrix[] getTranslationalPart(){
        Matrix[] TM = new Matrix[measurements];
        for(int i = 0; i < measurements; i++){
            TM[i] = robotMatricesM[i].getMatrix(0,2,3,3);
        }

        System.out.println("TM: ");
        for(Matrix n:TM){
            n.print(10, 5);
        }

        return TM;
    }

    /**
     * Gets the rotational part of M_i, inverts it and saves it in an array
     * @return Array of rotational part matrices
     */
    private Matrix[] getRotationPart(){
        Matrix[] RM = new Matrix[measurements];
        for(int i = 0; i < measurements; i++){
            RM[i] = robotMatricesM[i].getMatrix(0,2,0,2);
        }

        System.out.println("RM: ");
        for(Matrix n :RM){
            n.print(10, 5);
        }

        return RM;
    }

    /**
     * Moves the robot randomly and takes measurements while doing so
     */
    private void gatherDate(){
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
        /* debug Kommentare */

        System.out.println("Matrizen des Roboters: ");
        for(Matrix m :robotMatricesM){
            m.print(10, 5);
        }
        System.out.println("Matrizen des Trackingsystems: ");
        for(Matrix m : trackingMatricesN){
            m.print(10,5);
        }
    }

    /**
     * Moves the Robot randomly for the purpose of getting data
     * Only works in PTP-mode (not real time)
     * TODO: change from PTP to RT
     */
    public void moveRobotPTP(){
        int[] joints = new int[]  {0,-150, 150, 0, 0, 0};

        for(int i = 0; i <= 4; i++){
            if (new Random().nextInt(2) == 1){
                joints[i] += new Random().nextInt(25)*(-1);
            } else{
                joints[i] += new Random().nextInt(25);
            }
        }
        joints[4] = 0; joints[5] = 0;
        String robotCom = Arrays.toString(joints).replaceAll("\\[|\\]|\\,", "");
        System.out.println(robotCom);
        robot.send("MovePTPJoints " + robotCom);
        if(robot.received().equals("false")){
            moveRobotPTP();
        }
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
                    matrix[j][l] = Float.parseFloat(rowWise[i]);
                    i ++;
                }
            }

        } else if(!input.contains("n")) {
            int i = 0;
            for(int j= 0; j <3; j++ ){
                for(int l =0; l<4; l++){
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
