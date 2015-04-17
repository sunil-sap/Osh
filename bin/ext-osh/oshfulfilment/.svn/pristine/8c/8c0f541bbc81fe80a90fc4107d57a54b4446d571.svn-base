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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import de.hybris.platform.core.Registry;
import com.hybris.osh.test.actions.TestActionTemp;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.payment.commands.factory.CommandFactory;
import de.hybris.platform.payment.commands.factory.impl.DefaultCommandFactoryRegistryImpl;
import de.hybris.platform.processengine.definition.ProcessDefinitionFactory;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.processengine.impl.DefaultBusinessProcessService;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.ProcessTaskModel;
import de.hybris.platform.processengine.spring.Action;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.impl.DefaultTaskService;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.testframework.TestUtils;
import de.hybris.platform.util.Utilities;

import java.util.Arrays;
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



public class ProcessFlowTest extends HybrisJUnit4Test
{

	private static TaskServiceStub taskServiceStub;



	static private final Logger LOG = Logger.getLogger(ProcessFlowTest.class);

	private static DefaultBusinessProcessService processService;
	private static ProcessDefinitionFactory definitonFactory;
	private static ModelService modelService;


	@BeforeClass
	public static void prepare() throws Exception //NOPMD
	{
		Registry.activateStandaloneMode();
		Utilities.setJUnitTenant();
		LOG.debug("Preparing...");



		final ApplicationContext appCtx = Registry.getApplicationContext();

		//		final ConfigurationService configurationService = (ConfigurationService) appCtx.getBean("configurationService");
		//		configurationService.getConfiguration().setProperty("processengine.event.lockProcess", "true");

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
		processService = (DefaultBusinessProcessService) getBean("businessProcessService");
		definitonFactory = processService.getProcessDefinitionFactory();



		LOG.warn("Prepare Process Definition factory...");
		LOG.warn("Tennant is: " + JaloSession.getCurrentSession().getTenant().getTenantID());
		definitonFactory.add("classpath:/processdemo/test/testPlaceorder.xml");
		definitonFactory.add("classpath:/processdemo/test/testConsignmentFulfilmentsubprocess.xml");
		LOG.warn("loaded 'testPlaceorder':" + definitonFactory.getProcessDefinition("testPlaceorder") + " in factory "
				+ definitonFactory);


		//setup command factory to mock
		taskServiceStub = appCtx.getBean(TaskServiceStub.class);
		processService.setTaskService(taskServiceStub);

		final DefaultCommandFactoryRegistryImpl commandFactoryReg = appCtx.getBean(DefaultCommandFactoryRegistryImpl.class);
		commandFactoryReg.setCommandFactoryList(Arrays.asList((CommandFactory) appCtx.getBean("mockupCommandFactory")));

	}

	@Before
	public void setupActions()
	{
		setResultForAction("test.checkAuthorizeOrderPayment", "OK");
		setThrowExceptionForAction("test.reserveOrderAmount", false);
		setResultForAction("test.reserveOrderAmount", "OK");
		setResultForAction("test.fraudCheckOrderInternal", "OK");
		setResultForAction("test.fraudCheckOrder", "OK");
		setResultForAction("test.receiveConsignmentStatus", "OK");
		setResultForAction("test.takePayment", "OK");
	}

	@AfterClass
	public static void removeProcessDefinitions()
	{
		LOG.debug("cleanup...");


		final ApplicationContext appCtx = Registry.getApplicationContext();

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
		processService.setTaskService(appCtx.getBean(DefaultTaskService.class));
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

		//assertTrue("taks should be empty after test execution. Left: " + msg, tasks.isEmpty());
	}

	@Test
	public void testErrorCode() throws InterruptedException
	{
		setResultForAction("test.checkOrder", Action.ERROR_RETURN_CODE);
		try
		{
			final BusinessProcessModel process = createProcess("testPlaceorder");
			TestUtils.disableFileAnalyzer(400);
			assertStep(process, "checkOrder");

		}
		finally
		{
			setResultForAction("test.checkOrder", "OK");
			TestUtils.enableFileAnalyzer();
		}
	}

