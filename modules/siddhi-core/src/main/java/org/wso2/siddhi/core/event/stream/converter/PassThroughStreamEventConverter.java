/*
 * Copyright (c) 2005 - 2014, WSO2 Inc. (http://www.wso2.org)
 * All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.siddhi.core.event.stream.converter;

import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.stream.StreamEvent;

public class PassThroughStreamEventConverter implements EventConverter {

    private void convertToInnerStreamEvent(Object[] data, StreamEvent.Type type, long timestamp, StreamEvent borrowedEvent) {
        System.arraycopy(data, 0, borrowedEvent.getOutputData(), 0, data.length);
        borrowedEvent.setType(type);
        borrowedEvent.setTimestamp(timestamp);
    }

    public void convertEvent(Event event, StreamEvent borrowedEvent) {
        convertToInnerStreamEvent(event.getData(), event.isExpired() ? StreamEvent.Type.EXPIRED : StreamEvent.Type.CURRENT,
                event.getTimestamp(), borrowedEvent);
    }

    public void convertStreamEvent(StreamEvent streamEvent, StreamEvent borrowedEvent) {
        convertToInnerStreamEvent(streamEvent.getOutputData(), streamEvent.getType(),
                streamEvent.getTimestamp(), borrowedEvent);
    }

    @Override
    public void convertData(long timeStamp, Object[] data, StreamEvent borrowedEvent) {
        convertToInnerStreamEvent(data, StreamEvent.Type.CURRENT, timeStamp, borrowedEvent);
    }

}
