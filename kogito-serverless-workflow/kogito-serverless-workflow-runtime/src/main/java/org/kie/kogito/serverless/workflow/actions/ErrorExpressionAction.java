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
package org.kie.kogito.serverless.workflow.actions;

import org.jbpm.process.instance.ProcessInstance;
import org.jbpm.workflow.instance.NodeInstance;
import org.kie.kogito.internal.process.runtime.KogitoProcessContext;
import org.kie.kogito.internal.process.runtime.MessageException;

import com.fasterxml.jackson.databind.JsonNode;

public class ErrorExpressionAction extends BaseExpressionAction {

    public ErrorExpressionAction(String lang, String expr, String inputVar) {
        super(lang, expr, inputVar);
    }

    public void execute(KogitoProcessContext context) throws Exception {
        if (expr.isValid()) {
            JsonNode error = evaluate(context, JsonNode.class);
            if (!error.isNull() && error.isTextual()) {
                String errorStr = error.asText();
                if (!errorStr.isBlank()) {
                    setError(context, errorStr);
                }
            }
        } else {
            setError(context, "The expression used for generating error message is not a valid one: " + expr.asString());
        }
    }

    private void setError(KogitoProcessContext context, String message) {
        ((ProcessInstance) context.getProcessInstance()).setErrorState((NodeInstance) context.getNodeInstance(), new MessageException(message));
    }
}
