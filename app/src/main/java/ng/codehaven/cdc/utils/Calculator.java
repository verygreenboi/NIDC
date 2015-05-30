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

    /**
     * @param cif
     * @param fob
     * @param importDuty
     * @param levy
     * @return
     */
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

        Logger.m(b.toString());

        double cif = getCif(b.getString("cif"), b.getString("xr"), b.getBoolean("isDollars"));

        double fob = getFob(b.getString("fob"), b.getString("xr"), b.getBoolean("isDollars"));

        double duty = getDuty(b.getInt("duty"), cif);

        //Duty Result
        n.putString("dutyResult", String.valueOf(duty));

        // Surcharge
        n.putString("surchargeResult", String.valueOf(getSurcharge(duty)));

        // ETLS
        n.putString("etlsResult", String.valueOf(getETLS(cif)));

        // CISS
        n.putString("cissResult", String.valueOf(getCISS(fob)));

        // LEVY
        n.putString("levyResult", String.valueOf(getLevy(b.getInt("levy"), cif)));

        // VAT
        n.putString("vatResult", String.valueOf(getVat(cif, duty, getSurcharge(duty), getETLS(cif), getCISS(fob), getLevy(b.getInt("levy"), cif))));

        n.putString("totalResult",
                String.valueOf(getTotal(
                        duty,
                        getSurcharge(duty),
                        getLevy(b.getInt("levy"), cif),
                        getCISS(fob),
                        getETLS(cif),
                        getVat(cif, duty, getSurcharge(duty), getETLS(cif), getCISS(fob), getLevy(b.getInt("levy"), cif))
                )));
        Logger.m(n.toString());
        return n;
    }

    /**
     * @param duty
     * @param cif
     * @return
     */
    private static double getDuty(double duty, double cif) {
        double dutyD;
        dutyD = (cif * (duty / 100));

        return dutyD;
    }

    /**
     * @param duty
     * @return
     */
    private static double getSurcharge(Double duty) {
        return ((Constants.SURCHARGE / 100) * duty);
    }

    /**
     * @param cif Cost Insurance Freight
     * @return ETLS
     */
    private static double getETLS(Double cif) {
        return ((Constants.ETLS_PERCENTAGE / 100) * cif);
    }

    /**
     * @param cif
     * @param duty
     * @param surcharge
     * @param etls
     * @param ciss
     * @param levy
     * @return
     */
    private static double getVat(double cif, double duty, double surcharge, double etls, double ciss, double levy) {
        return ((Constants.VAT / 100) * (etls + ciss + duty + surcharge + levy + cif));
    }

    /**
     * @param levy
     * @param cif
     * @return
     */
    private static double getLevy(int levy, double cif) {
        return (((double) levy / 100) * cif);
    }

    /**
     * @param duty
     * @param surcharge
     * @param levy
     * @param ciss
     * @param etls
     * @param vat
     * @return
     */
    private static double getTotal(double duty, double surcharge, double levy, double ciss, double etls, double vat) {
        return duty + surcharge + levy + ciss + etls + vat;
    }

    private static double getFob(String fob, String exchange, boolean isDollars) {
        double mFOB = Double.parseDouble(fob);
        double mExchange = Double.parseDouble(exchange);
        if (isDollars) {
            mFOB = Double.parseDouble(fob) * mExchange;
        }
        return mFOB;
    }

    /**
     * @param fob
     * @return
     */
    private static double getCISS(double fob) {
        return ((Constants.FOB_PERCENTAGE / 100) * fob);
    }

    private static double getCif(String cif, String exchange, boolean isDollars) {
        double mExchange = Double.parseDouble(exchange);
        return getmCIF(cif, isDollars, mExchange);
    }

    private static double getmCIF(String cif, boolean isDollars, double mExchange) {
        double mCIF = Double.parseDouble(cif);
        if (isDollars) {
            mCIF = mCIF * mExchange;
        }
        return mCIF;
    }


    public static Bundle getDuties(Bundle b){


        double cif = Double.parseDouble(b.getString("cif"));
        double fob = Double.parseDouble(b.getString("fob"));
        double importDuty = Double.parseDouble(b.getString(DetailFragment.ARG_IMPORT_DUTY));
        double levy = Double.parseDouble(b.getString(DetailFragment.ARG_LEVY));

        if (b.getBoolean("inDollars")){
            cif = cif * b.getInt("xRate");
            fob = fob * b.getInt("xRate");
        }

        double[] duties = calculate(cif, fob, importDuty, levy);

        b.putString("dutyResult", String.valueOf(duties[0]));
        b.putString("surchargeResult", String.valueOf(duties[1]));
        b.putString("etlsResult", String.valueOf(duties[2]));
        b.putString("cissResult", String.valueOf(duties[3]));
        b.putString("levyResult", String.valueOf(duties[4]));
        b.putString("vatResult", String.valueOf(duties[5]));
        b.putString("totalResult", String.valueOf(duties[6]));

//        Logger.m(b.toString());
        return b;
    }

}
