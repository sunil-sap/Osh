package com.hybris.osh.actions;

import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.payment.commands.request.FollowOnRefundRequest;
import de.hybris.platform.payment.commands.result.RefundResult;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.processengine.action.AbstractAction;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.promotions.util.Helper;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.task.RetryLaterException;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.zkoss.util.logging.Log;

import com.hybris.osh.core.model.CreditPaymentTransactionModel;
import com.hybris.osh.core.promotion.model.OshTaxPromotionModel;
import com.hybris.osh.payment.service.OshCardPaymentService;
import com.hybris.osh.payment.services.OshPaymentService;

public class CustomerCreditConfirmation extends AbstractAction {

	private UserService userService;
	private OshPaymentService oshPaymentService;
	private CommonI18NService commonI18NService;

	private boolean isTaxPromo = false;
	private OshCardPaymentService oshCardPaymentService;

	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}

	public void setCommonI18NService(CommonI18NService commonI18NService) {
		this.commonI18NService = commonI18NService;
	}

	public OshCardPaymentService getOshCardPaymentService() {
		return oshCardPaymentService;
	}

	public void setOshCardPaymentService(
			OshCardPaymentService oshCardPaymentService) {
		this.oshCardPaymentService = oshCardPaymentService;
	}

	@Resource(name = "modelService")
	private ModelService modelService;
	private static final String STATUS_NOK = "NOK";
	private static final String STATUS_OK = "OK";

	private static Set<String> transitions = createTransitions(STATUS_OK,
			STATUS_NOK);

	@SuppressWarnings("null")
	@Override
	public String execute(final BusinessProcessModel process)
			throws RetryLaterException, Exception {
		PaymentTransactionEntryModel txnResultEntry = null;
		Logger.getLogger(this.getClass()).debug(
				"The transaction is going for refund transaction.");
		final SessionContext ctx = JaloSession.getCurrentSession()
				.getSessionContext();

		if (process instanceof OrderProcessModel) {
			final OrderModel order = ((OrderProcessModel) process).getOrder();

			List<CreditPaymentTransactionModel> listCreditPaymentTransactionModel = ((List<CreditPaymentTransactionModel>) order
					.getCreditPaymentTransaction());
			CreditPaymentTransactionModel creditPaymentTransactionModel = listCreditPaymentTransactionModel
					.get(listCreditPaymentTransactionModel.size() - 1);

			final Double refundAmount = creditPaymentTransactionModel
					.getCreditAmount();

			PaymentTransactionModel paymentTransactionModel = order
					.getPaymentTransactions().get(0);
			if (paymentTransactionModel == null) {
				Logger.getLogger(this.getClass()).error(
						"Processing error - Cancel command failed.");
				return STATUS_NOK;
			} else {

				final OrderHistoryEntryModel historyEntry = modelService
						.create(OrderHistoryEntryModel.class);
				try {

					final PaymentTransactionEntryModel entry = oshPaymentService
							.refundCreditAmount(paymentTransactionModel);

					creditPaymentTransactionModel.setTransactionStatus(entry
							.getTransactionStatus());

					if (entry.getTransactionStatus().equals(
							TransactionStatus.ACCEPTED.name())) {

						historyEntry.setDescription("Credited "
								+ order.getCurrency().getSymbol()
								+ refundAmount
								+ " to the customer account for order "
								+ order.getCode() + " Success;" + " Notes :-"+ creditPaymentTransactionModel.getCreditNotes() );
						order.setCreditTlog(true);
					} else {
						historyEntry.setDescription("Credited "
								+ order.getCurrency().getSymbol()
								+ refundAmount
								+ " to the customer account for order "
								+ order.getCode() + " Failed;" + " Notes :-"+ creditPaymentTransactionModel.getCreditNotes());
					}
				} catch (final Exception e) {
					creditPaymentTransactionModel
							.setTransactionStatus(TransactionStatus.REJECTED
									.toString());
					historyEntry.setDescription("Credited "
							+ order.getCurrency().getSymbol() + refundAmount
							+ " to the customer account for order "
							+ order.getCode() + " Failed;" + " Notes :-"+ creditPaymentTransactionModel.getCreditNotes());

				}
				historyEntry.setTimestamp(new Date());
				historyEntry.setOrder(order);
				modelService.save(creditPaymentTransactionModel);
				modelService.refresh(creditPaymentTransactionModel);
				modelService.save(historyEntry);
				modelService.refresh(historyEntry);
				modelService.save(process);
				modelService.refresh(process);
				modelService.save(order);
				modelService.refresh(order);
				return STATUS_OK;

			}
		}
		return STATUS_NOK;
	}

	protected String getNewEntryCode(final PaymentTransactionModel transaction) {
		if (transaction.getEntries() == null) {
			return (new StringBuilder(String.valueOf(transaction.getCode())))
					.toString();
		} else {
			return (new StringBuilder(String.valueOf(transaction.getCode())))
					.append("-")
					.append(transaction.getEntries().size() + Math.random())
					.toString();
		}
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * @param userService
	 *            the userService to set
	 */
	public void setUserService(final UserService userService) {
		this.userService = userService;
	}

	/**
	 * @return the oshPaymentService
	 */
	public OshPaymentService getOshPaymentService() {
		return oshPaymentService;
	}

	/**
	 * @param oshPaymentService
	 *            the oshPaymentService to set
	 */
	public void setOshPaymentService(final OshPaymentService oshPaymentService) {
		this.oshPaymentService = oshPaymentService;
	}

	@Override
	public Set getTransitions() {
		return transitions;
	}
}
