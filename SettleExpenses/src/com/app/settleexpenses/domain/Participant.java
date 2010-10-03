package com.app.settleexpenses.domain;

import java.util.ArrayList;
import java.util.List;


public class Participant {

	private String id;
    private String name;
	private final List<Email> emails;
	private final List<Phone> phoneNumbers;

    public Participant(String id, String name) {
        this(id, name, new ArrayList<Email>(), new ArrayList<Phone>());
    }

    public Participant(String id, String name, List<Email> emails, List<Phone> phoneNumbers) {
        this.id = id;
        this.name = name;
		this.emails = emails;
		this.phoneNumbers = phoneNumbers;
    }
    
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public List<Email> getEmails() {
		return emails;
	}

	public List<Phone> getPhoneNumbers() {
		return phoneNumbers;
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
