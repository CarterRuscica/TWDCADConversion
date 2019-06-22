package com.example.twdcadconversion;

public class DoubleBuilder {

    private int decimalLength = 0, decimalMaxLength;
    private int integerLength = 0, integerMaxLength;
    private boolean decimalMode = false;


    // Was going to be used to save what numbers we had, turned out to be incredibly useless however it'll stay here
//    private static final String FILENAME = "DoubleBuilderData.txt";

    private Double currency;


    public DoubleBuilder(int integerMaxLength, int decimalLength){
//        this.context = context;
        this.decimalMaxLength = decimalLength;
        this.integerMaxLength = integerMaxLength;
        this.currency = 0.0;
//        loadData();
    }

    /**
     * This is used because Java liked formatting the output to have way too many decimals.
     * @param value - The number we are going to round
     * @param places - to how many places
     * @return
     */

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public Double getCurrency() {
        return this.currency;
    }

    /***
     * When Decimal is toggled, next numbers will be decimal
     */

    public void toggleDecimal(){
        decimalMode = true;
    }

    /**
     *
     * @param i - Number we are "appending" to the beginning of the number, ex i = 1, currency = 5432
     *          currency will become 54321
     */

    public void addNumber(int i){
        if (decimalMode){
            if (decimalLength < decimalMaxLength) {
                decimalLength++;
                currency = currency + i * Math.pow(0.1, decimalLength);
            }
        }else{
            if(integerLength < integerMaxLength) {
                currency = currency * 10 + i;
                integerLength++;
            }
        }
        currency = round(currency, 2);
//        saveData();
    }

    /**
     * Makes the number 0 all around
     */

    public void clear(){
        currency = 0.0;
        integerLength = 0;
        decimalLength = 0;
        decimalMode = false;
//        saveData();
    }

    /**
     * Deletes the last number, 54321 -> 5432 would be the transition
     */

    public void backSpace(){
        if (decimalMode){
            int temp = decimalLength;
            currency = currency*Math.pow(10, temp);
            decimalLength--;
            int subtract = (int)Math.round(currency%10);
            currency -=subtract;
            currency = round(currency*Math.pow(0.1, temp), 2);
            if (decimalLength == 0){
//                decimalLength = 0;
                decimalMode = false;
            }
        }else{
            if (integerLength > 0) {
                integerLength--;
                int subtract = (int) Math.round(currency % 10);
                currency -= subtract;
                currency = currency * 0.1;
            }
        }
        currency = round(currency, 2);
//        saveData();
    }

    /**
     * Method is to be used for Textview to determine how many decimals to show, 10 vs 10.0 vs 10.00
     * @return - returns how many decimals values are from 0, 1, 2
     */
    public int getDecimalLength(){
        if (this.decimalMode && decimalLength == 0){
            return 1;
        }else {
            return this.decimalLength;
        }
    }

//    public void saveData(){
//        System.out.println("Working on saving file");
//        try {
//            PrintWriter pw = new PrintWriter(FILENAME);
//            String line =  currency + " " + integerLength + " " + decimalLength + " " + decimalMode;
//            pw.println(line);
//            pw.close();
//        }catch(FileNotFoundException e){
//            Log.i("File Error", "File error in saveData");
//        }
//
//    }
//
//    public void loadData(){
//        try{
//            File file = new File(FILENAME);
//            Scanner sc = new Scanner(file);
//            currency = sc.nextDouble();
//            integerLength = sc.nextInt();
//            decimalLength = sc.nextInt();
//            decimalMode = sc.nextBoolean();
//        }catch (FileNotFoundException e){
//            Log.i("File Error", "File not found");
//        }
//    }


}
