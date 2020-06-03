package org.eclipse.tracecompass.incubator.internal.jpftrace.ui.style;

import java.util.Map;

import org.eclipse.tracecompass.tmf.core.dataprovider.X11ColorUtils;
import org.eclipse.tracecompass.tmf.core.model.StyleProperties;

import com.google.common.collect.ImmutableMap;

public enum JpfThreadStyle {

    LOCK(String.valueOf(Messages.JpfThreadStyle_lock), 0, 100, 200, 255, 0.25f, StyleProperties.SymbolType.DIAMOND),

    EXPOSE(String.valueOf(Messages.JpfThreadStyle_expose), 255, 0, 100, 255, 0.25f, StyleProperties.SymbolType.CIRCLE);

    private final Map<String, Object> fMap;
    

    private JpfThreadStyle(String label, int red, int green, int blue, int alpha, float heightFactor, String symbolType) {
        if (red > 255 || red < 0) {
            throw new IllegalArgumentException("Red needs to be between 0 and 255"); //$NON-NLS-1$
        }
        if (green > 255 || green < 0) {
            throw new IllegalArgumentException("Green needs to be between 0 and 255"); //$NON-NLS-1$
        }
        if (blue > 255 || blue < 0) {
            throw new IllegalArgumentException("Blue needs to be between 0 and 255"); //$NON-NLS-1$
        }
        if (alpha > 255 || alpha < 0) {
            throw new IllegalArgumentException("alpha needs to be between 0 and 255"); //$NON-NLS-1$
        }
        if (heightFactor > 1.0 || heightFactor < 0) {
            throw new IllegalArgumentException("Height factor needs to be between 0 and 1.0, given hint : " + heightFactor); //$NON-NLS-1$
        }
        fMap = ImmutableMap.of(StyleProperties.STYLE_NAME, label,
                StyleProperties.BACKGROUND_COLOR, X11ColorUtils.toHexColor(red, green, blue),
                StyleProperties.HEIGHT, heightFactor,
                StyleProperties.OPACITY, (float) alpha / 255,
                StyleProperties.SYMBOL_TYPE, symbolType);
    }

    public String getLabel() {
        return (String) toMap().getOrDefault(StyleProperties.STYLE_NAME, ""); //$NON-NLS-1$
    }

    public Map<String, Object> toMap() {
        return fMap;
    }
}