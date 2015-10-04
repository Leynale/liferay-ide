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
package com.liferay.ide.project.ui.migration;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.actions.SelectionProviderAction;


/**
 * @author Gregory Amerson
 */
public class IgnoreAction extends SelectionProviderAction implements IAction
{

    public IgnoreAction( ISelectionProvider provider )
    {
        super( provider, "Ignore" );
    }


    @Override
    public void run()
    {
        // TODO get the resource and remove all migration markers on view and then refresh migration view
    }

    @Override
    public void selectionChanged( IStructuredSelection selection )
    {
        Object element = selection.getFirstElement();

        setEnabled( element instanceof TaskProblem );
    }
}