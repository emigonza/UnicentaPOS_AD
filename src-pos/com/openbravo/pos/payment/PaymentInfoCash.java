//    uniCenta oPOS  - Touch Friendly Point Of Sale
//    Copyright (C) 2008-2009 Openbravo, S.L.
//    http://www.unicenta.net/unicentaopos
//
//    This file is part of uniCenta oPOS
//
//    uniCenta oPOS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//   uniCenta oPOS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with uniCenta oPOS.  If not, see <http://www.gnu.org/licenses/>.
package com.openbravo.pos.payment;

import com.openbravo.format.Formats;

public class PaymentInfoCash extends PaymentInfo {

    private double prePayAmount = 0.0;
    private double m_dPaid;
    private double m_dTotal;
    private double m_dTendered;

    /**
     * Creates a new instance of PaymentInfoCash
     */
    public PaymentInfoCash(double dTotal, double dPaid) {
        m_dTotal = dTotal;
        m_dPaid = dPaid;
    }

    /**
     * Creates a new instance of PaymentInfoCash
     */
    public PaymentInfoCash(double dTotal, double dPaid, double prePayAmount) {
        this(dTotal, dPaid);
        this.prePayAmount = prePayAmount;
    }

    @Override
    public PaymentInfo copyPayment() {
        return new PaymentInfoCash(m_dTotal, m_dPaid, prePayAmount);
    }

    @Override
    public String getName() {
        return "cash";
    }

    @Override
    public double getTotal() {
        return m_dTotal;
    }

    
    public double getPaid() {
        return m_dPaid;
    }

    
    public double getPrePaid() {
        return prePayAmount;
    }

    public boolean hasPrePay() {

        if (prePayAmount > 0) {
            return true;
        }
        return false;
    }

    @Override
    public String getTransactionID() {
        return "no ID";
    }

    public String printPaid() {
        return Formats.CURRENCY.formatValue(new Double(m_dPaid));
    }

    public String printChange() {
        return Formats.CURRENCY.formatValue(new Double(m_dPaid - m_dTotal));
    }

   public double getChange(){
       return m_dPaid - m_dTotal;
   }



}