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
package vnreal.algorithms.utils;

import java.util.Collection;
import java.util.List;

import vnreal.constraints.demands.AbstractDemand;
import vnreal.constraints.demands.BandwidthDemand;
import vnreal.constraints.demands.CpuDemand;
import vnreal.constraints.resources.AbstractResource;
import vnreal.constraints.resources.BandwidthResource;
import vnreal.constraints.resources.CpuResource;
import vnreal.network.NetworkEntity;
import vnreal.network.substrate.SubstrateLink;
import vnreal.network.substrate.SubstrateNetwork;
import vnreal.network.substrate.SubstrateNode;
import vnreal.network.virtual.VirtualLink;
import vnreal.network.virtual.VirtualNode;

/**
 * Set of methods to realize the mapping of demands into resources, and verify
 * if they are possible.
 * 
 * @author Juan Felipe Botero
 * 
 */

public class NodeLinkAssignation {

	public NodeLinkAssignation() {

	}

	/**
	 * 
	 * @param demands
	 *            set of demands
	 * @param e
	 *            Network Entity (Node or link)
	 * @return boolean value indicating if the set of demands can be fulfilled
	 *         by the network entity e.
	 */
	public static boolean fulfills(
			Collection<? extends AbstractDemand> demands,
			NetworkEntity<? extends AbstractResource> e) {

		if (demands != null) {
			for (AbstractDemand dem : demands) {
				boolean found = false;
				for (AbstractResource res : e) {
					if (res.accepts(dem) && res.fulfills(dem)) {
						found = true;
						break;
					}
				}
				if (!found) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * 
	 * @param demands
	 *            set of demands
	 * @param e
	 *            Network Entity (Node or link)
	 * @return boolean value indicating if the set of demands could be occupied
	 *         by the network entity e.
	 */
	public static boolean occupy(Collection<? extends AbstractDemand> demands,
			NetworkEntity<? extends AbstractResource> e) {

		if (demands != null) {
			for (AbstractDemand dem : demands) {
				boolean found = false;
				for (AbstractResource res : e) {
					if (res.accepts(dem) && res.fulfills(dem)
							&& dem.occupy(res)) {
						found = true;
						break;
					}
				}
				if (!found) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 *
	 * @return boolean value indicating if the demand could be occupied by the
	 *         resource.
	 */
	public static boolean occupy(AbstractDemand demand,
			AbstractResource resource) {
		if (demand != null) {
			if (resource.accepts(demand) && resource.fulfills(demand)
					&& demand.occupy(resource)) {
				return true;
			} else {
				return false;
			}
		}

		return true;
	}

	/**
	 * 
	 * @param vn
	 *            Virtual Node
	 * @param sn
	 *            Substrate Node
	 * @return boolean value indicating whether vn can be mapped in sn
	 */
	public final static boolean isMappable(VirtualNode vn, SubstrateNode sn) {
		return fulfills(vn.get(), sn);
	}

	/**
	 * Performs virtual node mapping of a single virtual node
	 * 
	 * @param vn
	 *            virtual node
	 * @param sn
	 *            substrate node
	 * @return boolean value indicating whether mapping has been Successful or
	 *         not.
	 */
	public final static boolean vnm(VirtualNode vn, SubstrateNode sn) {
		return occupy(vn.get(), sn);

	}

	/**
	 * Performs virtual link mapping of a single virtual link into a substrate
	 * path
	 * 
	 * @param vl
	 *            virtual link
	 * @param spath
	 *            list of substrate link forming the path
	 * @param sNet
	 *            substrate network
	 * @param srcSNode
	 *            source substrate node
	 * @return boolean value indicating whether mapping has been Successful or
	 *         not.
	 */
	public final static boolean vlm(VirtualLink vl, List<SubstrateLink> spath,
			SubstrateNetwork sNet, SubstrateNode srcSNode) {
		for (SubstrateLink sl : spath) {
			// ... a resource to each link demand must be assigned.
			if (!occupy(vl.get(), sl)) {
				return false;
			} else {
				/*
				if (!sNet.getSource(sl).equals(srcSNode)) {
					if (!occupy(vl.getHiddenHopDemands(), sNet.getSource(sl)))
						return false;
				}
				*/
			}
		}
		return true;
	}

	public final static boolean vlm(VirtualLink vl, List<SubstrateLink> spath) {
		for (SubstrateLink sl : spath) {
			// ... a resource to each link demand must be assigned.
			if (!occupy(vl.get(), sl)) {
				return false;
			} else {
				/*
				if (!sNet.getSource(sl).equals(srcSNode)) {
					if (!occupy(vl.getHiddenHopDemands(), sNet.getSource(sl)))
						return false;
				}
				*/
			}
		}
		return true;
	}

	/**
	 * Method in charge of realizing the mapping of one substrate link to one
	 * virtual link (is used just to path splitting virtual link mapping
	 * algorithms), just bandwidth demands and cpu hidden hop demands are used
	 * 
	 * @param vl
	 *            link to be mapped
	 * @param bwDem
	 *            percentage of the bandwidth demand of vl to be mapped
	 * @param subsLink
	 *            substrate link to which the mapping should be done
	 * @return boolean value indicating whether mapping has been Successful or
	 *         not.
	 */
	public final static boolean vlmSingleLink(VirtualLink vl,
			BandwidthDemand bwDem, SubstrateLink subsLink) {
		CpuResource tempCpuRes;
		BandwidthResource tempBwRes;
		for (AbstractResource res : subsLink) {
			if (res instanceof BandwidthResource) {
				tempBwRes = (BandwidthResource) res;
				if ((int) ((double) (MiscelFunctions
						.round(tempBwRes.getAvailableBandwidth()
								- bwDem.getDemandedBandwidth(), 2) * 100)) == 0)
					bwDem.setDemandedBandwidth(tempBwRes
							.getAvailableBandwidth());
				// Mapping of bandwidth demand is performed
				if (occupy(bwDem, res)) {
					break;
				} else {
					return false;
				}
			}
		}

		/*
		if (hiddenHop != null) {
			for (AbstractResource res : hiddenHop) {
				if (res instanceof CpuResource) {
					tempCpuRes = (CpuResource) res;
					if ((int) ((double) (MiscelFunctions
							.round(tempCpuRes.getAvailableCycles()
									- hhDemand.getDemandedCycles(), 2) * 100)) == 0)
						hhDemand.setDemandedCycles(tempCpuRes
								.getAvailableCycles());
					// Mapping of cpu hidden hop demand is performed
					if (occupy(hhDemand, res)) {
						break;
					} else {
						return false;
					}
				}
			}
		}
		*/

		return true;
	}

	/**
	 * This method verifies if all the substrate links of a path accomplish the
	 * bandwidth and CPU (hidden hops) demands.
	 *
	 * @param path
	 * @return boolean value indicating whether the path accomplishes the
	 *         demands
	 */
	public final static boolean verifyPath(VirtualLink vl,
			List<SubstrateLink> path, SubstrateNode srcSNode,
			SubstrateNetwork sNet) {
		for (SubstrateLink tSLink : path) {
			if (!fulfills(vl.get(), tSLink)) {
				return false;
			} else {

			}
		}
		return true;
	}
}