	@Test
	public void testProcessFraudFinalOk() throws InterruptedException
	{
		setResultForAction("test.fraudCheckOrderInternal", "NOK");

		final BusinessProcessModel process = createProcess("testPlaceorder");

		assertStep(process, "checkOrder");
		assertStep(process, "checkAuthorizeOrderPayment");
		assertStep(process, "reserveAmount");
		assertStep(process, "fraudCheck");
		assertStep(process, "notifyCustomer");
		assertStep(process, "manualOrderCheckCSA");
		assertStep(process, "sendOrderPlacedNotification");
		assertStep(process, "splitOrder");


		assertStep(process, "waitBeforeTransmission");
		assertStep(process, "sendConsignmentToWarehouse");
		assertStep(process, "receiveConsignmentStatus");
		assertStep(process, "takePayment");
		assertStep(process, "allowShipment");
		assertStep(process, "sendDeliveryMessage");
		assertStep(process, "subprocessEnd");

		assertStep(process, "isProcessCompleted");
		assertStep(process, "sendOrderCompletedNotification");

	}



	@Test
	public void testConsignmentPaymentNotTaken() throws InterruptedException
	{
		setResultForAction("test.takePayment", "NOK");

		final BusinessProcessModel process = createProcess("testPlaceorder");

		assertStep(process, "checkOrder");
		assertStep(process, "checkAuthorizeOrderPayment");
		assertStep(process, "reserveAmount");
		assertStep(process, "fraudCheck");
		assertStep(process, "sendOrderPlacedNotification");
		assertStep(process, "splitOrder");

		assertStep(process, "waitBeforeTransmission");
		assertStep(process, "sendConsignmentToWarehouse");
		assertStep(process, "receiveConsignmentStatus");
		assertStep(process, "takePayment");
		assertStep(process, "sendPaymentFailedMessage");
		assertStep(process, "subprocessEnd");

		assertStep(process, "isProcessCompleted");
		assertStep(process, "sendOrderCompletedNotification");
	}

	@Test
	public void testConsignmentStatusPartial() throws InterruptedException
	{
		setResultForAction("test.receiveConsignmentStatus", "PARTIAL");

		final BusinessProcessModel process = createProcess("testPlaceorder");

		assertStep(process, "checkOrder");
		assertStep(process, "checkAuthorizeOrderPayment");
		assertStep(process, "reserveAmount");
		assertStep(process, "fraudCheck");
		assertStep(process, "sendOrderPlacedNotification");
		assertStep(process, "splitOrder");

		assertStep(process, "waitBeforeTransmission");
		assertStep(process, "sendConsignmentToWarehouse");
		assertStep(process, "receiveConsignmentStatus");
		assertStep(process, "calculatePayment");
		assertStep(process, "takePayment");
		assertStep(process, "allowShipment");
		assertStep(process, "sendDeliveryMessage");
		assertStep(process, "subprocessEnd");

		assertStep(process, "isProcessCompleted");
		assertStep(process, "sendOrderCompletedNotification");
	}

	@Test
	public void testErrorInProcess() throws InterruptedException
	{
		setResultForAction("test.reserveOrderAmount", Action.ERROR_RETURN_CODE);

		final BusinessProcessModel process = createProcess("testPlaceorder");

		assertStep(process, "checkOrder");
		assertStep(process, "checkAuthorizeOrderPayment");
		TestUtils.disableFileAnalyzer(400);
		try
		{
			assertStep(process, "reserveAmount");
		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}
		Thread.sleep(1000);

		modelService.refresh(process);
		assertEquals("Process state", ProcessState.ERROR, process.getProcessState());
	}

	@Test
	public void testExceptionInProcess() throws InterruptedException
	{
		setThrowExceptionForAction("test.reserveOrderAmount", true);

		final BusinessProcessModel process = createProcess("testPlaceorder");

		assertStep(process, "checkOrder");
		assertStep(process, "checkAuthorizeOrderPayment");
		TestUtils.disableFileAnalyzer(400);
		try
		{
			assertStep(process, "reserveAmount");
		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}

		Thread.sleep(1000);

		modelService.refresh(process);
		assertEquals("Process state", ProcessState.ERROR, process.getProcessState());
	}



