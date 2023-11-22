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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
/**
 * @author mb
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EmeraldScanner extends RuleBasedScanner {
	
    private static String[] fgKeywords= { "all","at","builtin","codeof","elseif","export","fix","function","isfixed","monitor","object","primitive","refix","signal","to","unfix","wait","and","attached","by","confirm","end","external","for","if","islocal","move","op","process","restrict","syntactictypeof","typeobject","var","when","as","awaiting","checkpoint","const","enumeration","failure","forall","immutable","locate","nameof","operation","record","return","suchthat","typeof","view","where","assert","begin","class","else","exit","field","from","initially","loop","new","or","recovery","returnandfail","then","unavailable","visit","while" };
            
	private static String[] fgConstants= { "false", "nil", "self", "true" };

    private IPreferenceStore preferences;

	/**
	 * Creates an emerald code scanner
	 */
	public EmeraldScanner(EmeraldColorProvider provider) {
		preferences = EmeraldPlugin.getDefault().getPreferenceStore();
		
		IToken keyword= new Token(new TextAttribute(provider.getColor(PreferenceConverter.getColor(preferences,EmeraldPlugin.EMERALD_COLOR_KEYWORD_PREFERENCE))));
		IToken constant= new Token(new TextAttribute(provider.getColor(PreferenceConverter.getColor(preferences,EmeraldPlugin.EMERALD_COLOR_CONSTANT_PREFERENCE))));
		IToken string= new Token(new TextAttribute(provider.getColor(PreferenceConverter.getColor(preferences,EmeraldPlugin.EMERALD_COLOR_STRING_PREFERENCE))));
		IToken comment= new Token(new TextAttribute(provider.getColor(PreferenceConverter.getColor(preferences,EmeraldPlugin.EMERALD_COLOR_SINGLE_LINE_COMMENT_PREFERENCE))));
		IToken other= new Token(new TextAttribute(provider.getColor(PreferenceConverter.getColor(preferences,EmeraldPlugin.EMERALD_COLOR_DEFAULT_PREFERENCE))));
		
		List rules= new ArrayList();

		// Add rule for single line comments.
		rules.add(new EndOfLineRule("%", comment)); //$NON-NLS-1$

		// Add rule for strings and character constants.
		rules.add(new MultiLineRule("\"", "\"", string, '\\')); //$NON-NLS-2$ //$NON-NLS-1$
		rules.add(new MultiLineRule("'", "'", string, '\\')); //$NON-NLS-2$ //$NON-NLS-1$

		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new EmeraldWhitespaceDetector()));

		// Add word rule for keywords and constants.
		WordRule wordRule= new WordRule(new EmeraldWordDetector(), other);
		for (int i= 0; i < fgKeywords.length; i++)
			wordRule.addWord(fgKeywords[i], keyword);
		for (int i= 0; i < fgConstants.length; i++)
			wordRule.addWord(fgConstants[i], constant);
		rules.add(wordRule);
		
		IRule[] result= new IRule[rules.size()];
		rules.toArray(result);
		setRules(result);
	}
}
