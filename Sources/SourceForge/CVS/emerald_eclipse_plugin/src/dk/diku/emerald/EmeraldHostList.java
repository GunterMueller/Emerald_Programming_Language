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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

/**
 * Class that plays the role of the domain model in the EmeraldTableViewer
 * 
 */

public class EmeraldHostList {

	private final int COUNT = 2;
	private Vector hosts = new Vector(COUNT);
	private Set changeListeners = new HashSet();

	/**
	 * Constructors
	 */
	public EmeraldHostList() {
		//super();
	//	System.out.println("EmeraldHostList()");
		//this.initData();
	}
	
	public EmeraldHostList(Vector h) {
		//super();
		for (Iterator iter = h.iterator(); iter.hasNext();) {
            EmeraldHost element = (EmeraldHost) iter.next();
            hosts.add(element);
        }
		//this.initData();
		
	}
	
	
	/*
	 * Initialize the table data.
	 * Create COUNT hosts and add them them to the 
	 * collection of hosts
	 * 
	 * for testing purposes 
	 */
	private void initData() {
		EmeraldHost host;
		for (int i = 0; i < COUNT; i++) {
			host = new EmeraldHost("planetlab"  + (i+1)+".diku.dk");
			host.setUsername("copenhagen_5");
			host.setEmx("/usr/bin/emx");
			hosts.add(host);
		}
	};

	public Vector getData() {
		EmeraldHost host;
		Vector tmphosts = new Vector(COUNT);
		for (int i = 0; i < COUNT; i++) {
			host = new EmeraldHost("planetolab"  + (i+1)+".diku.dk");
			host.setUsername("copenhagen_5");
			host.setEmx("/usr/bin/emx");
			tmphosts.add(host);
		}
		return tmphosts;
	};
	
	/**
	 * Return the collection of hosts
	 */
	public Vector getHosts() {
		return hosts;
	}
	
	/**
	 * Add a new host to the collection of hosts
	 */
	public void addHost() {
		EmeraldHost host = new EmeraldHost("host");
		hosts.add(hosts.size(), host);
		Iterator iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((IEmeraldHostListViewer) iterator.next()).addHost(host);
	}

	/**
	 * Add a new host to the collection of hosts
	 */
	public void addHost(EmeraldHost host) {
	    hosts.add(hosts.size(), host);
		Iterator iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((IEmeraldHostListViewer) iterator.next()).addHost(host);
	}

	/**
	 * @param host
	 */
	public void removeHost(EmeraldHost host) {
		hosts.remove(host);
		Iterator iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((IEmeraldHostListViewer) iterator.next()).removeHost(host);
	}

	/**
	 * @param host
	 */
	public void hostChanged(EmeraldHost host) {
		Iterator iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((IEmeraldHostListViewer) iterator.next()).updateHost(host);
	}

	/**
	 * @param viewer
	 */
	public void removeChangeListener(IEmeraldHostListViewer viewer) {
		changeListeners.remove(viewer);
	}

	/**
	 * @param viewer
	 */
	public void addChangeListener(IEmeraldHostListViewer viewer) {
		changeListeners.add(viewer);
	}

    /**
     * @param hosts The hosts to set.
     */
    public void setHosts(Vector h) {
        for (Iterator iter = hosts.iterator(); iter.hasNext();) {
            EmeraldHost element = (EmeraldHost) iter.next();
            removeHost(element);
        }
        for (Iterator iter = h.iterator(); iter.hasNext();) {
            EmeraldHost element = (EmeraldHost) iter.next();
            addHost(element);
        }
        //this.hosts = h;
    }
    
