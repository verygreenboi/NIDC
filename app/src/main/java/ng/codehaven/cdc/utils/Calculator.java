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

    public static Bundle calDuties(Bundle b) {
        Bundle n = new Bundle();

        int cif = getCif(b.getInt("cif"), b.getInt("xr"), b.getBoolean("isDollars"));

        int fob = getFob(b.getInt("fob"), b.getInt("xr"), b.getBoolean("isDollars"));

        int duty = getDuty(b.getInt("duty"), cif);

        //Duty Result
        n.putInt("dutyResult", duty);

        // Surcharge
        n.putInt("surchargeResult", getSurcharge(duty));

        // ETLS
        n.putInt("etlsResult", getETLS(cif));

        // CISS
        n.putInt("cissResult", getCISS(fob));

        // LEVY
        n.putInt("levyResult", getLevy(b.getInt("levy"), cif));

        // VAT
        n.putInt("vatResult", getVat(cif, duty, getSurcharge(duty), getETLS(cif), getCISS(fob), getLevy(b.getInt("levy"), cif)));

        n.putInt("totalResult",
                getTotal(
                        duty,
                        getSurcharge(duty),
                        getLevy(b.getInt("levy"), cif),
                        getCISS(fob),
                        getETLS(cif),
                        getVat(cif, duty, getSurcharge(duty), getETLS(cif), getCISS(fob), getLevy(b.getInt("levy"), cif))
                ));
        return n;
    }

    private static int getTotal(int duty, int surcharge, int levy, int ciss, int etls, int vat) {
        return duty + surcharge + levy + ciss + etls + vat;
    }

    private static int getVat(int cif, int duty, int surcharge, int etls, int ciss, int levy) {
        return (int) ((Constants.VAT / 100) * (etls + ciss + duty + surcharge + levy + cif));
    }

    private static int getLevy(int levy, int cif) {
        return (int) (((double) levy / 100) * cif);
    }

    private static int getCISS(int fob) {
        return (int) ((Constants.FOB_PERCENTAGE / 100) * fob);
    }

    private static int getFob(int fob, int exchange, boolean isDollars) {
        if (isDollars) {
            fob = fob * exchange;
        }
        return fob;
    }

    private static int getETLS(int cif) {
        return (int) ((Constants.ETLS_PERCENTAGE / 100) * cif);
    }

    private static int getSurcharge(int duty) {
        return (int) ((Constants.SURCHARGE / 100) * duty);
    }

    private static int getDuty(int duty, int cif) {
        double dutyD = duty;
        duty = (int) (cif * (dutyD / 100));

        return duty;
    }

    private static int getCif(int cif, int exchange, boolean isDollars) {
        if (isDollars) {
            cif = cif * exchange;
        }
        return cif;
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
