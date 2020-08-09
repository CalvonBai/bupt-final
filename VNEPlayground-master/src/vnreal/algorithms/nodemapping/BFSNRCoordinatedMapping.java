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
package vnreal.algorithms.nodemapping;

/**
 * This is the class of the virtual embedding algorithm that calculates the coordinated VNE
 * based on node ranking and BFS virtual network tree 
 * 
 * This algorithm was proposed in:
 * 
 * X. Cheng, S. Su, Z. Zhang, H. Wang, F. Yang, Y. Luo, J. Wang, Virtual network embedding 
 * through topology-aware node ranking, SIGCOMM Comput. Commun. Rev. 41 (2011) 38--47.
 * 
 * @author Juan Felipe Botero
 * @since 2011-02-10
 */

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraDistance;
import mulavito.algorithms.shortestpath.ksp.Yen;
import vnreal.algorithms.AbstractNodeMapping;
import vnreal.algorithms.utils.LinkWeight;
import vnreal.algorithms.utils.MiscelFunctions;
import vnreal.algorithms.utils.NodeLinkAssignation;
import vnreal.algorithms.utils.SubstrateLinkSingleTransformer;
import vnreal.algorithms.utils.breadthfirstsearch.BFS;
import vnreal.algorithms.utils.tree.GenericTree;
import vnreal.algorithms.utils.tree.GenericTreeNode;
import vnreal.network.Node;
import vnreal.network.substrate.SubstrateLink;
import vnreal.network.substrate.SubstrateNetwork;
import vnreal.network.substrate.SubstrateNode;
import vnreal.network.virtual.VirtualLink;
import vnreal.network.virtual.VirtualNetwork;
import vnreal.network.virtual.VirtualNode;

import java.util.*;

public class BFSNRCoordinatedMapping extends AbstractNodeMapping {
	private double epsilon;
	private boolean withDist;
	private int dist, backtrackLimit, k;
	private Map<Node<?>, Double> SortedNodeNR_i_opt_virt;
	GenericTree<Node<?>> bfsTree;

	@Deprecated
	public BFSNRCoordinatedMapping(SubstrateNetwork sNet,
			 double epsilon, boolean withDist,
			int dist, int k) {
		this.epsilon = epsilon;
		this.dist = dist;
		if (!withDist) { this.dist = -1; }
		this.backtrackLimit = 0;
		this.k = k;
		this.SortedNodeNR_i_opt_virt = null;
		this.bfsTree = null;

	}
	
