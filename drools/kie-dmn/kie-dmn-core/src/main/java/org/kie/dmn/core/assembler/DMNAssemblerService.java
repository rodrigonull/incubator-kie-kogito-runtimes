/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.dmn.core.assembler;

import org.drools.compiler.builder.impl.KnowledgeBuilderImpl;
import org.drools.compiler.compiler.PackageRegistry;
import org.drools.compiler.lang.descr.PackageDescr;
import org.drools.core.definitions.InternalKnowledgePackage;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceConfiguration;
import org.kie.api.io.ResourceType;
import org.kie.dmn.backend.unmarshalling.v1_1.DefaultUnmarshaller;
import org.kie.dmn.core.impl.DMNModelImpl;
import org.kie.dmn.core.impl.DMNPackageImpl;
import org.kie.dmn.core.runtime.DMNPackage;
import org.kie.dmn.feel.model.v1_1.Definitions;
import org.kie.internal.assembler.KieAssemblerService;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.io.ResourceTypePackage;

import java.util.Map;

public class DMNAssemblerService implements KieAssemblerService {

    @Override
    public ResourceType getResourceType() {
        return ResourceType.DMN;
    }

    @Override
    public void addResource(KnowledgeBuilder kbuilder, Resource resource, ResourceType type, ResourceConfiguration configuration)
            throws Exception {

        Definitions dmndefs = new DefaultUnmarshaller().unmarshal( resource.getReader() );
        if (dmndefs == null) {
            return;
        }

        KnowledgeBuilderImpl kbuilderImpl = (KnowledgeBuilderImpl) kbuilder;
        PackageRegistry pkgReg = kbuilderImpl.getPackageRegistry( dmndefs.getNamespace() );
        if ( pkgReg == null ) {
            pkgReg = kbuilderImpl.newPackage( new PackageDescr( dmndefs.getNamespace() ) );
        }
        InternalKnowledgePackage kpkgs = pkgReg.getPackage();

        Map<ResourceType, ResourceTypePackage> rpkg = kpkgs.getResourceTypePackages();

        DMNPackageImpl dmnpkg = (DMNPackageImpl) rpkg.get( ResourceType.DMN );
        if ( dmnpkg == null ) {
            dmnpkg = new DMNPackageImpl( dmndefs.getNamespace() );
            rpkg.put(ResourceType.DMN, dmnpkg);
        }
        dmnpkg.addModel( dmndefs.getName(), new DMNModelImpl( dmndefs ) );
    }

    @Override
    public Class getServiceInterface() {
        return KieAssemblerService.class;
    }
}
