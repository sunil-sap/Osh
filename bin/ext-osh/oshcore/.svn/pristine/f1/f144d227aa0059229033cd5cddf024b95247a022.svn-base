package com.hybris.osh.core.jalo;

import com.hybris.osh.core.constants.OshCoreConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class OshCoreManager extends GeneratedOshCoreManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( OshCoreManager.class.getName() );
	
	public static final OshCoreManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (OshCoreManager) em.getExtension(OshCoreConstants.EXTENSIONNAME);
	}
	
}
