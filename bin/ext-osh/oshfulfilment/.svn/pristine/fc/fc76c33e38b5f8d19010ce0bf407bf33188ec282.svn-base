/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2012 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package com.hybris.osh.test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.order.impl.DefaultCartService;
import de.hybris.platform.order.impl.DefaultOrderService;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.payment.commands.factory.CommandFactory;
import de.hybris.platform.payment.commands.factory.impl.DefaultCommandFactoryRegistryImpl;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.definition.ProcessDefinitionFactory;
import de.hybris.platform.processengine.impl.DefaultBusinessProcessService;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.ProcessTaskModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.product.impl.DefaultProductService;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.user.impl.DefaultUserService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.util.Utilities;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;



public class PaymentIntegrationTest extends ServicelayerTest
{

	static private final Logger LOG = Logger.getLogger(PaymentIntegrationTest.class);

	private static BusinessProcessService processService;
	private static ProcessDefinitionFactory definitonFactory;

	private static ProductService productService;
	private static CartService cartService;
	private static ModelService modelService;
	private static UserService userService;
	private static OrderService orderService;

	private static TaskServiceStub taskServiceStub;



	@BeforeClass
	public static void prepare() throws Exception //NOPMD
	{
		Registry.activateStandaloneMode();
		Utilities.setJUnitTenant();
		LOG.debug("Preparing...");


		final ApplicationContext appCtx = Registry.getGlobalApplicationContext();

		assertTrue("Application context of type " + appCtx.getClass() + " is not a subclass of "
				+ ConfigurableApplicationContext.class, appCtx instanceof ConfigurableApplicationContext);

		final ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext) appCtx;
		final ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
		if (beanFactory.getRegisteredScope("tenant") == null)
		{
			beanFactory.registerScope("tenant", new de.hybris.platform.spring.TenantScope());
		}
		assertTrue("Bean Factory of type " + beanFactory.getClass() + " is not of type " + BeanDefinitionRegistry.class,
				beanFactory instanceof BeanDefinitionRegistry);
		final XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader((BeanDefinitionRegistry) beanFactory);
		xmlReader.loadBeanDefinitions(new ClassPathResource("oshfulfilment-spring-test.xml"));


		modelService = (ModelService) getBean("modelService");
		processService = (BusinessProcessService) getBean("businessProcessService");
		definitonFactory = ((DefaultBusinessProcessService) processService).getProcessDefinitionFactory();

		LOG.warn("Prepare Process Definition factory...");
		LOG.warn("Tennant is: " + JaloSession.getCurrentSession().getTenant().getTenantID());
		definitonFactory.add("classpath:/processdemo/test/testPaymentIntegration.xml");

		//setup command factory to mock
		taskServiceStub = appCtx.getBean(TaskServiceStub.class);
		final DefaultCommandFactoryRegistryImpl commandFactoryReg = appCtx.getBean(DefaultCommandFactoryRegistryImpl.class);
		commandFactoryReg.setCommandFactoryList(Arrays.asList((CommandFactory) appCtx.getBean("mockupCommandFactory")));



		//		final DefaultCommandFactoryRegistryImpl commandFactoryRegImpl = Registry.getApplicationContext().getBean(
		//				DefaultCommandFactoryRegistryImpl.class);
		//		commandFactoryRegImpl.afterPropertiesSet();

