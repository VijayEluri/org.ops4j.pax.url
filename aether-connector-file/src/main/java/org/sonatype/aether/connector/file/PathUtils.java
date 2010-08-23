package org.sonatype.aether.connector.file;

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

import java.io.File;
import java.util.StringTokenizer;

/**
 * URL handling for file URLs. Based on org.apache.maven.wagon.PathUtils.
 * 
 * @author Benjamin Hanzelmann
 */
final class PathUtils
{

    private PathUtils()
    {
    }

    /**
     * Returns the directory path portion of a file specification string. Matches the equally named unix command.
     * 
     * @return The directory portion excluding the ending file separator.
     */
    public static String dirname( final String path )
    {
        final int i = path.lastIndexOf( "/" );

        return ( ( i >= 0 ) ? path.substring( 0, i ) : "" );
    }

    /**
     * Returns the filename portion of a file specification string.
     * 
     * @return The filename string with extension.
     */
    public static String filename( final String path )
    {
        final int i = path.lastIndexOf( "/" );
        return ( ( i >= 0 ) ? path.substring( i + 1 ) : path );
    }

    public static String[] dirnames( final String path )
    {
        final String dirname = PathUtils.dirname( path );
        return split( dirname, "/", -1 );

    }

    private static String[] split( final String str, final String separator, final int max )
    {
        final StringTokenizer tok;

        if ( separator == null )
        {
            // Null separator means we're using StringTokenizer's default
            // delimiter, which comprises all whitespace characters.
            tok = new StringTokenizer( str );
        }
        else
        {
            tok = new StringTokenizer( str, separator );
        }

        int listSize = tok.countTokens();

        if ( max > 0 && listSize > max )
        {
            listSize = max;
        }

        final String[] list = new String[listSize];

        int i = 0;

        int lastTokenBegin;
        int lastTokenEnd = 0;

        while ( tok.hasMoreTokens() )
        {
            if ( max > 0 && i == listSize - 1 )
            {
                // In the situation where we hit the max yet have
                // tokens left over in our input, the last list
                // element gets all remaining text.
                final String endToken = tok.nextToken();

                lastTokenBegin = str.indexOf( endToken, lastTokenEnd );

                list[i] = str.substring( lastTokenBegin );

                break;

            }
            else
            {
                list[i] = tok.nextToken();

                lastTokenBegin = str.indexOf( list[i], lastTokenEnd );

                lastTokenEnd = lastTokenBegin + list[i].length();
            }

            i++;
        }
        return list;
    }

    /**
     * /** Return the protocol name. <br/>
     * E.g: for input <code>http://www.codehause.org</code> this method will return <code>http</code>
     * 
     * @param url the url
     * @return the host name
     */
    public static String protocol( final String url )
    {
        final int pos = url.indexOf( ":" );

        if ( pos == -1 )
        {
            return "";
        }
        return url.substring( 0, pos ).trim();
    }

    /**
     * Derive the path portion of the given URL.
     * 
     * @param url the file-repository URL
     * @return the basedir of the repository
     * @todo need to URL decode for spaces?
     */
    public static String basedir( String url )
    {
        String protocol = PathUtils.protocol( url );

        String retValue = null;

        retValue = url.substring( protocol.length() + 1 );
        retValue = decode( retValue );
        // special case: if omitted // on protocol, keep path as is
        if ( retValue.startsWith( "//" ) )
        {
            retValue = retValue.substring( 2 );

            if ( retValue.length() >= 2 && ( retValue.charAt( 1 ) == '|' || retValue.charAt( 1 ) == ':' ) )
            {
                // special case: if there is a windows drive letter, then keep the original return value
                retValue = retValue.charAt( 0 ) + ":" + retValue.substring( 2 );
            }
            else
            {
                // Now we expect the host
                int index = retValue.indexOf( "/" );
                if ( index >= 0 )
                {
                    retValue = retValue.substring( index + 1 );
                }

                // special case: if there is a windows drive letter, then keep the original return value
                if ( retValue.length() >= 2 && ( retValue.charAt( 1 ) == '|' || retValue.charAt( 1 ) == ':' ) )
                {
                    retValue = retValue.charAt( 0 ) + ":" + retValue.substring( 2 );
                }
                else if ( index >= 0 )
                {
                    // leading / was previously stripped
                    retValue = "/" + retValue;
                }
            }
        }

        // special case: if there is a windows drive letter using |, switch to :
        if ( retValue.length() >= 2 && retValue.charAt( 1 ) == '|' )
        {
            retValue = retValue.charAt( 0 ) + ":" + retValue.substring( 2 );
        }

        return retValue.trim();
    }

    /**
     * Decodes the specified (portion of a) URL. <strong>Note:</strong> This decoder assumes that ISO-8859-1 is used to
     * convert URL-encoded octets to characters.
     * 
     * @param url The URL to decode, may be <code>null</code>.
     * @return The decoded URL or <code>null</code> if the input was <code>null</code>.
     */
    private static String decode( String url )
    {
        String decoded = url;
        if ( url != null )
        {
            int pos = -1;
            while ( ( pos = decoded.indexOf( '%', pos + 1 ) ) >= 0 )
            {
                if ( pos + 2 < decoded.length() )
                {
                    String hexStr = decoded.substring( pos + 1, pos + 3 );
                    char ch = (char) Integer.parseInt( hexStr, 16 );
                    decoded = decoded.substring( 0, pos ) + ch + decoded.substring( pos + 3 );
                }
            }
        }
        return decoded;
    }

    // TODO: move to plexus-utils or use something appropriate from there
    public static String toRelative( File basedir, String absolutePath )
    {
        String relative;

        absolutePath = absolutePath.replace( '\\', '/' );
        String basedirPath = basedir.getAbsolutePath().replace( '\\', '/' );

        if ( absolutePath.startsWith( basedirPath ) )
        {
            relative = absolutePath.substring( basedirPath.length() );
            if ( relative.startsWith( "/" ) )
            {
                relative = relative.substring( 1 );
            }
            if ( relative.length() <= 0 )
            {
                relative = ".";
            }
        }
        else
        {
            relative = absolutePath;
        }

        return relative;
    }

}
