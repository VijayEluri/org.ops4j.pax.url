package org.sonatype.maven.repository;

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

/**
 * An exclusion of one or more transitive dependencies.
 * 
 * @author Benjamin Bentmann
 */
public final class Exclusion
{

    private final String groupId;

    private final String artifactId;

    private final String classifier;

    private final String type;

    /**
     * Creates an exclusion for artifacts with the specified coordinates.
     * 
     * @param groupId The group identifier, may be {@code null}.
     * @param artifactId The artifact identifier, may be {@code null}.
     * @param classifier The classifier, may be {@code null}.
     * @param type The file type, may be {@code null}.
     */
    public Exclusion( String groupId, String artifactId, String classifier, String type )
    {
        this.groupId = ( groupId != null ) ? groupId : "";
        this.artifactId = ( artifactId != null ) ? artifactId : "";
        this.classifier = ( classifier != null ) ? classifier : "";
        this.type = ( type != null ) ? type : "";
    }

    /**
     * Gets the group identifier for artifacts to exclude.
     * 
     * @return The group identifier, never {@code null}.
     */
    public String getGroupId()
    {
        return groupId;
    }

    /**
     * Gets the artifact identifier for artifacts to exclude.
     * 
     * @return The artifact identifier, never {@code null}.
     */
    public String getArtifactId()
    {
        return artifactId;
    }

    /**
     * Gets the classifier for artifacts to exclude.
     * 
     * @return The classifier, never {@code null}.
     */
    public String getClassifier()
    {
        return classifier;
    }

    /**
     * Gets the file type for artifacts to exclude.
     * 
     * @return The file type of artifacts to exclude, never {@code null}.
     */
    public String getType()
    {
        return type;
    }

    @Override
    public String toString()
    {
        return getGroupId() + ':' + getArtifactId() + ':' + getType()
            + ( getClassifier().length() > 0 ? ':' + getClassifier() : "" );
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( obj == this )
        {
            return true;
        }
        else if ( obj == null || !getClass().equals( obj.getClass() ) )
        {
            return false;
        }

        Exclusion that = (Exclusion) obj;

        return artifactId.equals( that.artifactId ) && groupId.equals( that.groupId ) && type.equals( that.type )
            && classifier.equals( that.classifier );
    }

    @Override
    public int hashCode()
    {
        int hash = 17;
        hash = hash * 31 + artifactId.hashCode();
        hash = hash * 31 + groupId.hashCode();
        hash = hash * 31 + classifier.hashCode();
        hash = hash * 31 + type.hashCode();
        return hash;
    }

}
