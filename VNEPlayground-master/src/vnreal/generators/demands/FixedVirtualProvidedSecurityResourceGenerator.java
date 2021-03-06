package vnreal.generators.demands;

import java.util.ArrayList;
import java.util.Random;

import tests.generators.GeneratorParameter;
import vnreal.constraints.demands.VirtualProvidedSecurity;
import vnreal.core.oldFramework.ConversionHelper;
import vnreal.network.NetworkStack;
import vnreal.network.virtual.VirtualLink;
import vnreal.network.virtual.VirtualNetwork;
import vnreal.network.virtual.VirtualNode;

/**
 * The random {@link VirtualProvidedSecurity} Generator 
 * 
 * @author Fabian Kokot
 *
 */
@GeneratorParameter(
		parameters = { "Networks:Networks", "TR:Min_PDS", "TR:Max_PDS", "Seed:Seed" }
		)
public class FixedVirtualProvidedSecurityResourceGenerator extends AbstractDemandGenerator<Integer> {

	
	@Override
	public Integer generate(ArrayList<Object> parameters) {
		NetworkStack ns = (NetworkStack)parameters.get(0);
		Integer minPDS = ConversionHelper.paramObjectToInteger(parameters.get(1));
		Integer maxPDS = ConversionHelper.paramObjectToInteger(parameters.get(2));
		Long seed = ConversionHelper.paramObjectToLong(parameters.get(3));
		
		Random random = new Random();
		random.setSeed(seed);
		
		for(int u = 1; u < ns.size(); u++) {
			
			VirtualNetwork vNetwork = (VirtualNetwork)ns.getLayer(u);
		
			//Label all VirtualNodes (actual Random)
			for (VirtualNode n :  vNetwork.getVertices()) {
				VirtualProvidedSecurity res = new VirtualProvidedSecurity(n);
				int value = (int) (minPDS + (maxPDS
						- minPDS + 1)
						* random.nextDouble());
				res.setVirtualProvidedSecurityLevel((double) value);
				n.add(res);
				
			}
			
			for (VirtualLink n :  vNetwork.getEdges()) {
				VirtualProvidedSecurity res = new VirtualProvidedSecurity(n);
				int value = (int) (minPDS + (maxPDS
						- minPDS + 1)
						* random.nextDouble());
				res.setVirtualProvidedSecurityLevel((double) value);
				n.add(res);
				
			}
		}
		return 0;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
