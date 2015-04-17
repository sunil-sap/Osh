package com.hybris.osh.cockpits.cmscockpit.sitewizard;

import de.hybris.platform.commerceservices.enums.SiteChannel;

/**
 * Creates new B2B site from given information. Intended for usage within accelerator cms site wizard.
 */
public class B2BAcceleratorWizardHelper extends AcceleratorWizardHelper 
{

	@Override
	protected SiteChannel getSiteChannel() {
		return SiteChannel.B2B;
	}

}
