/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jbpm.bpmn2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.bpmn2.handler.SendTaskHandler;
import org.jbpm.bpmn2.objects.TestWorkItemHandler;
import org.jbpm.process.instance.impl.demo.SystemOutWorkItemHandler;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.kie.api.definition.process.WorkflowElementIdentifier;
import org.kie.kogito.internal.process.runtime.KogitoProcessInstance;
import org.kie.kogito.process.workitems.InternalKogitoWorkItem;

import static org.assertj.core.api.Assertions.assertThat;

public class EndEventTest extends JbpmBpmn2TestCase {

    @Test
    public void testImplicitEndParallel() throws Exception {
        kruntime = createKogitoProcessRuntime("org/jbpm/bpmn2/event/BPMN2-ParallelSplit.bpmn2");
        KogitoProcessInstance processInstance = kruntime.startProcess("ParallelSplit");
        assertProcessInstanceCompleted(processInstance);

    }

    @Test
    public void testErrorEndEventProcess() throws Exception {
        kruntime = createKogitoProcessRuntime("org/jbpm/bpmn2/event/BPMN2-ErrorEndEvent.bpmn2");
        KogitoProcessInstance processInstance = kruntime
                .startProcess("ErrorEndEvent");
        assertProcessInstanceAborted(processInstance);
        assertThat(((org.jbpm.process.instance.ProcessInstance) processInstance).getOutcome()).isEqualTo("error");

    }

    @Test
    public void testEscalationEndEventProcess() throws Exception {
        kruntime = createKogitoProcessRuntime("org/jbpm/bpmn2/escalation/BPMN2-EscalationEndEvent.bpmn2");
        KogitoProcessInstance processInstance = kruntime
                .startProcess("EscalationEndEvent");
        assertProcessInstanceAborted(processInstance);

    }

    @Test
    public void testSignalEnd() throws Exception {
        kruntime = createKogitoProcessRuntime("org/jbpm/bpmn2/event/BPMN2-SignalEndEvent.bpmn2");
        Map<String, Object> params = new HashMap<>();
        params.put("x", "MyValue");
        kruntime.startProcess("SignalEndEvent", params);

    }

    @Test
    public void testMessageEnd() throws Exception {
        kruntime = createKogitoProcessRuntime("BPMN2-MessageEndEvent.bpmn2");
        kruntime.getKogitoWorkItemManager().registerWorkItemHandler("Send Task",
                new SendTaskHandler());
        Map<String, Object> params = new HashMap<>();
        params.put("x", "MyValue");
        KogitoProcessInstance processInstance = kruntime.startProcess(
                "MessageEndEvent", params);
        assertProcessInstanceCompleted(processInstance);

    }

    @Test
    public void testMessageEndVerifyDeploymentId() throws Exception {
        kruntime = createKogitoProcessRuntime("BPMN2-MessageEndEvent.bpmn2");

        TestWorkItemHandler handler = new TestWorkItemHandler();

        kruntime.getKogitoWorkItemManager().registerWorkItemHandler("Send Task", handler);
        Map<String, Object> params = new HashMap<>();
        params.put("x", "MyValue");
        KogitoProcessInstance processInstance = kruntime.startProcess("MessageEndEvent", params);
        assertProcessInstanceCompleted(processInstance);

        InternalKogitoWorkItem workItem = (InternalKogitoWorkItem) handler.getWorkItem();
        assertThat(workItem).isNotNull();

        String nodeInstanceId = workItem.getNodeInstanceStringId();
        WorkflowElementIdentifier nodeId = workItem.getNodeId();
        String deploymentId = workItem.getDeploymentId();

        assertThat(nodeId).isNotNull();
        assertThat(nodeInstanceId).isNotNull();
        assertThat(deploymentId).isNull();

        // now set deployment id as part of kruntime's env
        kruntime.getKieRuntime().getEnvironment().set("deploymentId", "testDeploymentId");

        processInstance = kruntime.startProcess("MessageEndEvent", params);
        assertProcessInstanceCompleted(processInstance);

        workItem = (InternalKogitoWorkItem) handler.getWorkItem();
        assertThat(workItem).isNotNull();

        nodeInstanceId = workItem.getNodeInstanceStringId();
        nodeId = workItem.getNodeId();

        assertThat(nodeId).isNotNull();
        assertThat(nodeInstanceId).isNotNull();
    }

