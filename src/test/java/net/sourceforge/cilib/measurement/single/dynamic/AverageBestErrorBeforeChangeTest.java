/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.measurement.single.dynamic;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.util.Vectors;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Julien Duhain
 */
@RunWith(JMock.class)
public class AverageBestErrorBeforeChangeTest {
    private Mockery mockery = new JUnit4Mockery()
    {{
       setImposteriser(ClassImposteriser.INSTANCE);
    }};

    @Test
    public void results() {
        final Algorithm algorithm = mockery.mock(Algorithm.class);
        final OptimisationSolution mockSolution1 = new OptimisationSolution(Vectors.create(1.0), new MinimisationFitness(0.0));
        final FunctionOptimisationProblem mockProblem = mockery.mock(FunctionOptimisationProblem.class);

        mockery.checking(new Expectations() {{
            exactly(2).of(algorithm).getBestSolution(); will(returnValue(mockSolution1));
            exactly(2).of(algorithm).getOptimisationProblem();will(returnValue(mockProblem));
            exactly(2).of(mockProblem).getError(Vectors.create(1.0));
            will(onConsecutiveCalls(returnValue(30.0),
                                    returnValue(10.0)
                                    ));
            exactly(6).of(algorithm).getIterations();
            will(onConsecutiveCalls(returnValue(1),
                                    returnValue(2),
                                    returnValue(3),
                                    returnValue(4),
                                    returnValue(5),
                                    returnValue(6)
                                    ));
        }});

        AverageBestErrorBeforeChange m = new AverageBestErrorBeforeChange();
        m.setCycleSize(3);

        Assert.assertEquals(0.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
        Assert.assertEquals(30.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
        Assert.assertEquals(30.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
        Assert.assertEquals(30.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
        Assert.assertEquals(20.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
        Assert.assertEquals(20.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
    }

}
