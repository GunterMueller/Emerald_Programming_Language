/*
 * The Emerald Language Eclipse Plugin
 * 
 * Copyright (C) 2004 Mathias Bertelsen <mathias@bertelsen.co.uk>
 * 
 * This file is part of the Emerald Language Eclipse Plugin.
 *
 * The Emerald Language Eclipse Plugin is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 2 of the License.
 *
 *  The Emerald Language Eclipse Plugin is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with the Emerald Language Eclipse Plugin; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * Created on Nov 18, 2004
 *
 */
package dk.diku.emerald;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

/**
 * @author mb
 *
 */
public class EmeraldSourceViewerConfiguration extends SourceViewerConfiguration {

    
    /* (non-Javadoc)
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getContentAssistant(org.eclipse.jface.text.source.ISourceViewer)
     */
    public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {

        return super.getContentAssistant(sourceViewer);
    }
    /* (non-Javadoc)
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getPresentationReconciler(org.eclipse.jface.text.source.ISourceViewer)
     */
    public IPresentationReconciler getPresentationReconciler(
            ISourceViewer sourceViewer) {
        PresentationReconciler pr = new PresentationReconciler();
        DefaultDamagerRepairer ddr = new DefaultDamagerRepairer(new EmeraldScanner(new EmeraldColorProvider()));
        pr.setRepairer(ddr, IDocument.DEFAULT_CONTENT_TYPE);
        pr.setDamager(ddr, IDocument.DEFAULT_CONTENT_TYPE);
        return pr;
        
    }
    /* (non-Javadoc)
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getTextHover(org.eclipse.jface.text.source.ISourceViewer, java.lang.String)
     */
    public ITextHover getTextHover(ISourceViewer sourceViewer,
            String contentType) {
        return super.getTextHover(sourceViewer, contentType);
    }
    /* (non-Javadoc)
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getTabWidth(org.eclipse.jface.text.source.ISourceViewer)
     */
    public int getTabWidth(ISourceViewer sourceViewer) {
        return EmeraldPlugin.getDefault().getPreferenceStore().getInt(EmeraldPlugin.EMERALD_TAB_WIDTH_PREFERENCE);//super.getTabWidth(sourceViewer);
    }
}
