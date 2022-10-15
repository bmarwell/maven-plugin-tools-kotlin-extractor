package org.apache.maven.tools.plugin.extractor.kotlin;

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

import static org.apache.maven.tools.plugin.extractor.kotlin.KotlinKdocExtractor.NAME;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.intellij.lang.ASTNode;
import com.thoughtworks.qdox.model.JavaClass;
import org.apache.maven.plugin.descriptor.InvalidPluginDescriptorException;
import org.apache.maven.plugin.descriptor.MojoDescriptor;
import org.apache.maven.tools.plugin.PluginToolsRequest;
import org.apache.maven.tools.plugin.extractor.ExtractionException;
import org.apache.maven.tools.plugin.extractor.MojoDescriptorExtractor;
import org.apache.maven.tools.plugin.extractor.annotations.JavaAnnotationsMojoDescriptorExtractor;
import org.apache.maven.tools.plugin.extractor.annotations.scanner.MojoAnnotatedClass;
import org.apache.maven.tools.plugin.extractor.annotations.scanner.MojoAnnotationsScanner;
import org.apache.maven.tools.plugin.extractor.annotations.scanner.MojoAnnotationsScannerRequest;
import org.jetbrains.kotlin.ir.backend.js.ic.KotlinSourceFile;
import org.jetbrains.kotlin.ir.backend.js.ic.KotlinSourceFileMetadata;
import org.jetbrains.kotlin.kdoc.parser.KDocParser;
import org.jetbrains.kotlin.kdoc.psi.api.KDoc;
import org.jetbrains.kotlin.kdoc.psi.impl.KDocImpl;
import org.jetbrains.kotlin.load.kotlin.KotlinClassFinder;
import org.jetbrains.kotlin.load.kotlin.header.KotlinClassHeader;

/**
 * <p>
 * Extracts Mojo descriptors from <a href="https://kotlinlang.org/">Kotlin</a> sources.
 * </p>
 * For more information about the usage tag, have a look to:
 * <a href="http://maven.apache.org/developers/mojo-api-specification.html">
 * http://maven.apache.org/developers/mojo-api-specification.html</a>
 *
 * @see org.apache.maven.plugin.descriptor.MojoDescriptor
 */
@Named( NAME )
@Singleton
@org.codehaus.plexus.component.annotations.Component(
        role = MojoDescriptorExtractor.class,
        hint = NAME )
public class KotlinKdocExtractor
        extends JavaAnnotationsMojoDescriptorExtractor
{

    public static final String NAME = "kotlin-kdoc-extractor";

    @Inject
    private MojoAnnotationsScanner mojoAnnotationsScanner;

    @Override
    public List<MojoDescriptor> execute( PluginToolsRequest request )
            throws ExtractionException, InvalidPluginDescriptorException
    {
        final List<MojoDescriptor> mojoDescriptors = super.execute( request );

        final Map<String, MojoAnnotatedClass> annotatedClasses = scanAnnotations( request );

        for ( MojoAnnotatedClass annotatedClass : annotatedClasses.values() )
        {
            // if not in mojoDescriptors, continue
            MojoDescriptor descriptor;

            // get source

            // parse kdoc

            // set description
            descriptor.setDescription( "" );
        }

        return mojoDescriptors;
    }

    private Map<String, MojoAnnotatedClass> scanAnnotations( PluginToolsRequest request )
            throws ExtractionException
    {
        MojoAnnotationsScannerRequest mojoAnnotationsScannerRequest = new MojoAnnotationsScannerRequest();

        File output = new File( request.getProject().getBuild().getOutputDirectory() );
        mojoAnnotationsScannerRequest.setClassesDirectories( Arrays.asList( output ) );

        mojoAnnotationsScannerRequest.setDependencies( request.getDependencies() );

        mojoAnnotationsScannerRequest.setProject( request.getProject() );

        return mojoAnnotationsScanner.scan( mojoAnnotationsScannerRequest );
    }

}
