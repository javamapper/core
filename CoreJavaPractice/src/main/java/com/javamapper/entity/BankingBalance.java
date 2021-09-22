package com.javamapper.entity;

import java.util.concurrent.locks.StampedLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.javamapper.common.AppConstants.PAYMENT_TYPE;

public class BankingBalance {
	private static Logger logger = LoggerFactory.getLogger(BankingBalance.class);
	StampedLock withdrawBalanceLock;
	StampedLock loanBalanceLock;
	StampedLock taxPaidBalanceLock;
	StampedLock insuranceBalanceLock;
	StampedLock investmentBalanceLock;
	Customer customer;

	public BankingBalance(Customer customer) {
		withdrawBalanceLock = new StampedLock();
		loanBalanceLock = new StampedLock();
		taxPaidBalanceLock = new StampedLock();
		insuranceBalanceLock = new StampedLock();
		investmentBalanceLock = new StampedLock();
		this.customer = customer;
	}

	/**
	 * Loan service. Credit - WithDrawBalance+1, LoanBalance-1 Debit -
	 * WithDrawBalance-1, LoanBalance+1
	 * 
	 * @param payType the pay type
	 * @param amount  the amount
	 */
	public void loanUpdate(PAYMENT_TYPE payType, Double amount) {
		switch (payType) {
		case CREDIT: // Credit in loan amount: Means more amount taken in loan
			long creditLoanBalanceWriteLock = loanBalanceLock.writeLock();
			long debitWithDrawBalanceWriteLock = withdrawBalanceLock.writeLock();
			try {
				customer.setWithdrawBalance(customer.getWithdrawBalance() + amount);
				customer.setLoanBalance(customer.getLoanBalance() - amount);
			} finally {
				loanBalanceLock.unlock(creditLoanBalanceWriteLock);
				withdrawBalanceLock.unlock(debitWithDrawBalanceWriteLock);
			}
			break;
		case DEBIT: // Clear amount from loan amount
			long debitLoanBalanceWriteLock = loanBalanceLock.writeLock();
			long creditWithDrawBalanceWriteLock = withdrawBalanceLock.writeLock();
			try {
				customer.setLoanBalance(customer.getLoanBalance() + amount);
				customer.setWithdrawBalance(customer.getWithdrawBalance() - amount);
			} finally {
				loanBalanceLock.unlock(debitLoanBalanceWriteLock);
				withdrawBalanceLock.unlock(creditWithDrawBalanceWriteLock);
			}
			break;
		case STATUS: // Get statement
			long tryOptimisticRead = loanBalanceLock.tryOptimisticRead();
			if (!loanBalanceLock.validate(tryOptimisticRead)) {
				long statusLoanBalanceReadLock = loanBalanceLock.readLock();
				try {
					logger.info("send status LOAN to printService:{}", customer.getLoanBalance());
				} finally {
					loanBalanceLock.unlock(statusLoanBalanceReadLock);
				}
			}
			break;
		default:

		}
	}

	/**
	 * Atm service.
	 * 
	 * Credit - WithDrawBalance+1 Debit - WithDrawBalance-1
	 * 
	 * @param payType the pay type
	 * @param amount  the amount
	 */
	public void atmUpdate(PAYMENT_TYPE payType, Double amount) {
		switch (payType) {
		case CREDIT:
			long creditWithDrawBalanceWriteLock = withdrawBalanceLock.writeLock();
			try {
				customer.setWithdrawBalance(customer.getWithdrawBalance() + amount);
			} finally {
				withdrawBalanceLock.unlock(creditWithDrawBalanceWriteLock);
			}
			break;
		case DEBIT:
			long debitWithDrawBalanceWriteLock = withdrawBalanceLock.writeLock();
			try {
				customer.setWithdrawBalance(customer.getWithdrawBalance() - amount);
			} finally {
				withdrawBalanceLock.unlock(debitWithDrawBalanceWriteLock);
				logger.info("DEBIT by amount:{}",amount);
			}
			break;
		case STATUS:
			long tryOptimisticRead = withdrawBalanceLock.tryOptimisticRead();
			if (withdrawBalanceLock.validate(tryOptimisticRead)) {
				logger.info("send status ATM to printService:{}", customer.getWithdrawBalance());
			}else {
				long statuswithdrawBalanceReadLock = withdrawBalanceLock.readLock();
				try {
					logger.info("send status ATM to printService:{}", customer.getWithdrawBalance());
				}finally {
					withdrawBalanceLock.unlock(statuswithdrawBalanceReadLock);
				}
			}
			break;
		default:

		}
	}

