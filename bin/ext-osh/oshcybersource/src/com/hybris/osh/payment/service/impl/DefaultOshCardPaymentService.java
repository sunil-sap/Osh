/**
 *
 */
package com.hybris.osh.payment.service.impl;

import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.payment.commands.AuthorizationCommand;
import de.hybris.platform.payment.commands.CaptureCommand;
import de.hybris.platform.payment.commands.CreateSubscriptionCommand;
import de.hybris.platform.payment.commands.DeleteSubscriptionCommand;
import de.hybris.platform.payment.commands.EnrollmentCheckCommand;
import de.hybris.platform.payment.commands.FollowOnRefundCommand;
import de.hybris.platform.payment.commands.GetSubscriptionDataCommand;
import de.hybris.platform.payment.commands.PartialCaptureCommand;
import de.hybris.platform.payment.commands.StandaloneRefundCommand;
import de.hybris.platform.payment.commands.SubscriptionAuthorizationCommand;
import de.hybris.platform.payment.commands.UpdateSubscriptionCommand;
import de.hybris.platform.payment.commands.VoidCommand;
import de.hybris.platform.payment.commands.factory.CommandFactory;
import de.hybris.platform.payment.commands.factory.CommandNotSupportedException;
import de.hybris.platform.payment.commands.request.AuthorizationRequest;
import de.hybris.platform.payment.commands.request.CaptureRequest;
import de.hybris.platform.payment.commands.request.CreateSubscriptionRequest;
import de.hybris.platform.payment.commands.request.DeleteSubscriptionRequest;
import de.hybris.platform.payment.commands.request.EnrollmentCheckRequest;
import de.hybris.platform.payment.commands.request.FollowOnRefundRequest;
import de.hybris.platform.payment.commands.request.PartialCaptureRequest;
import de.hybris.platform.payment.commands.request.StandaloneRefundRequest;
import de.hybris.platform.payment.commands.request.SubscriptionAuthorizationRequest;
import de.hybris.platform.payment.commands.request.SubscriptionDataRequest;
import de.hybris.platform.payment.commands.request.UpdateSubscriptionRequest;
import de.hybris.platform.payment.commands.request.VoidRequest;
import de.hybris.platform.payment.commands.result.AuthorizationResult;
import de.hybris.platform.payment.commands.result.CaptureResult;
import de.hybris.platform.payment.commands.result.EnrollmentCheckResult;
import de.hybris.platform.payment.commands.result.RefundResult;
import de.hybris.platform.payment.commands.result.SubscriptionDataResult;
import de.hybris.platform.payment.commands.result.SubscriptionResult;
import de.hybris.platform.payment.commands.result.VoidResult;
import de.hybris.platform.payment.methods.impl.DefaultCardPaymentServiceImpl;

import com.hybris.osh.command.request.FullReversalRequest;
import com.hybris.osh.command.result.FullReversalResult;
import com.hybris.osh.payment.command.FullReversalCommand;
import com.hybris.osh.payment.service.OshCardPaymentService;


/**
 *
 */
public class DefaultOshCardPaymentService extends DefaultCardPaymentServiceImpl implements OshCardPaymentService
{
	/**
	 * this method is responsible to creating a command and perform Full Authorization Reversal
	 */
	@Override
	public FullReversalResult FullReversal(final FullReversalRequest request)
	{
		try
		{
			final FullReversalCommand command = getCommandFactoryRegistry().getFactory(request.getPaymentProvider()).createCommand(
					FullReversalCommand.class);

			return command.perform(request);
		}
		catch (final CommandNotSupportedException e)
		{
			throw new AdapterException(e.getMessage(), e);
		}
		catch (final Exception e)
		{
			throw new AdapterException(e.getMessage(), e);
		}
	}






	@Override
	public RefundResult refundStandalone(final StandaloneRefundRequest request) throws AdapterException
	{
		try
		{
			final CommandFactory commandFactory = getCommandFactoryRegistry().getFactory(request.getCard(), false);
			final StandaloneRefundCommand command1 = commandFactory.createCommand(StandaloneRefundCommand.class);
			final RefundResult result = command1.perform(request);
			result.setPaymentProvider(commandFactory.getPaymentProvider());
			return result;

		}
		catch (final CommandNotSupportedException e)
		{
			throw new AdapterException(e.getMessage(), e);
		}
	}

	@Override
	public AuthorizationResult authorize(final AuthorizationRequest request) throws AdapterException
	{
		try
		{
			final CommandFactory commandFactory = getCommandFactoryRegistry().getFactory(request.getCard(), false);
			final AuthorizationCommand command = commandFactory.createCommand(AuthorizationCommand.class);
			final AuthorizationResult result = command.perform(request);
			result.setPaymentProvider(commandFactory.getPaymentProvider());

			return result;
		}
		catch (final CommandNotSupportedException e)
		{
			throw new AdapterException(e.getMessage(), e);

		}
		catch (final Exception e)
		{
			throw new AdapterException(e.getMessage(), e);

		}
	}