	@Test
	public void testConsignmentStatusCancel() throws InterruptedException
	{
		setResultForAction("test.fraudCheckOrderInternal", "NOK");
		setResultForAction("test.receiveConsignmentStatus", "CANCEL");

		final BusinessProcessModel process = createProcess("testPlaceorder");

		assertStep(process, "checkOrder");
		assertStep(process, "checkAuthorizeOrderPayment");
		assertStep(process, "reserveAmount");
		assertStep(process, "fraudCheck");
		assertStep(process, "notifyCustomer");
		assertStep(process, "manualOrderCheckCSA");
		assertStep(process, "sendOrderPlacedNotification");
		assertStep(process, "splitOrder");

		assertStep(process, "waitBeforeTransmission");
		assertStep(process, "sendConsignmentToWarehouse");
		assertStep(process, "receiveConsignmentStatus");
		assertStep(process, "cancelConsignment");
		assertStep(process, "sendCancelMessage");
		assertStep(process, "subprocessEnd");


		assertStep(process, "isProcessCompleted");
		assertStep(process, "sendOrderCompletedNotification");
	}

	@Test
	public void testProcessFraudFinalFail() throws InterruptedException
	{
		setResultForAction("test.fraudCheckOrderInternal", "NOK");
		setResultForAction("test.fraudCheckOrder", "NOK");

		final BusinessProcessModel process = createProcess("testPlaceorder");

		assertStep(process, "checkOrder");
		assertStep(process, "checkAuthorizeOrderPayment");
		assertStep(process, "reserveAmount");
		assertStep(process, "fraudCheck");
		assertStep(process, "notifyCustomer");
		assertStep(process, "manualOrderCheckCSA");
		assertStep(process, "cancelOrder");

	}

	@Test
	public void testProcessPaymentFailed() throws InterruptedException
	{
		setResultForAction("test.reserveOrderAmount", "NOK");

		final BusinessProcessModel process = createProcess("testPlaceorder");

		assertStep(process, "checkOrder");
		assertStep(process, "checkAuthorizeOrderPayment");
		assertStep(process, "reserveAmount");
		assertStep(process, "sendPaymentFailedNotification");
	}

	@Test
	public void testProcessAuthorizationFailed() throws InterruptedException
	{
		setResultForAction("test.checkAuthorizeOrderPayment", "NOK");

		final BusinessProcessModel process = createProcess("testPlaceorder");

		assertStep(process, "checkOrder");
		assertStep(process, "checkAuthorizeOrderPayment");
		assertStep(process, "authorizationFailedNotification");
	}


	@Test
	public void testProcessOk() throws InterruptedException
	{
		final BusinessProcessModel process = createProcess("testPlaceorder");

		assertStep(process, "checkOrder");
		assertStep(process, "checkAuthorizeOrderPayment");
		assertStep(process, "reserveAmount");
		assertStep(process, "fraudCheck");
		assertStep(process, "sendOrderPlacedNotification");
		assertStep(process, "splitOrder");

		assertStep(process, "waitBeforeTransmission");
		assertStep(process, "sendConsignmentToWarehouse");
		assertStep(process, "receiveConsignmentStatus");
		assertStep(process, "takePayment");
		assertStep(process, "allowShipment");
		assertStep(process, "sendDeliveryMessage");
		assertStep(process, "subprocessEnd");

		assertStep(process, "isProcessCompleted");
		assertStep(process, "sendOrderCompletedNotification");

	}

	//tools
	private BusinessProcessModel createProcess(final String processName)
	{
		final String id = "Test" + (new Date()).getTime();
		final BusinessProcessModel process = processService.startProcess(id, processName);
		assertProcessState(process, ProcessState.RUNNING);
		modelService.save(process);
		return process;
	}

	private void setResultForAction(final String action, final String result)
	{
		final TestActionTemp tmp = (TestActionTemp) getBean(action);
		tmp.setResult(result);
	}

	private void setThrowExceptionForAction(final String action, final boolean throwException)
	{
		final TestActionTemp tmp = (TestActionTemp) getBean(action);
		tmp.setThrowException(throwException);
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

				assertNotNull("No taks found for bean " + bean + " found: " + found, processTaskModel);
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

	private void assertProcessState(final BusinessProcessModel process, final ProcessState state)
	{
		modelService.refresh(process);
		assertEquals("Process state", state, process.getState());
	}

}