	/**
	 * Tax service. Credit - WithDrawBalance+1, TaxPaidBalance-1 Debit -
	 * WithDrawBalance-1, TaxPaidBalance+1
	 * 
	 * @param payType the pay type
	 * @param amount  the amount
	 */
	public void taxUpdate(PAYMENT_TYPE payType, Double amount) {
		switch (payType) {
		case CREDIT:
			long creditWithDrawWriteLock = withdrawBalanceLock.writeLock();
			long debitTaxPaidBalanceWriteLock = taxPaidBalanceLock.writeLock();
			try {
				customer.setWithdrawBalance(customer.getWithdrawBalance() + amount);
				customer.setTaxPaidBalance(customer.getTaxPaidBalance() - amount);
			} finally {
				taxPaidBalanceLock.unlock(debitTaxPaidBalanceWriteLock);
				withdrawBalanceLock.unlock(creditWithDrawWriteLock);
			}
			break;
		case DEBIT:
			long debitWithDrawWriteLock = withdrawBalanceLock.writeLock();
			long creditTaxPaidBalanceWriteLock = taxPaidBalanceLock.writeLock();
			try {
				customer.setWithdrawBalance(customer.getWithdrawBalance() - amount);
				customer.setTaxPaidBalance(customer.getTaxPaidBalance() + amount);
			} finally {
				taxPaidBalanceLock.unlock(creditTaxPaidBalanceWriteLock);
				withdrawBalanceLock.unlock(debitWithDrawWriteLock);
			}
			break;
		case STATUS:
			long tryOptimisticRead = withdrawBalanceLock.tryOptimisticRead();
			if (!withdrawBalanceLock.validate(tryOptimisticRead)) {
				long statusWithDrawBalanceWriteLock = withdrawBalanceLock.readLock();
				try {
					logger.info("send status TAX to printService:{}", customer.getWithdrawBalance());
				} finally {
					withdrawBalanceLock.unlock(statusWithDrawBalanceWriteLock);
				}
			}
			break;
		default:

		}
	}

	/**
	 * Cheque service.
	 *
	 * @param payType the pay type
	 * @param amount  the amount
	 */
	public void chequeUpdate(PAYMENT_TYPE payType, Double amount) {
		switch (payType) {
		case CREDIT:
			long creditWithDrawBalanceWriteLock = withdrawBalanceLock.writeLock();
			try {
				customer.setWithdrawBalance(customer.getWithdrawBalance() + amount);
			} finally {
				withdrawBalanceLock.unlock(creditWithDrawBalanceWriteLock);
			}
			break;
		case DEBIT:
			long debitWithDrawBalanceWriteLock = withdrawBalanceLock.writeLock();
			try {
				customer.setWithdrawBalance(customer.getWithdrawBalance() - amount);
			} finally {
				withdrawBalanceLock.unlock(debitWithDrawBalanceWriteLock);
			}
			break;
		case STATUS:
			long tryOptimisticRead = withdrawBalanceLock.tryOptimisticRead();
			if (!withdrawBalanceLock.validate(tryOptimisticRead)) {
				long statusWithDrawBalanceWriteLock = withdrawBalanceLock.readLock();
				try {
					logger.info("send status CHEQUE to printService:{}", customer.getWithdrawBalance());
				} finally {
					withdrawBalanceLock.unlock(statusWithDrawBalanceWriteLock);
				}
			}
			break;
		default:

		}
	}

