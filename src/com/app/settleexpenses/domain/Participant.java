package com.app.settleexpenses.domain;


public class Participant {

	private String id;

	public Participant(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Participant that = (Participant) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
