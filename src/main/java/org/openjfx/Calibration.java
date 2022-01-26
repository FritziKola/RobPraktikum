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
     * Constructor for test calibration
     */
    public Calibration(){
        measurements = 3;
        robotMatricesM = new Matrix[measurements];
        trackingMatricesN = new Matrix[measurements];/*
        trackingMatricesN[0] = parser("0.05547     0.04634     0.99738  -234.14795 -0.07444     0.99633    -0.04216  -186.64951 -0.99568    -0.07191     0.05872 -1985.50879");
        trackingMatricesN[1] = parser("-0.72629     0.00262     0.68738    -0.36195 -0.08803     0.99140    -0.09680  -167.93530 -0.68172    -0.13081    -0.71982 -1806.91919");
        trackingMatricesN[2] = parser("0.06013     0.04368     0.99723   -95.07963-0.07770     0.99622    -0.03895  -140.97337-0.99516    -0.07514 0.06330 -1294.53027");
        robotMatricesM[0] = parser("-0.00016    -0.00003    -1.00000   243.88756 -0.00021    -1.00000     0.00003    -0.00643 -1.00000     0.00021 0.00016 607.41571");
        robotMatricesM[1] = parser("-0.76604    -0.00000    -0.64279   392.31442 0.00000    -1.00000     0.00000    -0.00000 -0.64279    -0.00000    z0.76604 380.77405");
        robotMatricesM[2] = parser("0.00000    -0.00000    -1.00000   925.00000 0.00000    -1.00000     0.00000    -0.00000 -1.00000    -0.00000    -0.00000 425.00000");
        invers();*/
        double[][] m1 = {{-0.00016, -0.00003, -1.00000, 243.88756},
                {-0.00021, -1.00000, 0.00003,-0.00643},
                {-1.00000, 0.00021, 0.00016, 607.41571},
                {0.00000, 0.00000, 0.00000, 1.00000}};


        double[][] m2 = {{-0.76604  ,  -0.00000 ,   -0.64279,   392.31442},
                {0.00000    ,-1.00000    , 0.00000  ,  -0.00000},
                {-0.64279 ,   -0.00000  ,   0.76604 ,  380.77405},
                {0.00000   ,  0.00000  ,   0.00000     ,1.00000}};


        double[][] m3 = {{0.00000 ,   -0.00000 ,   -1.00000   ,925.00000},
                {0.00000  ,  -1.00000   ,  0.00000   , -0.00000},
                {      -1.00000  ,  -0.00000   , -0.00000 ,  425.00000},
                {0.00000   ,  0.00000    , 0.00000   ,  1.00000}};

        double[][] n1 = {{0.05547 ,    0.04634    , 0.99738 , -234.14795},
                {-0.07444  ,   0.99633   , -0.04216  ,-186.64951},
                {-0.99568   , -0.07191    , 0.05872 ,-1985.50879},
                {0.00000   ,  0.00000   ,  0.00000   ,  1.00000}};


        double[][] n2 = {{  -0.72629   ,  0.00262   ,  0.68738  ,  -0.36195},
                {-0.08803   ,  0.99140  ,  -0.09680 , -167.93530},
                {-0.68172  ,  -0.13081  ,  -0.71982 ,-1806.91919},
                {0.00000   ,  0.00000   ,  0.00000   ,  1.00000}};


        double[][] n3 = {{0.06013    , 0.04368  ,   0.99723  , -95.07963},
                { -0.07770    , 0.99622  ,  -0.03895 , -140.97337},
                {-0.99516 ,   -0.07514   ,  0.06330 ,-1294.53027},
                { 0.00000    , 0.00000 ,    0.00000 ,    1.00000}};
        robotMatricesM[0] = new Matrix(m1);
        robotMatricesM[1] = new Matrix(m2);
        robotMatricesM[2] = new Matrix(m3);
        trackingMatricesN[0] = new Matrix(n1);
        trackingMatricesN[1] = new Matrix(n2);
        trackingMatricesN[2] = new Matrix(n3);
        inverse();
        /*
        robotMatricesM[0] = new Matrix( new double[][] {{-0.18, 0, 0.98, 928.232},
                {0.01, 1, 0, 8.509},
                {-0.98, 0.01, -0.18, 392.979},
                {0, 0, 0, 1}});
        robotMatricesM[1] = new Matrix( new double[][] {{-0.24, -0.18, 0.95, 889.02},
                {0.28, 0.93, 0.25, 226.827},
                {-0.93, 0.33, -0.17, 173.287},
                {0, 0, 0, 1}});
        trackingMatricesN[0] = new Matrix(new double[][]{{-0.13, 0.4, -1, -36.3},
                {-0.05, -1, -0.03, -175.7},
                {-1, 0.04, 0.13, -1285.77},
                {0, 0, 0, 1}});
        trackingMatricesN[1] = new Matrix(new double[][]{{-0.12, -0.23, -0.97, 173.64},
                {-0.32, -0.91, 0.26, 53.77},
                {-0.94, 0.34, 0.03, -1320.49},
                {0, 0, 0, 1}});*/
        Matrix b = createB();
        Matrix A = createA();
        QRDecomposition QR = new QRDecomposition(A);
        Matrix qr = QR.solve(b);
        qr.print(10,5);
        getXY(qr, 1).print(10,5);
        getXY(qr, 2).print(10, 5);
    }

    /**
     * Solves calibration and sets X and Y
     */
    public void solveCalibration(){
        gatherDate();
        QRDecomposition QR = new QRDecomposition(createA());
        Matrix qr = QR.solve(createB());
        X = getXY(qr, 1);
        Y = getXY(qr, 2);
    }

    /**
     * Returns the solution matrices, got fro the qr decomposition
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
     * inverses robot matrices
     * TODO: Herausfinden ob das gebraucht wird oder nicht
     */
    private void inverse(){
        for (int i =0; i < measurements; i++){ robotMatricesM[i]=robotMatricesM[i].inverse(); }
    }

    /**
     * Moves the robot randomly and takes measurements while doing so
     * TODO: ueberprüfen ob man den alter wirklich brauch
     */
    private void gatherDate(){
        robotMatricesM = new Matrix[measurements];
        trackingMatricesN =  new Matrix[measurements];
        robot.setSpeed("40");
        for(int i = 0 ;i < measurements; i++){
            moveRobotPTP();
            robot.sendAndReceive("EnableAlter");
            robot.send("GetPositionHomRowWise");
            robotMatricesM[i] = parser(robot.received());
            robot.sendAndReceive("DisableAlter");
            tracking.send("CM_NEXTVALUE");
            trackingMatricesN[i] = parser(tracking.received());
        }
        robot.setSpeed("5");
        /* debug Kommentare */
        /*
        System.out.println("Matrizen des Roboters: ");
        for(Matrix m :robotMatricesM){
            m.print(10, 5);
        }
        System.out.println("Matrizen des Trackingsystems: ");
        for(Matrix m : trackingMatricesN){
            m.print(10,5);
        }

         */
    }

    /**
     * Moves the Robot randomly for the purpose of getting data
     * Only works in PTP-mode (not real time)
     * TODO: change from PTP to RT
     */
    public void moveRobotPTP(){
        int[] joints = new int[]  {0,-150, 150, 0, 0, 0};
        for(int i = 0; i <= 4; i++){
            if (new Random().nextInt(2) == 1){ joints[i] += new Random().nextInt(35)*(-1); }
            else{ joints[i] += new Random().nextInt(35); }
        }
        joints[4] = 0; joints[5] = 0;
        String robotCom = Arrays.toString(joints).replaceAll("\\[|]|,", "");
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
        } else if(!input.contains("n")) {
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