	/**
	 * Dd service.
	 *
	 * @param payType the pay type
	 * @param amount  the amount
	 */
	public void ddUpdate(PAYMENT_TYPE payType, Double amount) {
		switch (payType) {
		case CREDIT:
			long creditWithDrawBalanceWriteLock = withdrawBalanceLock.writeLock();
			try {
				customer.setWithdrawBalance(customer.getWithdrawBalance() + amount);
			} finally {
				withdrawBalanceLock.unlock(creditWithDrawBalanceWriteLock);
			}
			break;
		case DEBIT:
			long debitWithDrawBalanceWriteLock = withdrawBalanceLock.writeLock();
			try {
				customer.setWithdrawBalance(customer.getWithdrawBalance() - amount);
			} finally {
				withdrawBalanceLock.unlock(debitWithDrawBalanceWriteLock);
			}
			break;
		case STATUS:
			long tryOptimisticRead = withdrawBalanceLock.tryOptimisticRead();
			if (!withdrawBalanceLock.validate(tryOptimisticRead)) {
				long statusWithDrawBalanceWriteLock = withdrawBalanceLock.readLock();
				try {
					logger.info("send status DD to printService:{}", customer.getWithdrawBalance());
				} finally {
					withdrawBalanceLock.unlock(statusWithDrawBalanceWriteLock);
				}
			}
			break;
		default:

		}
	}

	/**
	 * Online banking service.
	 *
	 * @param payType the pay type
	 * @param amount  the amount
	 */
	public void onlineBankingUpdate(PAYMENT_TYPE payType, Double amount) {
		switch (payType) {
		case CREDIT:
			long creditWithDrawBalanceWriteLock = withdrawBalanceLock.writeLock();
			try {
				customer.setWithdrawBalance(customer.getWithdrawBalance() + amount);
			} finally {
				withdrawBalanceLock.unlock(creditWithDrawBalanceWriteLock);
			}
			break;
		case DEBIT:
			long debitWithDrawBalanceWriteLock = withdrawBalanceLock.writeLock();
			try {
				customer.setWithdrawBalance(customer.getWithdrawBalance() - amount);
			} finally {
				withdrawBalanceLock.unlock(debitWithDrawBalanceWriteLock);
			}
			break;
		case STATUS:
			long tryOptimisticRead = withdrawBalanceLock.tryOptimisticRead();
			if (!withdrawBalanceLock.validate(tryOptimisticRead)) {
				long statusWithDrawBalanceWriteLock = withdrawBalanceLock.readLock();
				try {
					logger.info("send status Online to printService:{}", customer.getWithdrawBalance());
				} finally {
					withdrawBalanceLock.unlock(statusWithDrawBalanceWriteLock);
				}
			}
			break;
		default:

		}
	}

	/**
	 * Upi service.
	 *
	 * @param payType the pay type
	 * @param amount  the amount
	 */
	public void upiUpdate(PAYMENT_TYPE payType, Double amount) {
		switch (payType) {
		case CREDIT:
			long creditWithDrawBalanceWriteLock = withdrawBalanceLock.writeLock();
			try {
				customer.setWithdrawBalance(customer.getWithdrawBalance() + amount);
			} finally {
				withdrawBalanceLock.unlock(creditWithDrawBalanceWriteLock);
			}
			break;
		case DEBIT:
			long debitWithDrawBalanceWriteLock = withdrawBalanceLock.writeLock();
			try {
				customer.setWithdrawBalance(customer.getWithdrawBalance() - amount);
			} finally {
				withdrawBalanceLock.unlock(debitWithDrawBalanceWriteLock);
			}
			break;
		case STATUS:
			long tryOptimisticRead = withdrawBalanceLock.tryOptimisticRead();
			if (!withdrawBalanceLock.validate(tryOptimisticRead)) {
				long statusWithDrawBalanceWriteLock = withdrawBalanceLock.readLock();
				try {
					logger.info("send status UPI to printService:{}", customer.getWithdrawBalance());
				} finally {
					withdrawBalanceLock.unlock(statusWithDrawBalanceWriteLock);
				}
			}
			break;
		default:

		}
	}

