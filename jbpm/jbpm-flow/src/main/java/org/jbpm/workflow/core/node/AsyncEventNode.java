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
package org.jbpm.workflow.core.node;

import java.util.HashMap;
import java.util.Map;

import org.jbpm.workflow.core.impl.NodeImpl;
import org.kie.api.definition.process.Node;
import org.kie.api.definition.process.WorkflowElementIdentifier;

public class AsyncEventNode extends EventNode {

    private static final long serialVersionUID = -4724021457443413412L;

    private Node node;

    public AsyncEventNode(Node node) {
        this.node = node;
    }

    public Node getActualNode() {
        return node;
    }

    @Override
    public WorkflowElementIdentifier getId() {
        return node.getId();
    }

    @Override
    public Object getMetaData(String name) {
        return ((NodeImpl) node).getMetaData(name);
    }

    @Override
    public Map<String, Object> getMetaData() {
        Map<String, Object> metaData = new HashMap<>(node.getMetaData());
        metaData.put("hidden", "true");
        return metaData;
    }

}