	public BFSNRCoordinatedMapping( double epsilon,
			int dist, int k) {
		this.epsilon = epsilon;
		this.dist = dist;
		this.backtrackLimit = 0;
		this.k = k;
		this.SortedNodeNR_i_opt_virt = null;
		this.bfsTree = null;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	protected boolean nodeMapping(SubstrateNetwork sNet, VirtualNetwork vNet) {
		//In this phase both node and link mapping are performed
		/*
		 * See Algorithm 4
		 * X. Cheng, S. Su, Z. Zhang, H. Wang, F. Yang, Y. Luo, J. Wang, Virtual network embedding 
		 * through topology-aware node ranking, SIGCOMM Comput. Commun. Rev. 41 (2011) 38--47.
		 */
		this.backtrackLimit = 3 * vNet.getVertices().size();
		int backtrack_count = 0;
		List<VirtualNode> discVnodes = new LinkedList<VirtualNode>();
		Map<Node<?>, Double> SortedNodeNR_i_opt_subs = MiscelFunctions
				.sortByValue(MiscelFunctions.create_NR(sNet, epsilon));
		SortedNodeNR_i_opt_virt = MiscelFunctions.sortByValue(MiscelFunctions
				.create_NR(vNet, epsilon));
		VirtualNode discVnode = null;

		Map<VirtualNode, List<SubstrateNode>> currCandidateSet = new LinkedHashMap<VirtualNode, List<SubstrateNode>>();
		List<SubstrateNode> tempCandidateSet = new LinkedList<SubstrateNode>();

		/* Throwing away disconnected virtual nodes (in the case that VNR has disconnected nodes) */
		if (SortedNodeNR_i_opt_virt.containsValue(0.0)) {
			for (Iterator<Node<?>> vNode = SortedNodeNR_i_opt_virt.keySet()
					.iterator(); vNode.hasNext();) {
				discVnode = (VirtualNode) vNode.next();
				if (SortedNodeNR_i_opt_virt.get(discVnode) == 0.0
						&& vNet.getIncidentEdges(discVnode).isEmpty()) {
					discVnodes.add(discVnode);
				}

			}
		}
		for (VirtualNode vNode : discVnodes) {
			SortedNodeNR_i_opt_virt.remove(vNode);
		}

		@SuppressWarnings("rawtypes")
		List<SubstrateNode> tmpCandiNodes = new LinkedList(
				SortedNodeNR_i_opt_subs.keySet());
		// Creation of candidates for each virtual node //
		for (VirtualNode currVnode : vNet.getVertices()) {
			if (withDist) {
				currCandidateSet.put(currVnode,
						MiscelFunctions.findFulfillingNodes(currVnode,
								tmpCandiNodes, dist));

			} else {
				currCandidateSet.put(currVnode, MiscelFunctions
						.findFulfillingNodes(currVnode, tmpCandiNodes,
								nodeMapping));
			}
		}
		// ///////////////////////////////////////////////
		BFS BFSVnet = new BFS(SortedNodeNR_i_opt_virt.keySet().iterator()
				.next(), vNet, SortedNodeNR_i_opt_virt);
		List<Node<?>> bfsQueue = BFSVnet.search();
		bfsTree = BFSVnet.getNodeTree();
		boolean wasBacktrack = false;
		if (bfsQueue != null) {
			for (int i = 0; i < bfsQueue.size(); i++) {
				VirtualNode currVnode = (VirtualNode) bfsQueue.get(i);
				if (wasBacktrack) {
					tempCandidateSet = currCandidateSet.get(currVnode);
					tempCandidateSet.remove(nodeMapping.get(currVnode));
					if (tempCandidateSet.isEmpty())
						return false;

					currCandidateSet.put(currVnode, tempCandidateSet);
					MiscelFunctions.clearVnodeMappings(vNet, currVnode);
					nodeMapping.remove(currVnode);
				}
				/*
				 * Match function is called See Algorithm 4
				 * X. Cheng, S. Su, Z. Zhang, H. Wang, F. Yang, Y. Luo, J. Wang, Virtual network embedding 
				 * through topology-aware node ranking, SIGCOMM Comput. Commun. Rev. 41 (2011) 38--47. 
				 */
				if (currCandidateSet.get(currVnode).isEmpty()
						|| !match(sNet, vNet, currVnode,
								currCandidateSet.get(currVnode))) {
					if (i == 0 && backtrack_count == 0) {
						return false;
					}
					backtrack_count++;
					i = i - 2;
					wasBacktrack = true;
				} else {
					wasBacktrack = false;
				}
				if (backtrack_count > backtrackLimit)
					return false;
			}
		}
		/* Mapping of disconnected Nodes */
		if (!discVnodes.isEmpty()) {
			for (VirtualNode discVnod : discVnodes) {
				if (withDist) {
					currCandidateSet.put(discVnod, MiscelFunctions
							.findFulfillingNodes(discVnod, tmpCandiNodes,
									dist, nodeMapping));

				} else {
					currCandidateSet.put(discVnod, MiscelFunctions
							.findFulfillingNodes(discVnod, tmpCandiNodes,
									nodeMapping));
				}

				if(!currCandidateSet.get(discVnod).isEmpty()){
					SubstrateNode chosenSnode = currCandidateSet.get(discVnod)
					.iterator().next();
					nodeMapping.put(discVnod, chosenSnode);
					if (!NodeLinkAssignation.vnm(discVnod, chosenSnode))
						throw new AssertionError("But we checked before!");
				}else{
					return false;
				}

			}
		}
		/* ************************************* */
		return true;
	}
	
	/**
	 * Match function (see Algorithm 5).
	 * X. Cheng, S. Su, Z. Zhang, H. Wang, F. Yang, Y. Luo, J. Wang, Virtual network embedding 
	 * through topology-aware node ranking, SIGCOMM Comput. Commun. Rev. 41 (2011) 38--47.
	 * 
	 * @param vNet
	 * @param vNode
	 * @param candidates
	 * @return a boolean value indicating whether the virtual node could be mapped 
	 * or not 
	 */
	private boolean match(SubstrateNetwork sNet, VirtualNetwork vNet, VirtualNode vNode,
                          List<SubstrateNode> candidates) {
		Iterator<Node<?>> tempIt = SortedNodeNR_i_opt_virt.keySet().iterator();
		List<SubstrateNode> tempCandiNodes = null;

		if (((VirtualNode) tempIt.next()).equals(vNode)) {
			if (NodeLinkAssignation.vnm(vNode, candidates.iterator().next())) {
				nodeMapping.put(vNode, candidates.iterator().next());
				return true;
			} else {
				throw new AssertionError("But we checked before!");
			}
		}
		GenericTreeNode<Node<?>> tempNode = bfsTree.find(vNode);
		VirtualNode parentNode = (VirtualNode) tempNode.getParent().getData();

		for (int tmpK = 1; tmpK < k + 1; tmpK++) {
			DijkstraDistance<SubstrateNode, SubstrateLink> distanceGraph = new DijkstraDistance<SubstrateNode, SubstrateLink>(
					sNet, new SubstrateLinkSingleTransformer(), false);
			distanceGraph.setMaxDistance(tmpK);
			Map<SubstrateNode, Number> orderedMappedCandidates = distanceGraph
					.getDistanceMap(nodeMapping.get(parentNode));
			tempCandiNodes = new LinkedList<SubstrateNode>(
					orderedMappedCandidates.keySet());
			candidates = new LinkedList<SubstrateNode>();

			if (!orderedMappedCandidates.isEmpty()) {
				if (withDist) {
					candidates = MiscelFunctions.findFulfillingNodes(vNode,
							tempCandiNodes, dist, nodeMapping);

				} else {
					candidates = MiscelFunctions.findFulfillingNodes(vNode,
							tempCandiNodes, nodeMapping);
				}
				for (SubstrateNode sCorrNode : candidates) {
					if (MapPathsWithMappedNodes(sNet, vNet, vNode, sCorrNode)) {
						if (!NodeLinkAssignation.vnm(vNode, sCorrNode)) {
							throw new AssertionError("But we checked before!");
						} else {
							nodeMapping.put(vNode, sCorrNode);
							return true;
						}
					} else {
						MiscelFunctions.clearVnodeMappings(vNet, vNode);
					}
				}
			}
		}
		return false;
	}

	private boolean MapPathsWithMappedNodes(SubstrateNetwork sNet, VirtualNetwork vNet,
                                            VirtualNode vNode, SubstrateNode sNode) {
		SubstrateNode dstNode, srcNode;
		for (VirtualLink vLink : vNet.getIncidentEdges(vNode)) {

			VirtualNode tmpvNode = null;

			if(vNet.getEndpoints(vLink).getFirst() == vNode){
				tmpvNode = vNet.getEndpoints(vLink).getSecond();
			}
			else if(vNet.getEndpoints(vLink).getSecond() == vNode){
				tmpvNode = vNet.getEndpoints(vLink).getFirst();
			}

			if (nodeMapping.containsKey(tmpvNode)
					&& !MiscelFunctions.hasLinkMappings(vLink)) {
				dstNode = nodeMapping.get(tmpvNode);
				if (!sNode.equals(dstNode))
					if (!performLinkMapping(sNet, sNode, dstNode, vLink)) {
						return false;
					}
			}
		}

		return true;
	}

	private boolean performLinkMapping(SubstrateNetwork sNet, SubstrateNode srcSnode,
                                       SubstrateNode dstSnode, VirtualLink vLink) {
		// Search for path in filtered substrate using
		// KShortestPaths
		LinkWeight linkWeight = new LinkWeight();
		Yen<SubstrateNode, SubstrateLink> kshortestPaths = new Yen<SubstrateNode, SubstrateLink>(sNet,
				linkWeight);

		// get the k shortest paths to the dstSnode in
		// increasing order of weight
		List<List<SubstrateLink>> paths = kshortestPaths.getShortestPaths(
				srcSnode, dstSnode, 20);
		List<SubstrateLink> mappedPath = null;
		for (List<SubstrateLink> path : paths) {

			// Verify if the path fulfills the demand
			if (NodeLinkAssignation.verifyPath(vLink, path, srcSnode, sNet)) {
				// If the path has been verified, the path is
				// chosen if not,
				// the following shortest path is verified
				mappedPath = path;
				break;
			}
		}
		paths.clear();
		// if a path fulfilling the demand has been chosen, link
		// mapping is performed
		if (mappedPath != null) {
			// Perform virtual link mapping (VLM) for each link in
			// the path.
			if (!NodeLinkAssignation.vlm(vLink, mappedPath, sNet, srcSnode)) {
				throw new AssertionError("But we checked before!");
			}
		} else {// Not path available, link mapping can not be
			// performed
			return false;
		}

		return true;
	}
}
