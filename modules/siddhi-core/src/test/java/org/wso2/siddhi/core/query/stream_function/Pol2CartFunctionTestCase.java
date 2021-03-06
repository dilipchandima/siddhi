/*
 * Copyright (c) 2005 - 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.wso2.siddhi.core.query.stream_function;

import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.wso2.siddhi.core.ExecutionPlanRuntime;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.query.output.callback.QueryCallback;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.util.EventPrinter;

public class Pol2CartFunctionTestCase {
    private static final Logger log = Logger.getLogger(Pol2CartFunctionTestCase.class);
    private int inEventCount;
    private int removeEventCount;
    private boolean eventArrived;

    @Before
    public void init() {
        inEventCount = 0;
        removeEventCount = 0;
        eventArrived = false;
    }

    @Test
    public void pol2CartFunctionTest() throws InterruptedException {

        SiddhiManager siddhiManager = new SiddhiManager();

        String polarStream = "define stream PolarStream (theta double, rho double);";
        String query = "@info(name = 'query1') " +
                "from PolarStream#pol2Cart(theta, rho) " +
                "select x, y " +
                "insert into outputStream ;";

        ExecutionPlanRuntime executionPlanRuntime = siddhiManager.createExecutionPlanRuntime(polarStream + query);

        executionPlanRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                if (inEvents != null) {
                    inEventCount = inEventCount + inEvents.length;
                    Assert.assertEquals(12, Math.round((Double) inEvents[0].getData(0)));
                    Assert.assertEquals(5, Math.round((Double) inEvents[0].getData(1)));

                }
                eventArrived = true;
            }

        });

        InputHandler inputHandler = executionPlanRuntime.getInputHandler("PolarStream");
        executionPlanRuntime.start();
        inputHandler.send(new Object[]{22.6, 13.0});
        Thread.sleep(100);
        Assert.assertEquals(1, inEventCount);
        Assert.assertTrue(eventArrived);
        executionPlanRuntime.shutdown();

    }
    @Test
    public void pol2CartFunctionTest2() throws InterruptedException {

        SiddhiManager siddhiManager = new SiddhiManager();

        String polarStream = "define stream PolarStream (theta double, rho double, elevation double);";
        String query = "@info(name = 'query1') " +
                "from PolarStream#pol2Cart(theta, rho, elevation) " +
                "select x, z " +
                "insert into outputStream ;";

        ExecutionPlanRuntime executionPlanRuntime = siddhiManager.createExecutionPlanRuntime(polarStream + query);

        executionPlanRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                if (inEvents != null) {
                    inEventCount = inEventCount + inEvents.length;
                    Assert.assertEquals(12, Math.round((Double) inEvents[0].getData(0)));
                    Assert.assertEquals(7, Math.round((Double) inEvents[0].getData(1)));

                }
                eventArrived = true;
            }

        });

        InputHandler inputHandler = executionPlanRuntime.getInputHandler("PolarStream");
        executionPlanRuntime.start();
        inputHandler.send(new Object[]{22.6, 13.0, 7.0});
        Thread.sleep(100);
        Assert.assertEquals(1, inEventCount);
        Assert.assertTrue(eventArrived);
        executionPlanRuntime.shutdown();

    }
}