    public Vector[] getAsLists(){
        Vector[] v = new Vector[EmeraldHost.getNumSettings()];
        Vector hostlist = new Vector(hosts.size());
        Vector username = new Vector(hosts.size());
        Vector emx = new Vector(hosts.size());
        Vector enabled = new Vector(hosts.size());
        Vector arguments = new Vector(hosts.size());
        Vector roothost = new Vector(hosts.size());
        Vector files = new Vector(hosts.size());
        Vector priority = new Vector(hosts.size());
        Vector monitor = new Vector(hosts.size());
        for (Iterator iter = hosts.iterator(); iter.hasNext();) {
            EmeraldHost element = (EmeraldHost) iter.next();
            
            hostlist.add(hostlist.size(),element.getHost());
            username.add(username.size(),element.getUsername());
            emx.add(emx.size(),element.getEmx());
            enabled.add(enabled.size(),element.getEnabled().toString());
            arguments.add(arguments.size(),element.getArguments());
            roothost.add(roothost.size(),element.getRoothost().toString());
            files.add(files.size(),element.getFiles());
            priority.add(priority.size(),new String(element.getPriority().toString()));
            monitor.add(monitor.size(),element.getMonitor().toString());
        }  
        v[0] = hostlist;
        v[1] = username;
        v[2] = emx;
        v[3] = enabled;
        v[4] = arguments;
        v[5] = roothost;
        v[6] = files;
        v[7] = priority;
        v[8] = monitor;
        return v;
    }

    public Vector[] getAsSortedLists(){
        Vector[] v = new Vector[EmeraldHost.getNumSettings()];
        Vector hostlist = new Vector(hosts.size());
        Vector username = new Vector(hosts.size());
        Vector emx = new Vector(hosts.size());
        Vector enabled = new Vector(hosts.size());
        Vector arguments = new Vector(hosts.size());
        Vector roothost = new Vector(hosts.size());
        Vector files = new Vector(hosts.size());
        Vector priority = new Vector(hosts.size());
        Vector monitor = new Vector(hosts.size());
        
        apos[] s = new apos[hosts.size()];
        int i = 0;
        for (Iterator iter = hosts.iterator(); iter.hasNext();) {
            EmeraldHost element = (EmeraldHost) iter.next();
            apos p = new apos(i,element.getPriority().intValue());
            s[i] = p;
            i++;
        }
        bubbleSort(s);

        i = 0;
        EmeraldHost[] h = new EmeraldHost[hosts.size()];
        for (Iterator iter = hosts.iterator(); iter.hasNext();) {
            EmeraldHost element = (EmeraldHost) iter.next();
            int idx = indexOf(i,s);
            
            h[idx] = element;
            i++;
        }
        for (int j = 0; j < h.length; j++) {
            hostlist.add(hostlist.size(),h[j].getHost());
            username.add(username.size(),h[j].getUsername());
            emx.add(emx.size(),h[j].getEmx());
            enabled.add(enabled.size(),h[j].getEnabled().toString());
            arguments.add(arguments.size(),h[j].getArguments());
            roothost.add(roothost.size(),h[j].getRoothost().toString());
            files.add(files.size(),h[j].getFiles());
            priority.add(priority.size(),new String(h[j].getPriority().toString()));
            monitor.add(monitor.size(),h[j].getMonitor().toString());
        }  
        v[0] = hostlist;
        v[1] = username;
        v[2] = emx;
        v[3] = enabled;
        v[4] = arguments;
        v[5] = roothost;
        v[6] = files;
        v[7] = priority;
        v[8] = monitor;
        return v;
    }
    
    private class apos {
        public int pos;
        public int value;
        
        public apos(int pos, int value) {
            this.pos = pos;
            this.value = value;
        }
    }
  
    private int indexOf(int i, apos[] a){
        for (int j = 0; j < a.length; j++) {
            if (a[j].pos == i)
                return j;
        }
        return -1;
    }
    
    private apos[] bubbleSort(apos[] a) { 
        for (int i = a.length; --i >= 0; ) {
            for (int j = 0; j < i; j++) {
                if (a[j].value > a[j+1].value) {
                    apos temp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = temp;
                }
            }
        }
        return a;
    }
}
