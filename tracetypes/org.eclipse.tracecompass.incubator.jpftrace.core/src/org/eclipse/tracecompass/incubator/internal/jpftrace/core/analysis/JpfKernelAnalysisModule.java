/*******************************************************************************
 * Copyright (c) 2013, 2015 École Polytechnique de Montréal
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License 2.0 which
 * accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Geneviève Bastien - Initial API and implementation
 *   Mathieu Rail - Provide the requirements of the analysis
 *******************************************************************************/

package org.eclipse.tracecompass.incubator.internal.jpftrace.core.analysis;

import static org.eclipse.tracecompass.common.core.NonNullUtils.checkNotNull;

import java.util.Collections;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.tracecompass.analysis.os.linux.core.trace.DefaultEventLayout;
import org.eclipse.tracecompass.analysis.os.linux.core.trace.IKernelAnalysisEventLayout;
import org.eclipse.tracecompass.analysis.os.linux.core.trace.IKernelTrace;
import org.eclipse.tracecompass.common.core.NonNullUtils;
import org.eclipse.tracecompass.tmf.core.analysis.requirements.TmfAbstractAnalysisRequirement;
import org.eclipse.tracecompass.tmf.core.statesystem.ITmfStateProvider;
import org.eclipse.tracecompass.tmf.core.statesystem.TmfStateSystemAnalysisModule;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;

public class JpfKernelAnalysisModule extends TmfStateSystemAnalysisModule {

    /** The ID of this analysis module */
    public static final String ID = "org.eclipse.tracecompass.incubator.internal.jpftrace.core.analysis"; //$NON-NLS-1$

    /** The requirements as an immutable set */
    private static final Set<TmfAbstractAnalysisRequirement> REQUIREMENTS;

    static {
        REQUIREMENTS = Collections.emptySet();
    }

    @Override
    protected @NonNull ITmfStateProvider createStateProvider() {
        ITmfTrace trace = checkNotNull(getTrace());
        IKernelAnalysisEventLayout layout;

        if (trace instanceof IKernelTrace) {
            layout = ((IKernelTrace) trace).getKernelEventLayout();
        } else {
            /* Fall-back to the base LttngEventLayout */
            layout = DefaultEventLayout.getInstance();
        }

        System.out.println("JpfKernelAnalysisModule::createStateProvider: instantiating KernelStateProvider");

        return new JpfKernelStateProvider(trace, layout);
    }

    @Override
    protected String getFullHelpText() {
        return NonNullUtils.nullToEmptyString(Messages.LttngKernelAnalysisModule_Help);
    }

    @Override
    public Iterable<TmfAbstractAnalysisRequirement> getAnalysisRequirements() {
        return REQUIREMENTS;
    }
}