	/**
	 * Insurance service. Credit - WithDrawBalance+1, InsuranceBalance-1 Debit -
	 * WithDrawBalance-1, InsuranceBalance+1
	 * 
	 * @param payType the pay type
	 * @param amount  the amount
	 */
	public void insuranceUpdate(PAYMENT_TYPE payType, Double amount) {
		switch (payType) {
		case CREDIT:
			long creditWithDrawBalanceWriteLock = withdrawBalanceLock.writeLock();
			long debitInsuranceBalanceLock = insuranceBalanceLock.writeLock();
			try {
				customer.setWithdrawBalance(customer.getWithdrawBalance() + amount);
				customer.setInsuranceBalance(customer.getInsuranceBalance() - amount);
			} finally {
				withdrawBalanceLock.unlock(creditWithDrawBalanceWriteLock);
				insuranceBalanceLock.unlock(debitInsuranceBalanceLock);
			}
			break;
		case DEBIT:
			long debitWithDrawBalanceWriteLock = withdrawBalanceLock.writeLock();
			long creditInsuranceBalanceLock = insuranceBalanceLock.writeLock();
			try {
				customer.setWithdrawBalance(customer.getWithdrawBalance() - amount);
				customer.setInsuranceBalance(customer.getInsuranceBalance() + amount);
			} finally {
				insuranceBalanceLock.unlock(creditInsuranceBalanceLock);
				withdrawBalanceLock.unlock(debitWithDrawBalanceWriteLock);
			}
			break;
		case STATUS:
			long tryOptimisticRead = insuranceBalanceLock.tryOptimisticRead();
			if (!insuranceBalanceLock.validate(tryOptimisticRead)) {
				long statusInsuranceBalanceReadLock = insuranceBalanceLock.readLock();
				try {
					logger.info("send status Insurance to printService:{}", customer.getInsuranceBalance());
				} finally {
					insuranceBalanceLock.unlock(statusInsuranceBalanceReadLock);
				}
			}
			break;
		default:

		}
	}

	/**
	 * Investment service. Credit - WithDrawBalance+1, InvestmentBalance-1 Debit -
	 * WithDrawBalance-1, InvestmentBalance+1
	 * 
	 * @param payType the pay type
	 * @param amount  the amount
	 */
	public void investmentUpdate(PAYMENT_TYPE payType, Double amount) {
		switch (payType) {
		case CREDIT:
			long creditWithDrawBalanceWriteLock = withdrawBalanceLock.writeLock();
			long debitInvestmentBalanceLock = investmentBalanceLock.writeLock();
			try {
				customer.setWithdrawBalance(customer.getWithdrawBalance() + amount);
				customer.setInvestmentBalance(customer.getInvestmentBalance() - amount);
			} finally {
				withdrawBalanceLock.unlock(creditWithDrawBalanceWriteLock);
				insuranceBalanceLock.unlock(debitInvestmentBalanceLock);
			}
			break;
		case DEBIT:
			long debitWithDrawBalanceWriteLock = withdrawBalanceLock.writeLock();
			long creditInvestmentBalanceLock = investmentBalanceLock.writeLock();
			try {
				customer.setWithdrawBalance(customer.getWithdrawBalance() - amount);
				customer.setInvestmentBalance(customer.getInvestmentBalance() + amount);
			} finally {
				insuranceBalanceLock.unlock(creditInvestmentBalanceLock);
				withdrawBalanceLock.unlock(debitWithDrawBalanceWriteLock);
			}
			break;
		case STATUS:
			long tryOptimisticRead = investmentBalanceLock.tryOptimisticRead();
			if (!insuranceBalanceLock.validate(tryOptimisticRead)) {
				logger.info("send status InvestmentBalance to printService:{}", customer.getInvestmentBalance());
			}
			break;
		default:

		}
	}
}
