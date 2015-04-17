package com.hybris.osh.actions;

import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.processengine.action.AbstractAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.promotions.util.Helper;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.task.RetryLaterException;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.hybris.osh.core.promotion.model.OshTaxPromotionModel;
import com.hybris.osh.payment.services.OshPaymentService;


public class ReturnOrderAuthorization extends AbstractAction{

	private UserService userService;
	private OshPaymentService oshPaymentService;
	private boolean isTaxPromo=false;
	
	@Resource(name = "modelService")
	private ModelService modelService;
	private static final String STATUS_NOK = "NOK";
    private static final String STATUS_OK = "OK";

    private static Set<String> transitions = createTransitions(STATUS_OK, STATUS_NOK);
    private final static Logger LOG = Logger.getLogger(ReturnOrderAuthorization.class);

	@Override
	public String execute(final BusinessProcessModel process) throws RetryLaterException, Exception
	{
		
		PaymentTransactionEntryModel txnResultEntry=null;
		Logger.getLogger(this.getClass()).debug("The transaction is going for refund transaction.");
		final SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
		
		if (process instanceof OrderProcessModel)
        {
			final BusinessProcessModel bpm = process;
			//process to calculate ammount of refund order.
			final OrderModel order = ((OrderProcessModel) bpm).getOrder();
			final Set<PromotionResultModel> promotions = order.getAllPromotionResults();
			for (final PromotionResultModel promotionResultModel : promotions)
			{
				if (promotionResultModel.getPromotion() instanceof OshTaxPromotionModel)
				{
					isTaxPromo = true;
				}
			}
			final List<ReturnRequestModel> returnRequestModel=order.getReturnRequests();
			for(final ReturnRequestModel refundEntry:returnRequestModel){
				final List<ReturnEntryModel> returnEntryModel=refundEntry.getReturnEntries();

				for(final ReturnEntryModel returnEntry:returnEntryModel)
				{
					final RefundEntryModel refundEntryModel=(RefundEntryModel) returnEntry;
					if((refundEntryModel.getStatus().equals(ReturnStatus.RECEIVED)) && !(refundEntryModel.isRefundDone()) )
					{
						Double refundableTaxAmount= 0.0d;
						double calculateTotalPrice=0.0d;
						double refundTaxAmount = refundEntryModel.getOrderEntry().getTaxPerUnit()*refundEntryModel.getExpectedQuantity();
						refundableTaxAmount=Helper.roundCurrencyValue(ctx, ctx.getCurrency(), refundTaxAmount).doubleValue();
						Double calculatedUnitPrize = refundEntryModel.getOrderEntry().getDiscountPrice();
						double calculatePrice=calculatedUnitPrize * refundEntryModel.getExpectedQuantity();
						calculateTotalPrice=Helper.roundCurrencyValue(ctx, ctx.getCurrency(), calculatePrice).doubleValue();
						double refundAmount = (calculateTotalPrice) + refundableTaxAmount;
						if(isTaxPromo){
							refundAmount=calculatePrice+refundTaxAmount;
						}
						refundAmount = Double.valueOf(Helper.roundCurrencyValue(ctx, ctx.getCurrency(), refundAmount).doubleValue());
						refundEntryModel.setRefundDone(true);

						//for eod tlog files
						if (refundEntry.isTlogPerformed())
						{
							refundEntry.setTlogPerformed(false);
							modelService.save(refundEntry);
							modelService.refresh(refundEntry);
						}

						modelService.save(refundEntryModel);
						modelService.refresh(refundEntryModel);
						final ConsignmentEntryModel consignmentEntry = returnEntry.getOrderEntry().getConsignmentEntries().iterator().next();
						
							txnResultEntry = oshPaymentService.partialRefundFollowOn(consignmentEntry,BigDecimal.valueOf(refundAmount));
					}
				}
			}
			
			if(txnResultEntry!=null){
				if (txnResultEntry.getTransactionStatus().equalsIgnoreCase(TransactionStatus.ACCEPTED.name()))
				{
					Calendar cal = Calendar.getInstance();
					Logger.getLogger(this.getClass()).debug("Refund successful.");
					//for eod tlog files
					order.setTlogPerformed(false);
					order.setReturnTransactionDate(cal.getTime());
					modelService.save(order);
					modelService.refresh(order);
					return STATUS_OK;
				}
				else
				{
					for(final ReturnRequestModel refundEntry:returnRequestModel){
						final List<ReturnEntryModel> returnEntryModel=refundEntry.getReturnEntries();

						for(final ReturnEntryModel returnEntry:returnEntryModel)
						{
							final RefundEntryModel refundEntryModel=(RefundEntryModel) returnEntry;	
							refundEntryModel.setStatus(ReturnStatus.CANCELED);
							refundEntryModel.setRefundDone(false);
							modelService.save(refundEntryModel);
							modelService.refresh(refundEntryModel);	
						}

					}
					Logger.getLogger(this.getClass()).error("Processing error - Cancel command failed.");
					return STATUS_NOK;
				}
			}
        }
		return STATUS_NOK;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @return the oshPaymentService
	 */
	public OshPaymentService getOshPaymentService()
	{
		return oshPaymentService;
	}

	/**
	 * @param oshPaymentService
	 *           the oshPaymentService to set
	 */
	public void setOshPaymentService(final OshPaymentService oshPaymentService)
	{
		this.oshPaymentService = oshPaymentService;
	}
	@Override
	public Set getTransitions() {
		return transitions;
	}
}
