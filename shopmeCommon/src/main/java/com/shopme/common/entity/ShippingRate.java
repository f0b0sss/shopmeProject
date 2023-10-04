package com.shopme.common.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "shipping_rates")
public class ShippingRate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private float rate;
	private int days;

	@Column(name = "cod_supported")
	private boolean codSupported;

	@ManyToOne
	@JoinColumn(name = "country_id")
	private Country country;

	@Column(nullable = false, length = 45)
	private String state;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public boolean isCodSupported() {
		return codSupported;
	}

	public void setCodSupported(boolean codSupported) {
		this.codSupported = codSupported;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ShippingRate that = (ShippingRate) o;
		return Float.compare(that.rate, rate) == 0 && days == that.days && codSupported == that.codSupported && Objects.equals(id, that.id) && Objects.equals(country, that.country) && Objects.equals(state, that.state);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, rate, days, codSupported, country, state);
	}
}