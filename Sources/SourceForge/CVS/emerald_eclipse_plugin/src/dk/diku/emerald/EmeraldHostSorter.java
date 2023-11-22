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
 * Created on Nov 19, 2004
 *
 */

package dk.diku.emerald;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * Sorter for the EmeraldTableViewer that displays items of type 
 * <code>EmeraldHost</code>.
 * The sorter supports four sort criteria:
 * <p>
 * <code>HOST</code>: Hostname (String)
 * </p>
 * <p>
 * <code>USERNAME</code>: Username for host (String)
 * </p>
 * <p>
 * <code>EMX</code>: Path to emx.
 * </p>
 * <p>
 * <code>PRIORITY</code>: Priority of host.
 * </p>
 */
public class EmeraldHostSorter extends ViewerSorter {


	public final static int HOST 		= 0;
	public final static int USERNAME 				= 1;
	public final static int EMX 	= 2;
	public final static int PRIORITY 	= 7;

	// Criteria that the instance uses 
	private int criteria;
    private int direction;

	/**
	 * Creates a resource sorter that will use the given sort criteria.
	 *
	 * @param criteria the sort criterion to use: one of <code>NAME</code> or 
	 *   <code>TYPE</code>
	 */
	public EmeraldHostSorter(int criteria, int direction) {
		super();
		this.criteria = criteria;
		this.direction = direction;
	}

	/* (non-Javadoc)
	 * Method declared on ViewerSorter.
	 */
	public int compare(Viewer viewer, Object o1, Object o2) {
		EmeraldHost host1 = (EmeraldHost) o1;
		EmeraldHost host2 = (EmeraldHost) o2;

		int result;
        switch (criteria) {
			case HOST :
				return compareHosts(host1, host2)*direction;
			case USERNAME :
				return compareUsernames(host1, host2)*direction;
			case EMX :
				return compareEmx(host1, host2)*direction;
			case PRIORITY :
				return comparePriorities(host1, host2)*direction;
			default:
				return 0;
		}
	}

	/**
	 * Returns a number reflecting the collation order of the given hosts
	 * based on the percent completed.
	 *
	 * @param host1
	 * @param host2
	 * @return a negative number if the first element is less  than the 
	 *  second element; the value <code>0</code> if the first element is
	 *  equal to the second element; and a positive number if the first
	 *  element is greater than the second element
	 */
	private int compareEmx(EmeraldHost host1, EmeraldHost host2) {
		int result = collator.compare(host1.getEmx(), host2.getEmx()); 
		return result;
	}

	/**
	 * Returns a number reflecting the collation order of the given hosts
	 * based on the description.
	 *
	 * @param host1 the first host element to be ordered
	 * @param resource2 the second host element to be ordered
	 * @return a negative number if the first element is less  than the 
	 *  second element; the value <code>0</code> if the first element is
	 *  equal to the second element; and a positive number if the first
	 *  element is greater than the second element
	 */
	protected int compareHosts(EmeraldHost host1, EmeraldHost host2) {
		return collator.compare(host1.getHost(), host2.getHost());
	}

	/**
	 * Returns a number reflecting the collation order of the given hosts
	 * based on their owner.
	 *
	 * @param resource1 the first resource element to be ordered
	 * @param resource2 the second resource element to be ordered
	 * @return a negative number if the first element is less  than the 
	 *  second element; the value <code>0</code> if the first element is
	 *  equal to the second element; and a positive number if the first
	 *  element is greater than the second element
	 */
	protected int compareUsernames(EmeraldHost host1, EmeraldHost host2) {
		return collator.compare(host1.getUsername(), host2.getUsername());
	}

	/**
	 * Returns a number reflecting the collation order of the given hosts
	 * based on their owner.
	 *
	 * @param resource1 the first resource element to be ordered
	 * @param resource2 the second resource element to be ordered
	 * @return a negative number if the first element is less  than the 
	 *  second element; the value <code>0</code> if the first element is
	 *  equal to the second element; and a positive number if the first
	 *  element is greater than the second element
	 */
	protected int comparePriorities(EmeraldHost host1, EmeraldHost host2) {
		return host1.getPriority().compareTo(host2.getPriority());
	}
}
