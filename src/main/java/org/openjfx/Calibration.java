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
    private Matrix X;
    private Matrix Y;

    /**
     * Constructor for real calibration
     * @param robot The robot in use
     * @param tracking The tracking in use
     * @param measurements The number auf measurements
     */
    public Calibration(Robot robot, Tracking tracking, int measurements){
        this.robot = robot;
        this.tracking = tracking;
        this.measurements =measurements;
    }

    /**
     * Constructor for test calibration, used for debuging
     */
    public Calibration(){

    }

    /**
     * Solves calibration and sets X and Y
     */
    public void solveCalibration(){

        doAllMeasurments();
        invertHM();
        QRDecomposition QR = new QRDecomposition(createA());
        Matrix qr = createA().solve(createB());
        X = getXY(qr, 2);
        Y = getXY(qr, 1);
        System.out.println("X: ");
        X.print(10,5);
        System.out.println("Y: ");
        Y.print(10,5);
    }

    /**
     * Returns the solution matrices, got for the qr decomposition
     * @param w Array with
     * @param whichOne choosing between the X matrix with 1 or the Y matrix with 2
     * @return Ether X or Y matrix
     */
    private Matrix getXY(Matrix w, int whichOne){
        Matrix XY = new Matrix(4,4);
        for(int i = (whichOne-1)*12, j=-1; i < 12*whichOne; i++){
            if(i%3 == 0){j++;}
            XY.set(i%3, j, w.get(i, 0));
        }
        XY.set(3,3, 1);
        return XY;
    }

    /**
     * Creates one matrix 12*measurements X 24 out of an array of matrices
     * @return Matrix A
     */
    private Matrix createA(){
        Matrix[] ai = createAi();
        double[][] a = new double[measurements*12][24];
        for(int i = 0; i < 24; i++){
            for(int j = 0, k =-1; j< measurements*12;j ++){
                if( j % 12 == 0){k++;}
                a[j][i]= ai[k].get(j%12, i);
            }
        }
        return new Matrix(a);
    }

    /**
     * Creates an array of matrices need for the QR-factorisation
     * @return An array of matrices ai that can be used to create A
     */
    private Matrix[] createAi() {
        Matrix[] rotationalPart = getRotationPart();
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
                        ai[j][i] = rotationalPart[k].get(j % 3, i % 3)*trackingMatricesN[k].get(x,y);
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
     * Takes the translational part of M_i and zeros to create bi
     * bi is used to create b for the Aw=b equation
     * @return b
     */
    private Matrix createB(){
        Matrix[] translationalPart =getTranslationalPart();
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
        return RM;

    }

    /**
     * inverts homogeneous matrix
     * @return inverted matrix
     */
    public void invertHM(){
        for(int n = 0; n <measurements; n++) {
            Matrix hm = robotMatricesM[n];
            Matrix hminverted = new Matrix(4, 4);

            //rotational part:
            Matrix rotPartinverted = hm.getMatrix(0, 2, 0, 2).transpose();
            hminverted.setMatrix(0, 2, 0, 2, rotPartinverted);

            //translational part:
            double[][] transPart = {{0}, {0}, {0}, {1}};
            double k;
            for (int i = 0; i < 3; i++) {
                k = 0;
                for (int j = 0; j < 3; j++) {
                    k = k + (rotPartinverted.get(i, j) * hm.get(j, 3));
                }
                transPart[i][0] = -k;
            }
            hminverted.setMatrix(0, 3, 3, 3, new Matrix(transPart));
            hminverted.print(10,5);
            robotMatricesM[n] = hminverted;
        }

    }

    /**
     * Moves the robot randomly and takes measurements while doing so
     */
    private void doAllMeasurments() {
        robotMatricesM = new Matrix[measurements];
        trackingMatricesN =  new Matrix[measurements];
        robot.setSpeed(10L);
        String[] movements = { "MovePTPJoints -14 -167 165 -25 0 0", "MovePTPJoints 13 -152 151 -28 0 0", "MovePTPJoints -32 -146 143 2 0 0",
                "MovePTPJoints 11 -184 146 18 0 0", "MovePTPJoints -31 -128 136 5 0 0", "MovePTPJoints -29 -121 136 -28 0 0",
                "MovePTPJoints -27 -122 155 26 0 0", "MovePTPJoints -26 -152 142 7 0 0", "MovePTPJoints 25 -153 157 -31 0 0",
                "MovePTPJoints 17 -147 162 4 0 0", "MovePTPJoints 15 -148 141 17 0 0", "MovePTPJoints -15 -163 150 -13 0 0",};
        int i =0;
        for(String m : movements){
            robot.sendAndReceive(m);
            try {
                Thread.sleep(3000);
            } catch (Exception e){
                System.out.println("Fehler sleep");
            }
            tracking.send("CM_NEXTVALUE");
            if(tracking.received().contains("n")){moveRobotPTP();}
            robot.send("GetPositionHomRowWise");
            robotMatricesM[i] = parser(robot.received());
            tracking.send("CM_NEXTVALUE");
            trackingMatricesN[i] = parser(tracking.received());
            i++;

        }
        robot.setSpeed(5L);
    }


    /**
     * Moves the Robot randomly for the purpose of getting data
     * Only works in PTP-mode (not real time)
     */
    public void moveRobotPTP() {
        int[] joints = new int[]  {0,-150, 150, 0, 0, 0};
        for(int i = 0; i <= 4; i++){
            if (new Random().nextInt(2) == 1){ joints[i] += new Random().nextInt(35)*(-1); }
            else{ joints[i] += new Random().nextInt(30); }
        }
        joints[4] = 0; joints[5] = 0;
        String robotCom = Arrays.toString(joints).replaceAll("\\[|]|,", "");
        System.out.println(robotCom);

        try {
            Thread.sleep(10000);
        } catch (Exception e){
            System.out.println("Fehler sleep");
        }
        robot.send("MovePTPJoints " + robotCom);
        if(robot.received().equals("false")){moveRobotPTP();}

    }

    /**
     * Splices the strings received by the serves into Matrix
     * @param input Values got by the servers
     * @return A double that can be put into a matrix
     */
    public Matrix parser(String input){
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
        }
        else if(!input.contains("n")) {
            int i = 0;
            for(int j= 0; j <3; j++ ){
                for(int l =0; l<4; l++){
                    matrix[j][l] = Float.parseFloat(rowWise[i]);
                    i ++;
                }
            }
        }
        else { System.out.println("WARNING: No connection to Marker");}
        for(int l = 0; l < 3; l++){ matrix[3][l] = 0; }
        matrix[3][3] = 1;
        Matrix m = new Matrix(matrix);
        m.print(10, 5);
        return m;
    }

    public Matrix getX() { return X; }

    public Matrix getY() { return Y; }
}