		taskServiceStub = appCtx.getBean(TaskServiceStub.class);
		productService = appCtx.getBean("defaultProductService", DefaultProductService.class);
		cartService = appCtx.getBean("defaultCartService", DefaultCartService.class);
		userService = appCtx.getBean("defaultUserService", DefaultUserService.class);
		orderService = appCtx.getBean("defaultOrderService", DefaultOrderService.class);
	}

	@Before
	public void setUpProductCatalogue()
	{


		try
		{
			new CoreBasicDataCreator().createEssentialData(Collections.EMPTY_MAP, null);
			importCsv("/test/testBasics.csv", "windows-1252");
			importCsv("/test/testCatalog.csv", "windows-1252");
			LOG.warn("Catalogue has been imported");
		}
		catch (final ImpExException e)
		{
			LOG.warn("Catalogue import has failed");
			e.printStackTrace();
		}
		catch (final Exception e)
		{
			LOG.warn("createEssentialData(...) has failed");
			e.printStackTrace();
		}
	}


	@AfterClass
	public static void removeProcessDefinitions()
	{
		LOG.debug("cleanup...");


		final ApplicationContext appCtx = Registry.getGlobalApplicationContext();

		assertTrue("Application context of type " + appCtx.getClass() + " is not a subclass of "
				+ ConfigurableApplicationContext.class, appCtx instanceof ConfigurableApplicationContext);

		final ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext) appCtx;
		final ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
		if (beanFactory.getRegisteredScope("tenant") == null)
		{
			beanFactory.registerScope("tenant", new de.hybris.platform.spring.TenantScope());
		}
		assertTrue("Bean Factory of type " + beanFactory.getClass() + " is not of type " + BeanDefinitionRegistry.class,
				beanFactory instanceof BeanDefinitionRegistry);
		final XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader((BeanDefinitionRegistry) beanFactory);
		xmlReader.loadBeanDefinitions(new ClassPathResource("oshfulfilment-spring-testcleanup.xml"));


		//cleanup command factory
		final Map<String, CommandFactory> commandFactoryList = applicationContext.getBeansOfType(CommandFactory.class);
		commandFactoryList.remove("mockupCommandFactory");
		final DefaultCommandFactoryRegistryImpl commandFactoryReg = appCtx.getBean(DefaultCommandFactoryRegistryImpl.class);
		commandFactoryReg.setCommandFactoryList(commandFactoryList.values());


		if (definitonFactory != null)
		{
			// TODO this test seems to let processes run after method completion - therfore we cannot
			// remove definitions !!!
			//			definitonFactory.remove("testPlaceorder");
			//			definitonFactory.remove("testConsignmentFulfilmentSubprocess");
		}
		definitonFactory = null;
		processService = null;
	}

	@After
	public void resetServices()
	{
		final List<TaskModel> tasks = taskServiceStub.cleanup();
		final StringBuffer msg = new StringBuffer();
		for (final TaskModel task : tasks)
		{
			final ProcessTaskModel processTask = (ProcessTaskModel) task;

			msg.append(processTask.getAction()).append(", ");
		}

		assertTrue("taks should be empty after test execution. Left: " + msg, tasks.isEmpty());
	}


	@Test
	public void testPaymentIntegrationAccepted() throws InterruptedException
	{
		try
		{
			final OrderModel order = createValidOrder(true);

			final BusinessProcessModel process = createProcess("testPaymentIntegration");
			final OrderProcessModel orderProcess = (OrderProcessModel) process; // it is defined this way in the bp XML file
			orderProcess.setOrder(order);
			modelService.save(orderProcess);
			assertStep(process, "checkOrder");
			assertStep(process, "checkAuthorizeOrderPayment");
			assertStep(process, "cancelOrder");
			assertStep(process, "notifyCustomer");
		}
		catch (final InvalidCartException e)
		{
			Logger.getLogger(this.getClass()).info("Problems with the chart detected");
		}
	}

	@Test
	public void testPaymentIntegrationDeclined() throws InterruptedException
	{
		try
		{
			final OrderModel order = createValidOrder(false);

			final BusinessProcessModel process = createProcess("testPaymentIntegration");
			final OrderProcessModel orderProcess = (OrderProcessModel) process; // it is defined this way in the bp XML file
			orderProcess.setOrder(order);
			modelService.save(orderProcess);
			assertStep(process, "checkOrder");
			assertStep(process, "checkAuthorizeOrderPayment");

			assertStep(process, "notifyCustomer");
		}
		catch (final InvalidCartException e)
		{
			Logger.getLogger(this.getClass()).info("Problems with the chart detected");
		}
	}

	//tools
	private OrderModel createValidOrder(final boolean valid) throws InvalidCartException
	{
		// make an order
		final ProductModel product0 = productService.getProduct("testProduct0");
		final ProductModel product1 = productService.getProduct("testProduct1");
		final ProductModel product2 = productService.getProduct("testProduct2");
		final CartModel cart = cartService.getSessionCart();
		cartService.addToCart(cart, product0, 2, null);
		cartService.addToCart(cart, product1, 2, null);
		cartService.addToCart(cart, product2, 2, null);

		final AddressModel deliveryAddress = new AddressModel();
		deliveryAddress.setOwner(userService.getCurrentUser());
		deliveryAddress.setFirstname("Juergen");
		deliveryAddress.setLastname("Albertsen");
		deliveryAddress.setTown("Muenchen");
		modelService.save(deliveryAddress);

		final CreditCardPaymentInfoModel creditCardInfo = new CreditCardPaymentInfoModel();
		creditCardInfo.setCcOwner("Jurgen Albersten");
		creditCardInfo.setNumber("4111111111111111");
		creditCardInfo.setType(CreditCardType.VISA);
		creditCardInfo.setValidToMonth("12");
		if (valid)
		{
			creditCardInfo.setValidToYear("2100"); // we will be all dead by then...
		}
		else
		{
			creditCardInfo.setValidToYear("2000");
		}
		creditCardInfo.setUser(userService.getCurrentUser());
		creditCardInfo.setCode("XCODE");
		modelService.save(creditCardInfo);

		final OrderModel order = orderService.placeOrder(cart, deliveryAddress, deliveryAddress, creditCardInfo);
		modelService.save(order); // probably not necessary

		return order;
	}

	private BusinessProcessModel createProcess(final String processName)
	{
		final String id = "Test" + (new Date()).getTime();
		return processService.startProcess(id, processName);
	}

	private void assertStep(final BusinessProcessModel process, final String bean) throws InterruptedException
	{
		Logger.getLogger(this.getClass()).info("assertStep action = " + bean);

		try
		{
			final ProcessTaskModel processTaskModel = taskServiceStub.runProcessTask(bean);

			if (bean == null)
			{
				final StringBuffer found = new StringBuffer();

				for (final TaskModel task : taskServiceStub.getTasks())
				{
					if (task instanceof ProcessTaskModel)
					{
						found.append(((ProcessTaskModel) task).getAction()).append("; ");
					}
				}

				assertNotNull("No taks found for bean " + bean + " found: " + found.toString(), processTaskModel);
			}


		}
		catch (final RetryLaterException e)
		{
			fail(e.toString());
		}
	}

	private static Object getBean(final String name)
	{
		return Registry.getApplicationContext().getBean(name);
	}

}