	@Override
	public AuthorizationResult authorize(final SubscriptionAuthorizationRequest request) throws AdapterException
	{
		try
		{
			CommandFactory commandFactory;
			if (request.getPaymentProvider() == null)
			{
				commandFactory = getCommandFactoryRegistry().getFactory(null, false);
			}
			else
			{
				commandFactory = getCommandFactoryRegistry().getFactory(request.getPaymentProvider());
			}
			final SubscriptionAuthorizationCommand command = commandFactory.createCommand(SubscriptionAuthorizationCommand.class);
			final AuthorizationResult result = command.perform(request);
			result.setPaymentProvider(commandFactory.getPaymentProvider());

			return result;
		}
		catch (final CommandNotSupportedException e)
		{
			throw new AdapterException(e.getMessage(), e);
		}
		catch (final Exception e)
		{
			throw new AdapterException(e.getMessage(), e);
		}
	}

	@Override
	public CaptureResult capture(final CaptureRequest request) throws AdapterException
	{

		try
		{
			final CaptureCommand command = getCommandFactoryRegistry().getFactory(request.getPaymentProvider()).createCommand(
					CaptureCommand.class);

			return command.perform(request);
		}
		catch (final CommandNotSupportedException e)
		{
			throw new AdapterException(e.getMessage(), e);
		}

	}

	@Override
	public CaptureResult partialCapture(final PartialCaptureRequest request) throws AdapterException
	{
		try
		{
			final PartialCaptureCommand command = getCommandFactoryRegistry().getFactory(request.getPaymentProvider())
					.createCommand(PartialCaptureCommand.class);
			return command.perform(request);
		}
		catch (final CommandNotSupportedException e)
		{
			throw new AdapterException(e.getMessage(), e);
		}

	}

	@Override
	public EnrollmentCheckResult enrollmentCheck(final EnrollmentCheckRequest request) throws AdapterException
	{
		final CommandFactory commandFactory = getCommandFactoryRegistry().getFactory(request.getCard(), true);


		try
		{
			final EnrollmentCheckCommand command = commandFactory.createCommand(EnrollmentCheckCommand.class);
			final EnrollmentCheckResult result = command.perform(request);

			result.setPaymentProvider(commandFactory.getPaymentProvider());

			return result;

		}
		catch (final CommandNotSupportedException e)
		{
			throw new AdapterException(e.getMessage(), e);
		}

	}

	@Override
	public RefundResult refundFollowOn(final FollowOnRefundRequest request) throws AdapterException
	{
		try
		{
			final FollowOnRefundCommand command = getCommandFactoryRegistry().getFactory(request.getPaymentProvider())
					.createCommand(FollowOnRefundCommand.class);

			return command.perform(request);
		}
		catch (final CommandNotSupportedException e)
		{
			throw new AdapterException(e.getMessage(), e);
		}
	}


	@Override
	public VoidResult voidCreditOrCapture(final VoidRequest request) throws AdapterException
	{
		try
		{
			final VoidCommand command = getCommandFactoryRegistry().getFactory(request.getPaymentProvider()).createCommand(
					VoidCommand.class);
			return command.perform(request);
		}
		catch (final CommandNotSupportedException e)
		{
			throw new AdapterException(e.getMessage(), e);
		}
	}

	@Override
	public SubscriptionResult createSubscription(final CreateSubscriptionRequest request) throws AdapterException
	{
		try
		{
			final CreateSubscriptionCommand command = getCommandFactoryRegistry().getFactory(request.getPaymentProvider())
					.createCommand(CreateSubscriptionCommand.class);

			return command.perform(request);
		}
		catch (final CommandNotSupportedException e)
		{
			throw new AdapterException(e.getMessage(), e);
		}
	}

	@Override
	public SubscriptionResult updateSubscription(final UpdateSubscriptionRequest request) throws AdapterException
	{
		try
		{
			final UpdateSubscriptionCommand command = getCommandFactoryRegistry().getFactory(request.getPaymentProvider())
					.createCommand(UpdateSubscriptionCommand.class);

			return command.perform(request);
		}
		catch (final CommandNotSupportedException e)
		{
			throw new AdapterException(e.getMessage(), e);
		}
	}

	@Override
	public SubscriptionDataResult getSubscriptionData(final SubscriptionDataRequest request) throws AdapterException
	{
		try
		{
			final GetSubscriptionDataCommand command = getCommandFactoryRegistry().getFactory(request.getPaymentProvider())
					.createCommand(GetSubscriptionDataCommand.class);

			return command.perform(request);
		}
		catch (final CommandNotSupportedException e)
		{
			throw new AdapterException(e.getMessage(), e);
		}
	}

	@Override
	public SubscriptionResult deleteSubscription(final DeleteSubscriptionRequest request) throws AdapterException
	{
		try
		{
			final DeleteSubscriptionCommand command = getCommandFactoryRegistry().getFactory(request.getPaymentProvider())
					.createCommand(DeleteSubscriptionCommand.class);

			return command.perform(request);
		}
		catch (final CommandNotSupportedException e)
		{
			throw new AdapterException(e.getMessage(), e);
		}
	}

	@Override
	public RefundResult oshRefundStandalone(final StandaloneRefundRequest request, final String paymentProvider)
			throws AdapterException
	{
		try
		{
			final StandaloneRefundCommand command = getCommandFactoryRegistry().getFactory(paymentProvider).createCommand(
					StandaloneRefundCommand.class);
			final RefundResult result = command.perform(request);
			result.setPaymentProvider(paymentProvider);
			return result;
		}
		catch (final CommandNotSupportedException e)
		{
			throw new AdapterException(e.getMessage(), e);
		}

	}
}
