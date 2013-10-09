//    uniCenta oPOS  - Touch Friendly Point Of Sale
//    Copyright (c) 2009-2013 uniCenta & previous Openbravo POS works
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

package com.openbravo.data.loader;

/**
 *
 * @author adrianromero
 */
public class SessionDBDerby implements SessionDB {

    @Override
    public String TRUE() {
        return "1";
    }
    @Override
    public String FALSE() {
        return "0";
    }
    @Override
    public String INTEGER_NULL() {
        return "CAST(NULL AS INTEGER)";
    }
    @Override
    public String CHAR_NULL() {
        return "CAST(NULL AS CHAR)";
    }

    @Override
    public String getName() {
        return "Derby";
    }

    @Override
    public SentenceFind getSequenceSentence(Session s, String sequence) {
        return new SequenceForDerby(s, sequence);
    }
    
    @Override
    public SentenceFind resetSequenceSentence(Session s, String sequence) {
        return new SequenceForDerby(s, sequence);    
}
}
