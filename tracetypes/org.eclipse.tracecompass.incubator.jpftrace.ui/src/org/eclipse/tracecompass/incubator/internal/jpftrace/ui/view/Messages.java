package org.eclipse.tracecompass.incubator.internal.jpftrace.ui.view;

import org.eclipse.osgi.util.NLS;

class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.eclipse.tracecompass.incubator.internal.jpftrace.ui.view.messages"; //$NON-NLS-1$

    public static String JpfThreadStatusDataProviderFactory_title;

    public static String JpfThreadStatusDataProviderFactory_descriptionText;

    public static String JpfThreadStatusDataProvider_link;

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}