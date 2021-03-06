/*
 ************************************************************************
 *******************  CANADIAN ASTRONOMY DATA CENTRE  *******************
 **************  CENTRE CANADIEN DE DONNÉES ASTRONOMIQUES  **************
 *
 *  (c) 2018.                            (c) 2018.
 *  Government of Canada                 Gouvernement du Canada
 *  National Research Council            Conseil national de recherches
 *  Ottawa, Canada, K1A 0R6              Ottawa, Canada, K1A 0R6
 *  All rights reserved                  Tous droits réservés
 *
 *  NRC disclaims any warranties,        Le CNRC dénie toute garantie
 *  expressed, implied, or               énoncée, implicite ou légale,
 *  statutory, of any kind with          de quelque nature que ce
 *  respect to the software,             soit, concernant le logiciel,
 *  including without limitation         y compris sans restriction
 *  any warranty of merchantability      toute garantie de valeur
 *  or fitness for a particular          marchande ou de pertinence
 *  purpose. NRC shall not be            pour un usage particulier.
 *  liable in any event for any          Le CNRC ne pourra en aucun cas
 *  damages, whether direct or           être tenu responsable de tout
 *  indirect, special or general,        dommage, direct ou indirect,
 *  consequential or incidental,         particulier ou général,
 *  arising from the use of the          accessoire ou fortuit, résultant
 *  software.  Neither the name          de l'utilisation du logiciel. Ni
 *  of the National Research             le nom du Conseil National de
 *  Council of Canada nor the            Recherches du Canada ni les noms
 *  names of its contributors may        de ses  participants ne peuvent
 *  be used to endorse or promote        être utilisés pour approuver ou
 *  products derived from this           promouvoir les produits dérivés
 *  software without specific prior      de ce logiciel sans autorisation
 *  written permission.                  préalable et particulière
 *                                       par écrit.
 *
 *  This file is part of the             Ce fichier fait partie du projet
 *  OpenCADC project.                    OpenCADC.
 *
 *  OpenCADC is free software:           OpenCADC est un logiciel libre ;
 *  you can redistribute it and/or       vous pouvez le redistribuer ou le
 *  modify it under the terms of         modifier suivant les termes de
 *  the GNU Affero General Public        la “GNU Affero General Public
 *  License as published by the          License” telle que publiée
 *  Free Software Foundation,            par la Free Software Foundation
 *  either version 3 of the              : soit la version 3 de cette
 *  License, or (at your option)         licence, soit (à votre gré)
 *  any later version.                   toute version ultérieure.
 *
 *  OpenCADC is distributed in the       OpenCADC est distribué
 *  hope that it will be useful,         dans l’espoir qu’il vous
 *  but WITHOUT ANY WARRANTY;            sera utile, mais SANS AUCUNE
 *  without even the implied             GARANTIE : sans même la garantie
 *  warranty of MERCHANTABILITY          implicite de COMMERCIALISABILITÉ
 *  or FITNESS FOR A PARTICULAR          ni d’ADÉQUATION À UN OBJECTIF
 *  PURPOSE.  See the GNU Affero         PARTICULIER. Consultez la Licence
 *  General Public License for           Générale Publique GNU Affero
 *  more details.                        pour plus de détails.
 *
 *  You should have received             Vous devriez avoir reçu une
 *  a copy of the GNU Affero             copie de la Licence Générale
 *  General Public License along         Publique GNU Affero avec
 *  with OpenCADC.  If not, see          OpenCADC ; si ce n’est
 *  <http://www.gnu.org/licenses/>.      pas le cas, consultez :
 *                                       <http://www.gnu.org/licenses/>.
 *
 *  $Revision: 4 $
 *
 ************************************************************************
 */

package ca.nrc.cadc.tap.writer.format;

import ca.nrc.cadc.dali.Point;
import ca.nrc.cadc.dali.Polygon;
import ca.nrc.cadc.dali.postgresql.PgSpoly;
import ca.nrc.cadc.stc.CoordPair;
import ca.nrc.cadc.stc.Flavor;
import ca.nrc.cadc.stc.Frame;
import ca.nrc.cadc.stc.ReferencePosition;
import ca.nrc.cadc.stc.STC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Extract and format a PGSphere spoly as an STC-S string (TAP-1.0 compatibility).
 */
public class SPolyFormat10 extends AbstractResultSetFormat {
    /**
     * Takes a ResultSet and column index of the spoly
     * and returns a STC-S Polygon String.
     *
     * @param resultSet   containing the spoint column.
     * @param columnIndex index of the column in the ResultSet.
     * @return STC Polygon
     * @throws SQLException if there is an error accessing the ResultSet.
     */
    @Override
    public Object extract(ResultSet resultSet, int columnIndex)
        throws SQLException {
        String s = resultSet.getString(columnIndex);
        return getPolygon(s);
    }

    /**
     * Takes a String representation of the spoly
     * and returns a STC-S Polygon String.
     *
     * @param object to format.
     * @return STC-S Polygon String of the spoly.
     * @throws IllegalArgumentException if the object is not a String, or if
     *                                  the String cannot be parsed.
     */
    @Override
    public String format(Object object) {
        if (object == null) {
            return "";
        }
        return STC.format((ca.nrc.cadc.stc.Polygon) object);
    }

    ca.nrc.cadc.stc.Polygon getPolygon(String s) {
        if (s == null) {
            return null;
        }

        PgSpoly spoly = new PgSpoly();
        Polygon poly = spoly.getPolygon(s);

        List<CoordPair> coordPairs = new ArrayList<>();
        for (Point p : poly.getVertices()) {
            coordPairs.add(new CoordPair(p.getLongitude(), p.getLatitude()));
        }
        return new ca.nrc.cadc.stc.Polygon(Frame.ICRS, ReferencePosition.UNKNOWNREFPOS, Flavor.SPHERICAL2, coordPairs);
    }
}
