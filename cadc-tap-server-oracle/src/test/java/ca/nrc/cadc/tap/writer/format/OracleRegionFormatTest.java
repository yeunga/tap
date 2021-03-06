
/*
 ************************************************************************
 *******************  CANADIAN ASTRONOMY DATA CENTRE  *******************
 **************  CENTRE CANADIEN DE DONNÉES ASTRONOMIQUES  **************
 *
 *  (c) 2019.                            (c) 2019.
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
 *
 ************************************************************************
 */

package ca.nrc.cadc.tap.writer.format;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.Assert;
import ca.nrc.cadc.dali.Circle;
import ca.nrc.cadc.dali.Point;
import ca.nrc.cadc.dali.Polygon;
import ca.nrc.cadc.stc.CoordPair;
import ca.nrc.cadc.stc.Flavor;
import ca.nrc.cadc.stc.Frame;
import ca.nrc.cadc.stc.ReferencePosition;
import ca.nrc.cadc.stc.Region;
import ca.nrc.cadc.stc.Union;


public class OracleRegionFormatTest {

    @Test
    public void formatCircle() {
        final OracleRegionFormat testSubject = new OracleRegionFormat();

        final BigDecimal[] points = new BigDecimal[] {
                new BigDecimal(3.4D - 0.7D),
                new BigDecimal(88.5D),
                new BigDecimal(3.4D),
                new BigDecimal(88.5D + 0.7D),
                new BigDecimal(3.4D + 0.7D),
                new BigDecimal(88.5D)
        };

        final BigDecimal[] typeValues = new BigDecimal[] {
                new BigDecimal(1),
                new BigDecimal(1003),
                new BigDecimal(4)
        };

        final Circle comparisonCircle = new Circle(new Point(3.4D, 88.5D), 0.6999999999999997D);
        final String comparisonCircleString = new OracleCircleFormat().format(comparisonCircle);

        final String asString = testSubject.polygonToString(typeValues, points);
        Assert.assertEquals("Wrong output.", comparisonCircleString, asString);
    }

    @Test
    public void formatPolygon() {
        final OracleRegionFormat testSubject = new OracleRegionFormat();
        final Polygon comparisonPolygon = new Polygon();

        final double[] points = {0.9, 4.6, 9.0, 10.2, 3.3, 8.7, 12.9, 45.6};

        final BigDecimal[] structPoints = new BigDecimal[points.length];

        for (int i = 0; i < points.length; i++) {
            structPoints[i] = new BigDecimal(points[i]);
        }

        comparisonPolygon.getVertices().add(new Point(0.9D, 4.6D));
        comparisonPolygon.getVertices().add(new Point(9.0D, 10.2D));
        comparisonPolygon.getVertices().add(new Point(3.3D, 8.7D));
        comparisonPolygon.getVertices().add(new Point(12.9D, 45.6D));

        final BigDecimal[] typeValues = new BigDecimal[] {
                new BigDecimal(1),
                new BigDecimal(1003),
                new BigDecimal(1)
        };

        final String comparisonPolygonString = new OraclePolygonFormat().format(comparisonPolygon);

        final String asString = testSubject.polygonToString(typeValues, structPoints);
        Assert.assertEquals("Wrong output.", comparisonPolygonString, asString);
    }

    @Test
    @Ignore("Just trying Unions...")
    public void formatUnion() {
        final OracleRegionFormat testSubject = new OracleRegionFormat();
        final List<Region> regionList = new ArrayList<>();

        final List<CoordPair> coordPairs = new ArrayList<>();

        coordPairs.add(new CoordPair(0.9D, 4.6D));
        coordPairs.add(new CoordPair(9.0D, 10.2D));
        coordPairs.add(new CoordPair(3.3D, 8.7D));
        coordPairs.add(new CoordPair(12.9D, 45.6D));

        final ca.nrc.cadc.stc.Polygon comparisonPolygon =
                new ca.nrc.cadc.stc.Polygon(Frame.ICRS, ReferencePosition.UNKNOWNREFPOS, Flavor.CARTESIAN2, coordPairs);

        //comparisonPolygon.getVertices().add(new Point(0.9D, 4.6D));
        //comparisonPolygon.getVertices().add(new Point(9.0D, 10.2D));
        //comparisonPolygon.getVertices().add(new Point(3.3D, 8.7D));
        //comparisonPolygon.getVertices().add(new Point(12.9D, 45.6D));

        regionList.add(comparisonPolygon);

        final Union union = new Union(Frame.ICRS, ReferencePosition.UNKNOWNREFPOS, Flavor.CARTESIAN2, regionList);
    }
}
