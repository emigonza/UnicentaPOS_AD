/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.util;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializerRead;

/**
 *
 * @author JA - based on A Escartin Barcode
 * 15 Dec 2013
 */
public class ReturnInfo implements SerializableRead, IKeyed {

    private static final long serialVersionUID = 8906929819402L;
    private Integer idret;

    public ReturnInfo() {
        idret = null;
    }

    @Override
    public Object getKey() {
        return idret;
    }

    @Override
    public void readValues(DataRead dr) throws BasicException {
        idret = dr.getInt(1);
    }

    public void setId(Integer id) {
        idret = id;
    }

    public Integer getId() {
        return idret;
    }

    public static SerializerRead getSerializerRead() {
        return new SerializerRead() {
            @Override
            public Object readValues(DataRead dr) throws BasicException {
                return new ReturnInfo(
                        dr.getInt(1));
            }
        };
    }

    public ReturnInfo(Integer id) {
        this.idret = id;
    }

}
