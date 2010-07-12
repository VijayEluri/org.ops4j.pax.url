package org.sonatype.maven.repository.internal;

/*
 * Copyright (c) 2010 Sonatype, Inc. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0, 
 * and you may not use this file except in compliance with the Apache License Version 2.0. 
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the Apache License Version 2.0 is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */

import org.sonatype.maven.repository.Artifact;
import org.sonatype.maven.repository.LocalRepository;
import org.sonatype.maven.repository.RepositorySystemSession;
import org.sonatype.maven.repository.spi.LocalRepositoryEvent;

/**
 * @author Benjamin Bentmann
 */
public class DefaultLocalRepositoryEvent
    implements LocalRepositoryEvent
{

    private final RepositorySystemSession session;

    private final LocalRepository repository;

    private final Artifact artifact;

    public DefaultLocalRepositoryEvent( RepositorySystemSession session, Artifact artifact )
    {
        this.session = session;
        this.repository = session.getLocalRepository();
        this.artifact = artifact;
    }

    public RepositorySystemSession getSession()
    {
        return session;
    }

    public LocalRepository getRepository()
    {
        return repository;
    }

    public Artifact getArtifact()
    {
        return artifact;
    }

}