    @Test
    @Disabled("On Exit not supported, see https://issues.redhat.com/browse/KOGITO-2067")
    public void testOnEntryExitScript() throws Exception {
        kruntime = createKogitoProcessRuntime("BPMN2-OnEntryExitScriptProcess.bpmn2");
        kruntime.getKogitoWorkItemManager().registerWorkItemHandler("MyTask",
                new SystemOutWorkItemHandler());
        List<String> myList = new ArrayList<>();
        kruntime.getKieSession().setGlobal("list", myList);
        KogitoProcessInstance processInstance = kruntime
                .startProcess("OnEntryExitScriptProcess");
        assertProcessInstanceCompleted(processInstance);
        assertThat(myList).hasSize(4);

    }

    @Test
    @Disabled("On Exit not supported, see https://issues.redhat.com/browse/KOGITO-2067")
    public void testOnEntryExitNamespacedScript() throws Exception {
        kruntime = createKogitoProcessRuntime("BPMN2-OnEntryExitNamespacedScriptProcess.bpmn2");
        kruntime.getKogitoWorkItemManager().registerWorkItemHandler("MyTask",
                new SystemOutWorkItemHandler());
        List<String> myList = new ArrayList<>();
        kruntime.getKieSession().setGlobal("list", myList);
        KogitoProcessInstance processInstance = kruntime
                .startProcess("OnEntryExitScriptProcess");
        assertProcessInstanceCompleted(processInstance);
        assertThat(myList).hasSize(4);

    }

    @Test
    @Disabled("On Exit not supported, see https://issues.redhat.com/browse/KOGITO-2067")
    public void testOnEntryExitMixedNamespacedScript() throws Exception {
        kruntime = createKogitoProcessRuntime("BPMN2-OnEntryExitMixedNamespacedScriptProcess.bpmn2");
        kruntime.getKogitoWorkItemManager().registerWorkItemHandler("MyTask",
                new SystemOutWorkItemHandler());
        List<String> myList = new ArrayList<>();
        kruntime.getKieSession().setGlobal("list", myList);
        KogitoProcessInstance processInstance = kruntime
                .startProcess("OnEntryExitScriptProcess");
        assertProcessInstanceCompleted(processInstance);
        assertThat(myList).hasSize(4);

    }

    @Test
    @Disabled("On Exit not supported, see https://issues.redhat.com/browse/KOGITO-2067")
    public void testOnEntryExitScriptDesigner() throws Exception {
        kruntime = createKogitoProcessRuntime("BPMN2-OnEntryExitDesignerScriptProcess.bpmn2");
        kruntime.getKogitoWorkItemManager().registerWorkItemHandler("MyTask",
                new SystemOutWorkItemHandler());
        List<String> myList = new ArrayList<>();
        kruntime.getKieSession().setGlobal("list", myList);
        KogitoProcessInstance processInstance = kruntime
                .startProcess("OnEntryExitScriptProcess");
        assertProcessInstanceCompleted(processInstance);
        assertThat(myList).hasSize(4);

    }

    @Test
    public void testTerminateWithinSubprocessEnd() throws Exception {
        kruntime = createKogitoProcessRuntime("org/jbpm/bpmn2/event//BPMN2-SubprocessWithParallelSplitTerminate.bpmn2");
        KogitoProcessInstance processInstance = kruntime.startProcess("SubprocessWithParallelSplitTerminate");

        kruntime.signalEvent("signal1", null, processInstance.getStringId());

        assertProcessInstanceCompleted(processInstance);

    }

    @Test
    public void testTerminateEnd() throws Exception {
        kruntime = createKogitoProcessRuntime("org/jbpm/bpmn2/event/BPMN2-ParallelSplitTerminate.bpmn2");
        KogitoProcessInstance processInstance = kruntime.startProcess("ParallelSplitTerminate");

        kruntime.signalEvent("Signal 1", null, processInstance.getStringId());

        assertProcessInstanceCompleted(processInstance);

    }

    @Test
    public void testSignalEndWithData() throws Exception {
        kruntime = createKogitoProcessRuntime("org/jbpm/bpmn2/event/BPMN2-EndEventSignalWithData.bpmn2");
        Map<String, Object> params = new HashMap<>();
        KogitoProcessInstance processInstance = kruntime.startProcess("EndEventSignalWithData", params);

        assertProcessInstanceCompleted(processInstance);

    }
}
