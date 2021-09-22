package com.javamapper.entity;

public class Customer {
	private String id;
	private String name;
	private String accountNumber;
	private Double withdrawBalance;
	private Double taxPaidBalance;
	private Double loanBalance;
	private Double insuranceBalance;
	private Double investmentBalance;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Double getWithdrawBalance() {
		return withdrawBalance;
	}

	public void setWithdrawBalance(Double withdrawBalance) {
		this.withdrawBalance = withdrawBalance;
	}

	public Double getLoanBalance() {
		return loanBalance;
	}

	public void setLoanBalance(Double loanBalance) {
		this.loanBalance = loanBalance;
	}

	public Double getTaxPaidBalance() {
		return taxPaidBalance;
	}

	public void setTaxPaidBalance(Double taxPaidBalance) {
		this.taxPaidBalance = taxPaidBalance;
	}

	public Double getInsuranceBalance() {
		return insuranceBalance;
	}

	public void setInsuranceBalance(Double insuranceBalance) {
		this.insuranceBalance = insuranceBalance;
	}

	public Double getInvestmentBalance() {
		return investmentBalance;
	}

	public void setInvestmentBalance(Double investmentBalance) {
		this.investmentBalance = investmentBalance;
	}

}
