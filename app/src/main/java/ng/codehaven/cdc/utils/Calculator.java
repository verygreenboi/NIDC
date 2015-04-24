package ng.codehaven.cdc.utils;

import android.os.Bundle;

import ng.codehaven.cdc.Constants;
import ng.codehaven.cdc.fragments.DetailFragment;


public class Calculator {


    /**
     * @param cif Cost insurance freight
     * @return {#link ETLS}
     */
    public static double ETLS(double cif) {
        return (Constants.ETLS_PERCENTAGE / 100) * cif;
    }

    public static double CISS(double fob) {
        return (Constants.FOB_PERCENTAGE / 100) * fob;
    }

    public static double DUTY(double importDuty, double cif) {
        return (importDuty / 100) * cif;
    }

    public static double SURCHARGE(double importDuty, double cif) {
        return (Constants.SURCHARGE / 100) * DUTY(importDuty, cif);
    }

    public static double LEVY(double levy, double cif) {
        return (levy / 100) * cif;
    }

    public static double VAT(double cif, double fob, double importDuty, double levy) {
        double etls = ETLS(cif);
        double ciss = CISS(fob);
        double duty = DUTY(importDuty, cif);
        double surcharge = SURCHARGE(importDuty, cif);
        double mLevy = LEVY(levy, cif);

        return (Constants.VAT / 100) * (etls + ciss + duty + surcharge + mLevy + cif);

    }

    public static double total(double cif, double fob, double importDuty, double levy) {
        double etls = ETLS(cif);
        double ciss = CISS(fob);
        double duty = DUTY(importDuty, cif);
        double surcharge = SURCHARGE(importDuty, cif);
        double mLevy = LEVY(levy, cif);
        double vat = VAT(cif, fob, importDuty, levy);

        return etls + ciss + duty + surcharge + mLevy + vat;

    }

    public static double[] calculate(double cif, double fob, double importDuty, double levy) {

        return new double[]{
                DUTY(importDuty, cif),
                SURCHARGE(importDuty, cif),
                ETLS(cif),
                CISS(fob),
                LEVY(levy, cif),
                VAT(cif, fob, importDuty, levy),
                total(cif, fob, importDuty, levy)
        };
    }

    public static Bundle getDuties(Bundle b){


        double cif = b.getInt("cif");
        double fob = b.getInt("fob");
        double importDuty = b.getInt(DetailFragment.ARG_IMPORT_DUTY);
        double levy = b.getInt(DetailFragment.ARG_LEVY);

        if (b.getBoolean("inDollars")){
            cif = cif * b.getInt("xRate");
            fob = fob * b.getInt("xRate");
        }

        double[] duties = calculate(cif, fob, importDuty, levy);

        b.putInt("dutyResult", (int) duties[0]);
        b.putInt("surchargeResult", (int) duties[1]);
        b.putInt("etlsResult", (int) duties[2]);
        b.putInt("cissResult", (int) duties[3]);
        b.putInt("levyResult", (int) duties[4]);
        b.putInt("vatResult", (int) duties[5]);
        b.putInt("totalResult", (int) duties[6]);

//        Logger.m(b.toString());
        return b;
    }

}
