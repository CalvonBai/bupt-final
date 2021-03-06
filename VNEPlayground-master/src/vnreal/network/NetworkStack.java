/* ***** BEGIN LICENSE BLOCK *****
 * Copyright (C) 2010-2011, The VNREAL Project Team.
 * 
 * This work has been funded by the European FP7
 * Network of Excellence "Euro-NF" (grant agreement no. 216366)
 * through the Specific Joint Developments and Experiments Project
 * "Virtual Network Resource Embedding Algorithms" (VNREAL). 
 *
 * The VNREAL Project Team consists of members from:
 * - University of Wuerzburg, Germany
 * - Universitat Politecnica de Catalunya, Spain
 * - University of Passau, Germany
 * See the file AUTHORS for details and contact information.
 * 
 * This file is part of ALEVIN (ALgorithms for Embedding VIrtual Networks).
 *
 * ALEVIN is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License Version 3 or later
 * (the "GPL"), or the GNU Lesser General Public License Version 3 or later
 * (the "LGPL") as published by the Free Software Foundation.
 *
 * ALEVIN is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * or the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License and
 * GNU Lesser General Public License along with ALEVIN; see the file
 * COPYING. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */
package vnreal.network;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mulavito.graph.AbstractLayerStack;
import vnreal.constraints.resources.AbstractResource;
import vnreal.network.substrate.SubstrateLink;
import vnreal.network.substrate.SubstrateNetwork;
import vnreal.network.substrate.SubstrateNode;
import vnreal.network.virtual.VirtualLink;
import vnreal.network.virtual.VirtualNetwork;
import vnreal.network.virtual.VirtualNode;

/**
 * The stack of {@link Network}s = {@link VirtualNetwork}s built upon the
 * {@link SubstrateNetwork}.
 * 
 * @author Michael Duelli
 * @since 2010-09-13
 */
public final class NetworkStack extends AbstractLayerStack<Network<?, ?, ?>> {
	private SubstrateNetwork substrate;

	//this Data Structure contains data from the Algorithm 
	private HashMap<String, Double> evaluationData = new HashMap<String, Double>();
	
	public NetworkStack(SubstrateNetwork substrate, List<VirtualNetwork> vns) {
		//If we create a new Stack, we can reset the id Counter of the thread
		//NetworkEntity.resetIDs();
		//NO we can't! We need a better improved IDSource....
		
		this.substrate = substrate;
		addLayer(substrate);
		layers.addAll(vns);
	}
	
	public NetworkStack getCopy(boolean deepCopy) {
		LinkedList<VirtualNetwork> vns = new LinkedList<VirtualNetwork>();
		for (Network<?,?,?> n : getVirtuals()) {
			vns.add(((VirtualNetwork) n).getCopy(false, deepCopy));
		}
		NetworkStack copy = new NetworkStack(substrate.getCopy(false, deepCopy), vns);
		
		return copy;
	}

	public SubstrateNetwork getSubstrate() {
		return substrate;
	}
	
	public List<VirtualNetwork> getVirtuals() {
		
		LinkedList<VirtualNetwork> vns = new LinkedList<VirtualNetwork>();
		boolean first = true;
		for (Network<?,?,?> n : layers) {
			if (first)
				first = false;
			else
				vns.add((VirtualNetwork) n);
		}
		
		return vns;
	}

	/**
	 * @param layer
	 *            The demanded layer id
	 * @return The substrate network for layer 0, a virtual network for higher
	 *         layers or <code>null</code> if no such layer exists.
	 */
	public Network<?,?,?> getLayer(int layer) {
		if (layer > layers.size() - 1)
			return null;

		return layers.get(layer);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("Network stack:\n");

		//sb.append(substrate.getLabel() + "\n" + substrate.toString() + "\n");

		for (Network<?, ?, ?> n : this)
			sb.append(n.getLabel() + "\n" + n.toString() + "\n");

		return sb.toString();
	}

	/** @return The number of networks including substrate and virtual networks. */
	public int size() {
		return layers.size();
	}

	public boolean hasMappings() {
		if (substrate != null) {
			for (SubstrateNode sn : substrate.getVertices()) {
				for (AbstractResource c : sn.get()) {
					if (c.getMappings().size() > 0)
						return true;
				}
			}
			for (SubstrateLink sl : substrate.getEdges()) {
				for (AbstractResource c : sl.get()) {
					if (c.getMappings().size() > 0)
						return true;
				}
			}
			return false;
		} else
			throw new AssertionError("Substrate network is null.");
	}

	/** unregister mapping of a virtual network that could not be mapped */
	public void clearVnrMappings(VirtualNetwork vNet) {
		vNet.clearVnrMappings();
		fireStateChanged();
	}
	
	public void clearMappings() {
		substrate.clearMappings();
        fireStateChanged();
	}
	
	public void generateDuplicateEdges() {
		for (Network<?,?,?> n : layers)
			n.generateDuplicateEdges();
	}

	public void clearConstraints() {
		SubstrateNetwork sn = substrate;
		for (SubstrateNode n : sn.getVertices())
			n.removeAll();
		for (SubstrateLink l : sn.getEdges())
			l.removeAll();

		for (int i = 1; i < size(); i++) {
			VirtualNetwork vn = (VirtualNetwork) getLayer(i);

			for (VirtualNode n : vn.getVertices())
				n.removeAll();
			for (VirtualLink l : vn.getEdges())
				l.removeAll();
		}
	}

	public HashMap<String, Double> getEvaluationData() {
		return evaluationData;
	}
	
	/**
	 * 
	 * @param name Name of the DataElement
	 * @param value Value
	 */
	public void addEvaluationData(String name, double value) {
		evaluationData.put(name, value);
	}
	
	/**
	 * 
	 * @param Map with EvaluationData
	 */
	public void addEvaluationData(Map<String, Double> data) {
		evaluationData.putAll(data);
	}
	
	
	public void removeDuplicateEdges() {
		for (Network<?,?,?> net : layers) {
			net.removeDuplicateEdges();
		}
	}
	
	public void removeVirtual(VirtualNetwork vnr) {
		if (layers.contains(vnr)) {
			layers.remove(vnr);
			fixLayers();
			fireStateChanged();
		}
	}
	
	private void fixLayers() {
		for (int i = 0; i < layers.size(); i++) {
			Network<?, ?, ?> net = layers.get(i);
			net.setLayer(i);
		}
	}
	
	
}
