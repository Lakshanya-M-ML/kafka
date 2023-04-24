/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.kafka.common.requests;

import org.apache.kafka.common.message.GetTelemetrySubscriptionsRequestData;
import org.apache.kafka.common.message.GetTelemetrySubscriptionsResponseData;
import org.apache.kafka.common.protocol.Errors;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetTelemetrySubscriptionRequestTest {

    @Test
    public void testGetErrorResponse() {
        GetTelemetrySubscriptionRequest req = new GetTelemetrySubscriptionRequest(new GetTelemetrySubscriptionsRequestData(), (short) 0);
        GetTelemetrySubscriptionResponse response = req.getErrorResponse(0, Errors.CLUSTER_AUTHORIZATION_FAILED.exception());
        assertEquals(Collections.singletonMap(Errors.CLUSTER_AUTHORIZATION_FAILED, 1), response.errorCounts());
    }

    @Test
    public void testErrorCountsReturnsNoneWhenNoErrors() {
        GetTelemetrySubscriptionsResponseData data = new GetTelemetrySubscriptionsResponseData()
                .setThrottleTimeMs(10)
                .setErrorCode(Errors.NONE.code());
        GetTelemetrySubscriptionResponse response = new GetTelemetrySubscriptionResponse(data);
        assertEquals(Collections.singletonMap(Errors.NONE, 1), response.errorCounts());
    }

    @Test
    public void testErrorCountsReturnsOneError() {
        GetTelemetrySubscriptionsResponseData data = new GetTelemetrySubscriptionsResponseData()
                .setThrottleTimeMs(10)
                .setErrorCode(Errors.CLUSTER_AUTHORIZATION_FAILED.code());
        data.setErrorCode(Errors.INVALID_CONFIG.code());

        GetTelemetrySubscriptionResponse response = new GetTelemetrySubscriptionResponse(data);
        Map<Errors, Integer> errorCounts = response.errorCounts();
        assertEquals(1, errorCounts.size());
        assertEquals(1, errorCounts.get(Errors.INVALID_CONFIG));
    }
}