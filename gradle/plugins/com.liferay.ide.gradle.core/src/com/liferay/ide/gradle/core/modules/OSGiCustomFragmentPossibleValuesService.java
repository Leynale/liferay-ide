/*******************************************************************************
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 *******************************************************************************/

package com.liferay.ide.gradle.core.modules;

import com.liferay.ide.server.core.LiferayServerCore;
import com.liferay.ide.server.core.portal.PortalBundle;
import com.liferay.ide.server.util.ServerUtil;

import java.io.File;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.sapphire.ElementList;
import org.eclipse.sapphire.PossibleValuesService;
import org.eclipse.sapphire.Value;
import org.eclipse.sapphire.modeling.Status;
import org.eclipse.wst.server.core.IRuntime;

/**
 * @author Terry Jia
 */
public class OSGiCustomFragmentPossibleValuesService extends PossibleValuesService
{

    private static String osgiBundleName;
    private static Set<String> possibleValues;

    @Override
    protected void compute( final Set<String> values )
    {
        final NewModuleFragmentOp op = op();

        String currentOSGiBundle = op.getCustomOSGiBundle().content();

        if( osgiBundleName == null || !osgiBundleName.equals( currentOSGiBundle ) || possibleValues == null )
        {
            osgiBundleName = currentOSGiBundle;

            possibleValues = new HashSet<String>();

            final String runtimeName = op.getLiferayRuntimeName().content();

            IRuntime runtime = ServerUtil.getRuntime( runtimeName );

            PortalBundle portalBundle = LiferayServerCore.newPortalBundle( runtime.getLocation() );

            if( portalBundle != null )
            {
                File module =
                    portalBundle.getOSGiBundlesDir().append( "modules" ).append( currentOSGiBundle ).toFile();

                if( module.exists() )
                {
                    try( JarFile jar = new JarFile( module ) )
                    {
                        Enumeration<JarEntry> enu = jar.entries();

                        while( enu.hasMoreElements() )
                        {
                            JarEntry entry = enu.nextElement();
                            String name = entry.getName();

                            if( name.startsWith( "META-INF/resources/" ) &&
                                ( name.endsWith( ".jsp" ) || name.endsWith( ".jspf" ) ) )
                            {
                                possibleValues.add( name );
                            }
                        }
                    }
                    catch( Exception e )
                    {
                    }
                }
            }
        }

        if( possibleValues != null )
        {
            Set<String> possibleValuesSet = new HashSet<String>();

            possibleValuesSet.addAll( possibleValues );

            ElementList<OSGiCustomFragment> currentFiles = op.getCustomFiles();

            if( currentFiles != null )
            {
                for( OSGiCustomFragment cj : currentFiles )
                {
                    String value = cj.getValue().content();

                    if( value != null )
                    {
                        possibleValuesSet.remove( value );
                    }
                }
            }

            values.addAll( possibleValuesSet );
        }
    }

    @Override
    public Status problem( Value<?> value )
    {
        return Status.createOkStatus();
    }

    private NewModuleFragmentOp op()
    {
        return context( NewModuleFragmentOp.class );
    }

}
